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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Statement;
import javafx.scene.Node;



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
    private String type;

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

  public List<String> collectUserAnswers(VBox optionsBox, String questionType) {
    List<String> selectedAnswers = new ArrayList<>();

    for (javafx.scene.Node node : optionsBox.getChildren()) {
        if ("Один варіант".equals(questionType)) {
            if (node instanceof RadioButton) {
                RadioButton rb = (RadioButton) node;
                if (rb.isSelected()) {
                    selectedAnswers.add(rb.getText());
                }
            }
        } else if ("Декілька варіантів".equals(questionType)) {
            if (node instanceof CheckBox) {
                CheckBox cb = (CheckBox) node;
                if (cb.isSelected()) {
                    selectedAnswers.add(cb.getText());
                }
            }
        } else if ("Власна відповідь".equals(questionType)) {
            if (node instanceof TextField) {
                TextField tf = (TextField) node;
                String answer = tf.getText().trim();
                if (!answer.isEmpty()) {
                    selectedAnswers.add(answer);
                }
            }
        }
    }
    return selectedAnswers;
}


    

@FXML
private void handleDoneClick(ActionEvent event) {
    SurveyDataStore store = SurveyDataStore.getInstance();

    // Оновлюємо відповіді користувача у кожному питанні, збираючи їх із UI
    int questionIndex = 0;
    for (Node questionNode : questionContainer.getChildren()) {
        if (questionNode instanceof VBox) {
            VBox questionBox = (VBox) questionNode;
            // questionBox має Label (питання) і VBox (опції)
            if (questionBox.getChildren().size() < 2) continue;

            Label questionLabel = (Label) questionBox.getChildren().get(0);
            VBox optionsBox = (VBox) questionBox.getChildren().get(1);

            SurveyQuestion q = store.getQuestions().get(questionIndex);

            List<String> selectedAnswers = collectUserAnswers(optionsBox, q.getType());
            q.setSelectedAnswers(selectedAnswers);  // припускаю, що є сеттер для вибраних відповідей

            questionIndex++;
        }
    }

    try (Connection conn = DataBaseConnect.connect()) {
        conn.setAutoCommit(false);

        int currentUserId = Session.getCurrentUserId();

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

        Map<SurveyQuestion, Integer> questionIdMap = new HashMap<>();

        for (SurveyQuestion q : store.getQuestions()) {
            questionStmt.setInt(1, surveyId);
            questionStmt.setString(2, q.getQuestionText());
            questionStmt.executeUpdate();

            ResultSet questionKeys = questionStmt.getGeneratedKeys();
            int questionId = -1;
            if (questionKeys.next()) {
                questionId = questionKeys.getInt(1);
            }

            questionIdMap.put(q, questionId);

            for (String opt : q.getOptions()) {
                answerStmt.setInt(1, questionId);
                answerStmt.setString(2, opt);
                answerStmt.setBoolean(3, false);
                answerStmt.addBatch();
            }

            if (q.isCustomAllowed()) {
                answerStmt.setInt(1, questionId);
                answerStmt.setString(2, "Власний варіант");
                answerStmt.setBoolean(3, true);
                answerStmt.addBatch();
            }
        }

        answerStmt.executeBatch();

        String historySql = "INSERT INTO survey_history (survey_id, user_id) VALUES (?, ?)";
        PreparedStatement historyStmt = conn.prepareStatement(historySql, Statement.RETURN_GENERATED_KEYS);
        historyStmt.setInt(1, surveyId);
        historyStmt.setInt(2, currentUserId);
        historyStmt.executeUpdate();

        ResultSet historyKeys = historyStmt.getGeneratedKeys();
        int surveyHistoryId = -1;
        if (historyKeys.next()) {
            surveyHistoryId = historyKeys.getInt(1);
        }

        String userAnswerSql = "INSERT INTO user_answers (survey_history_id, question_id, answer_text) VALUES (?, ?, ?)";
        PreparedStatement userAnswerStmt = conn.prepareStatement(userAnswerSql);

        for (SurveyQuestion q : store.getQuestions()) {
            int questionId = questionIdMap.get(q);

            System.out.println("Selected answers for question '" + q.getQuestionText() + "': " + q.getSelectedAnswers());

            for (String selectedAnswer : q.getSelectedAnswers()) {
                userAnswerStmt.setInt(1, surveyHistoryId);
                userAnswerStmt.setInt(2, questionId);
                userAnswerStmt.setString(3, selectedAnswer);
                userAnswerStmt.addBatch();
            }
        }

        userAnswerStmt.executeBatch();

        conn.commit();
        System.out.println("COMMIT SUCCESSFUL");

    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
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




public void loadSurveyById(int surveyId) {
    try (Connection conn = DataBaseConnect.connect()) {
        // Отримати назву і опис
        String surveyQuery = "SELECT title, description FROM surveys WHERE id = ?";
        PreparedStatement surveyStmt = conn.prepareStatement(surveyQuery);
        surveyStmt.setInt(1, surveyId);
        ResultSet surveyRs = surveyStmt.executeQuery();

        if (surveyRs.next()) {
            String title = surveyRs.getString("title");
            String description = surveyRs.getString("description");
            setSurveyInfo(title, description);
        }

        // Отримати питання
        String questionQuery = "SELECT id, text FROM questions WHERE survey_id = ?";
        PreparedStatement questionStmt = conn.prepareStatement(questionQuery);
        questionStmt.setInt(1, surveyId);
        ResultSet questionRs = questionStmt.executeQuery();

        List<SurveyQuestion> questions = new java.util.ArrayList<>();

        while (questionRs.next()) {
            int questionId = questionRs.getInt("id");
            String questionText = questionRs.getString("text");

            // Витягуємо відповіді
            String answerQuery = "SELECT answer, is_custom_allowed FROM answers WHERE question_id = ?";
            PreparedStatement answerStmt = conn.prepareStatement(answerQuery);
            answerStmt.setInt(1, questionId);
            ResultSet answerRs = answerStmt.executeQuery();

            List<String> answers = new java.util.ArrayList<>();
            boolean hasCustom = false;

            while (answerRs.next()) {
                String ans = answerRs.getString("answer");
                boolean isCustom = answerRs.getBoolean("is_custom_allowed");

                if (isCustom) {
                    hasCustom = true;
                } else {
                    answers.add(ans);
                }
            }

            // Визначаємо тип питання
            String type;
            if (answers.size() == 1) {
                type = "Один варіант";
            } else if (answers.size() > 1) {
                type = "Декілька варіантів";
            } else {
                type = "Власна відповідь";
            }

            SurveyQuestion question = new SurveyQuestion(questionText, type, answers, hasCustom);
            questions.add(question);
        }

        setQuestions(questions);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


}

