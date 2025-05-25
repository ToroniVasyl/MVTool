package com.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class RegisterController {

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
    private Hyperlink loginLink;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        exitButton.setOnAction(this::handleExit);
        loginLink.setOnAction(this::handleLoginClick);
        registerButton.setOnAction(this::handleRegisterClick);
        plus.setOnMouseClicked(this::handlePlusClick);
        home.setOnMouseClicked(this::handleHomeClick);
        stories.setOnMouseClicked(this::handleStoriesClick);

        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
            "-fx-background-color: #41485b;" +
            "-fx-text-fill: white;"
        ));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgb(255, 255, 255), rgb(156, 156, 156));" +
            "-fx-text-fill: #41485b;"
        ));

        exitButton.setOnMouseEntered(e -> exitButton.setStyle(
            "-fx-background-color:rgb(80, 12, 12);" +
            "-fx-text-fill: white;"
        ));

        exitButton.setOnMouseExited(e -> exitButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgb(161, 37, 37), rgb(92, 17, 17));" +
            "-fx-text-fill: white;"
        ));

        home.setOnMouseEntered(e -> {
            home.setScaleX(1.2);
            home.setScaleY(1.2);
        });
        home.setOnMouseExited(e -> {
            home.setScaleX(1.0);
            home.setScaleY(1.0);
        });

        plus.setOnMouseEntered(e -> {
            plus.setScaleX(1.2);
            plus.setScaleY(1.2);
        });
        plus.setOnMouseExited(e -> {
            plus.setScaleX(1.0);
            plus.setScaleY(1.0);
        });

        stories.setOnMouseEntered(e -> {
            stories.setScaleX(1.2);
            stories.setScaleY(1.2);
        });
        stories.setOnMouseExited(e -> {
            stories.setScaleX(1.0);
            stories.setScaleY(1.0);
        });
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

    @FXML
    private void handleRegisterClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
    
        if (AuthService.register(username, password)) {
            System.out.println("Реєстрація успішна");
            switchScene("/login.fxml");
        } else {
            System.out.println("Помилка під час реєстрації");
        }
    }

    private void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
