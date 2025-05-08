package com.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewSurveyController {

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
    private Button newquestions;

    @FXML 
    private Button doneButton;

    @FXML 
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    public void initialize() {
        exitButton.setOnAction(this::handleExit);
        plus.setOnMouseClicked(this::handlePlusClick);
        home.setOnMouseClicked(this::handleHomeClick);
        stories.setOnMouseClicked(this::handleStoriesClick);
        loginButton.setOnAction(this::handleLoginClick);
        newquestions.setOnAction(this::handleAddQuestion);
        doneButton.setOnAction(this::handleDoneClick);

        Tooltip.install(home, new Tooltip("Домашня сторінка"));
        Tooltip.install(plus, new Tooltip("Створити нове"));
        Tooltip.install(stories, new Tooltip("Історія опитувань"));

        SurveyDataStore store = SurveyDataStore.getInstance();
        if (store.getTitle() != null) titleField.setText(store.getTitle());
        if (store.getDescription() != null) descriptionField.setText(store.getDescription());
        if (store.getQuestions() != null && !store.getQuestions().isEmpty()) {
            for (SurveyQuestion q : store.getQuestions()) {
                restoreQuestion(q);
            }
        }
    }

    private void handleExit(ActionEvent event) {
        ((Stage) exitButton.getScene().getWindow()).close();
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

    private void handleDoneClick(ActionEvent event) {
        SurveyDataStore store = SurveyDataStore.getInstance();
        store.setTitle(titleField.getText());
        store.setDescription(descriptionField.getText());
        store.setQuestions(collectQuestions());

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

    private List<SurveyQuestion> collectQuestions() {
        List<SurveyQuestion> questions = new ArrayList<>();

        for (javafx.scene.Node node : questionContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox questionPane = (VBox) node;

                HBox topRow = (HBox) questionPane.getChildren().get(0);
                TextField questionField = (TextField) topRow.getChildren().get(0);
                ComboBox<String> methodBox = (ComboBox<String>) topRow.getChildren().get(1);

                VBox optionsBox = (VBox) questionPane.getChildren().get(2);
                String type = methodBox.getValue();

                if (type == null || questionField.getText().isBlank()) continue;

                List<String> options = new ArrayList<>();

                if (!type.equals("Власна відповідь")) {
                    for (javafx.scene.Node optNode : optionsBox.getChildren()) {
                        if (optNode instanceof HBox) {
                            HBox optBox = (HBox) optNode;
                            if (optBox.getChildren().size() >= 2) {
                                TextField optField = (TextField) optBox.getChildren().get(1);
                                if (!optField.getText().isBlank()) {
                                    options.add(optField.getText());
                                }
                            }
                        }
                    }
                }

                questions.add(new SurveyQuestion(questionField.getText(), type, options));
            }
        }

        return questions;
    }

    private void handleAddQuestion(ActionEvent event) {
        restoreQuestion(new SurveyQuestion("", null, new ArrayList<>()));
    }

    private void restoreQuestion(SurveyQuestion q) {
        VBox questionPane = new VBox(10);
        questionPane.setPadding(new Insets(10));
        questionPane.setStyle("-fx-background-color: #D1D3D8; -fx-background-radius: 5;");
        questionPane.setPrefWidth(452);

        TextField questionField = new TextField(q.getQuestionText());
        questionField.setPromptText("Запитання");
        questionField.setFont(javafx.scene.text.Font.font(15));
        questionField.setPrefWidth(250);

        ComboBox<String> methodBox = new ComboBox<>();
        methodBox.setPrefWidth(150);
        methodBox.setPrefHeight(30);
        methodBox.setStyle("-fx-background-color: white;");
        methodBox.getItems().addAll("Один варіант", "Декілька варіантів", "Власна відповідь");
        if (q.getType() != null) methodBox.setValue(q.getType());
        else methodBox.setPromptText("Оберіть");

        HBox topRow = new HBox(10, questionField, methodBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Button deleteQuestionBtn = new Button("Видалити запитання");
        deleteQuestionBtn.setFont(javafx.scene.text.Font.font(13));
        deleteQuestionBtn.setCursor(Cursor.HAND);
        deleteQuestionBtn.setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
        deleteQuestionBtn.setOnAction(e -> questionContainer.getChildren().remove(questionPane));

        VBox optionsBox = new VBox(10);

        Button addOptionBtn = new Button("Додати варіант");
        addOptionBtn.setFont(javafx.scene.text.Font.font(15));
        addOptionBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #41485b;");
        addOptionBtn.setCursor(Cursor.HAND);
        addOptionBtn.setAlignment(Pos.CENTER_LEFT);

        addOptionBtn.setOnAction(e -> {
            String selectedType = methodBox.getValue();
            if (selectedType == null) return;

            TextField optionField = new TextField();
            optionField.setPromptText("Назва варіанту");
            optionField.setFont(javafx.scene.text.Font.font(15));

            HBox optionBox = new HBox(10);
            optionBox.setAlignment(Pos.CENTER_LEFT);

            Button deleteOptionBtn = new Button("✖");
            deleteOptionBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            deleteOptionBtn.setCursor(Cursor.HAND);
            deleteOptionBtn.setOnAction(ev -> optionsBox.getChildren().remove(optionBox));

            if ("Один варіант".equals(selectedType)) {
                RadioButton radio = new RadioButton();
                radio.setDisable(true);
                optionBox.getChildren().addAll(radio, optionField, deleteOptionBtn);
            } else if ("Декілька варіантів".equals(selectedType)) {
                CheckBox check = new CheckBox();
                check.setDisable(true);
                optionBox.getChildren().addAll(check, optionField, deleteOptionBtn);
            }

            optionsBox.getChildren().add(optionsBox.getChildren().size() - 1, optionBox);
        });

        methodBox.setOnAction(e -> {
            String selected = methodBox.getValue();
            optionsBox.getChildren().clear();

            if ("Власна відповідь".equals(selected)) {
                TextField userAnswer = new TextField();
                userAnswer.setPromptText("Тут користувач введе свою відповідь");
                userAnswer.setFont(javafx.scene.text.Font.font(15));
                userAnswer.setDisable(true);
                optionsBox.getChildren().add(userAnswer);
            } else {
                optionsBox.getChildren().add(addOptionBtn);
            }
        });

        if (q.getType() != null) {
            if ("Власна відповідь".equals(q.getType())) {
                TextField userAnswer = new TextField();
                userAnswer.setPromptText("Тут користувач введе свою відповідь");
                userAnswer.setFont(javafx.scene.text.Font.font(15));
                userAnswer.setDisable(true);
                optionsBox.getChildren().add(userAnswer);
            } else {
                for (String option : q.getOptions()) {
                    TextField optionField = new TextField(option);
                    optionField.setFont(javafx.scene.text.Font.font(15));

                    HBox optionBox = new HBox(10);
                    optionBox.setAlignment(Pos.CENTER_LEFT);

                    Button deleteOptionBtn = new Button("✖");
                    deleteOptionBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
                    deleteOptionBtn.setCursor(Cursor.HAND);
                    deleteOptionBtn.setOnAction(ev -> optionsBox.getChildren().remove(optionBox));

                    if ("Один варіант".equals(q.getType())) {
                        RadioButton radio = new RadioButton();
                        radio.setDisable(true);
                        optionBox.getChildren().addAll(radio, optionField, deleteOptionBtn);
                    } else if ("Декілька варіантів".equals(q.getType())) {
                        CheckBox check = new CheckBox();
                        check.setDisable(true);
                        optionBox.getChildren().addAll(check, optionField, deleteOptionBtn);
                    }

                    optionsBox.getChildren().add(optionBox);
                }
                optionsBox.getChildren().add(addOptionBtn);
            }
        } else {
            optionsBox.getChildren().add(addOptionBtn);
        }

        questionPane.getChildren().addAll(topRow, deleteQuestionBtn, optionsBox);
        questionContainer.getChildren().add(questionPane);
    }
}