package com.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.Statement;


import com.Main.DataBase.DataBaseConnect;

public class DoneSurveyController {

    @FXML 
    private Button exitButton;

    @FXML 
    private Button loginButton;

    @FXML
    private Button save;

    @FXML 
    private ImageView stories;

    @FXML 
    private ImageView plus;

    @FXML 
    private ImageView home;

    @FXML 
    private VBox questionContainer;

    @FXML 
    private Text surveyTitle;

    @FXML 
    private Label surveyDescription;

    @FXML
    private Label idLabel;

    @FXML
    public void initialize() {
        exitButton.setOnAction(this::handleExit);
        plus.setOnMouseClicked(this::handlePlusClick);
        home.setOnMouseClicked(this::handleHomeClick);
        stories.setOnMouseClicked(this::handleStoriesClick);
        loginButton.setOnAction(this::handleLoginClick);
        save.setOnAction(this::handleDoneClick);

        Tooltip.install(home, new Tooltip("Домашня сторінка"));
        Tooltip.install(plus, new Tooltip("Створити нове"));
        Tooltip.install(stories, new Tooltip("Історія опитувань"));
    }

    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void handleLoginClick(ActionEvent event) {
        switchScene("/login.fxml");
    }

    private void handlePlusClick(MouseEvent event) {
        switchScene("/newsurvey.fxml");
    }

    private void handleHomeClick(MouseEvent event) {
        switchScene("/main.fxml");
    }

    private void handleStoriesClick(MouseEvent event) {
        switchScene("/stories.fxml");
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSurveyInfo(String title, String description) {
        surveyTitle.setText(title);
        surveyDescription.setText(description);
    }

    public void setQuestions(List<SurveyQuestion> questions) {
        questionContainer.getChildren().clear();

        for (SurveyQuestion q : questions) {
            VBox questionBox = new VBox(10);
            questionBox.setPadding(new Insets(10));
            questionBox.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 5;");

            Label questionLabel = new Label(q.getQuestionText());
            questionLabel.setFont(javafx.scene.text.Font.font(16));

            VBox optionsBox = new VBox(5);

            if ("Один варіант".equals(q.getType())) {
                ToggleGroup toggleGroup = new ToggleGroup();
                for (String option : q.getOptions()) {
                    RadioButton rb = new RadioButton(option);
                    rb.setToggleGroup(toggleGroup);
                    optionsBox.getChildren().add(rb);
                }
            } else if ("Декілька варіантів".equals(q.getType())) {
                for (String option : q.getOptions()) {
                    CheckBox cb = new CheckBox(option);
                    optionsBox.getChildren().add(cb);
                }
            } else if ("Власна відповідь".equals(q.getType())) {
                TextField answerField = new TextField();
                answerField.setPromptText("Ваша відповідь");
                optionsBox.getChildren().add(answerField);
            }

            questionBox.getChildren().addAll(questionLabel, optionsBox);
            questionContainer.getChildren().add(questionBox);
        }
    }

     @FXML
 private void handleDoneClick(ActionEvent event) {
    SurveyDataStore store = SurveyDataStore.getInstance();

    try (Connection conn = DataBaseConnect.connect()) {
        conn.setAutoCommit(false);

        // ⬇️ Отримуємо ID поточного користувача
        int currentUserId = Session.getCurrentUserId(); // Ти повинен мати Session клас, який зберігає user_id після логіну

        // ⬇️ Вставка опитування з user_id
        String sqlSurvey = "INSERT INTO surveys (title, description, user_id) VALUES (?, ?, ?)";
        PreparedStatement surveyStmt = conn.prepareStatement(sqlSurvey, Statement.RETURN_GENERATED_KEYS);
        surveyStmt.setString(1, store.getTitle());
        surveyStmt.setString(2, store.getDescription());
        surveyStmt.setInt(3, currentUserId);
        surveyStmt.executeUpdate();

        ResultSet surveyKeys = surveyStmt.getGeneratedKeys();
        int surveyId = -1;
        if (surveyKeys.next()) {
            surveyId = surveyKeys.getInt(1);
        }

        String sqlQuestion = "INSERT INTO questions (survey_id, text) VALUES (?, ?)";
        PreparedStatement questionStmt = conn.prepareStatement(sqlQuestion, Statement.RETURN_GENERATED_KEYS);

        String sqlAnswer = "INSERT INTO answers (question_id, answer, is_custom_allowed) VALUES (?, ?, ?)";
        PreparedStatement answerStmt = conn.prepareStatement(sqlAnswer);

        for (SurveyQuestion q : store.getQuestions()) {
            questionStmt.setInt(1, surveyId);
            questionStmt.setString(2, q.getQuestionText());
            questionStmt.executeUpdate();

            ResultSet questionKeys = questionStmt.getGeneratedKeys();
            int questionId = -1;
            if (questionKeys.next()) {
                questionId = questionKeys.getInt(1);
            }

            for (String ans : q.getAnswers()) {
                answerStmt.setInt(1, questionId);
                answerStmt.setString(2, ans);
                answerStmt.setBoolean(3, false);
                answerStmt.addBatch();
            }

            if (q.isCustomAllowed()) {
                answerStmt.setInt(1, questionId);
                answerStmt.setString(2, "Власний варіант");
                answerStmt.setBoolean(3, true);
                answerStmt.addBatch();
            }

            answerStmt.executeBatch();
        }

        conn.commit();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/donesurvey.fxml"));
        Parent root = loader.load();

        DoneSurveyController controller = loader.getController();
        controller.setSurveyInfo(store.getTitle(), store.getDescription());
        controller.setQuestions(store.getQuestions());

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

