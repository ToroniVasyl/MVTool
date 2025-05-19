package com.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.Main.DataBase.DataBaseConnect;

import javafx.event.ActionEvent;

public class StoriesController {

    @FXML
    private Button exitButton; 

    @FXML
    private ImageView stories;

    @FXML
    private ImageView plus;

    @FXML
    private ImageView home;

    @FXML
    private Button loginButton;

    @FXML
    private VBox surveyList;

    @FXML
    private VBox questionContainer;

    

    @FXML
    public void initialize() {

        exitButton.setOnAction(this::handleExit);
      
        Integer currentUserId = Session.getCurrentUserId();
    if (currentUserId != null) {
        loadSurveys(currentUserId);
    } else {
        // наприклад, вивести повідомлення, що користувач не авторизований
    }

    }

    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


  private void loadSurveys(int userId) {
    try (Connection conn = DataBaseConnect.connect()) {
      String sql = "SELECT sh.id AS history_id, s.title, s.description " +
             "FROM survey_history sh " +
             "JOIN surveys s ON sh.survey_id = s.id " +
             "WHERE sh.user_id = ? " +
             "ORDER BY sh.response_date DESC " +
             "LIMIT 10";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int historyId = rs.getInt("history_id");
            String title = rs.getString("title");
            String description = rs.getString("description");

            VBox surveyBox = new VBox(5);
            surveyBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-cursor: hand;");
            surveyBox.getChildren().addAll(
                new javafx.scene.control.Label("Назва: " + title),
                new javafx.scene.control.Label("Опис: " + description)
            );

            // відкриває DoneSurvey з historyId
            surveyBox.setOnMouseClicked(event -> openSurvey(historyId));

            surveyList.getChildren().add(surveyBox);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


// відкриває donesurvey.fxml і передає surveyId
private void openSurvey(int historyId) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/donesurvey.fxml"));
        Parent root = loader.load();

        DoneSurveyController controller = loader.getController();
        controller.loadSurveyFromHistory(historyId);  // новий метод, який треба реалізувати

        Stage stage = (Stage) surveyList.getScene().getWindow();
        stage.setScene(new Scene(root));

    } catch (IOException e) {
        e.printStackTrace();
    }
}

   private void showAnswersOnly(List<SurveyQuestion> questions) {
    questionContainer.getChildren().clear();

    for (SurveyQuestion q : questions) {
        VBox questionBox = new VBox(10);
        questionBox.setPadding(new Insets(10));
        questionBox.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 5;");

        Label questionLabel = new Label(q.getQuestionText());
        questionLabel.setFont(javafx.scene.text.Font.font(16));

        VBox optionsBox = new VBox(5);

        for (String answer : q.getSelectedAnswers()) {
            Label answerLabel = new Label("Відповідь: " + answer);
            answerLabel.setStyle("-fx-text-fill: #333;");
            optionsBox.getChildren().add(answerLabel);
        }

        questionBox.getChildren().addAll(questionLabel, optionsBox);
        questionContainer.getChildren().add(questionBox);
    }

    
}



}
