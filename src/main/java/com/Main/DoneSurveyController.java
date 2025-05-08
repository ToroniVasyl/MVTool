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
import java.util.List;

public class DoneSurveyController {

    @FXML 
    private Button exitButton;

    @FXML 
    private Button loginButton;

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
    public void initialize() {
        exitButton.setOnAction(this::handleExit);
        plus.setOnMouseClicked(this::handlePlusClick);
        home.setOnMouseClicked(this::handleHomeClick);
        stories.setOnMouseClicked(this::handleStoriesClick);
        loginButton.setOnAction(this::handleLoginClick);

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
}
