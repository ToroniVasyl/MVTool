package com.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button exitButton; 

    @FXML
    private Button login1Button;

    @FXML
    private ImageView stories;

    @FXML
    private ImageView plus;

    @FXML
    private ImageView home;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        exitButton.setOnAction(this::handleExit);
        login1Button.setOnAction(this::handleLoginClick);
        registerLink.setOnAction(this::handleRegisterClick);

        home.setOnMouseClicked(this::handleHomeClick);
        plus.setOnMouseClicked(this::handlePlusClick);
        stories.setOnMouseClicked(this::handleStoriesClick);

        Tooltip.install(home, new Tooltip("Домашня сторінка"));
        Tooltip.install(plus, new Tooltip("Створити нове"));
        Tooltip.install(stories, new Tooltip("Історія опитувань"));

        login1Button.setOnMouseEntered(e -> login1Button.setStyle(
            "-fx-background-color: #41485b;" +
            "-fx-text-fill: white;"
        ));

        login1Button.setOnMouseExited(e -> login1Button.setStyle(
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
    String username = usernameField.getText();
    String password = passwordField.getText();

    Integer userId = AuthService.login(username, password);

    if (userId != null) {
        Session.setCurrentUserId(userId);
        switchScene("/main.fxml");
        System.out.println("Успішний вхід. user_id = " + userId);
    } else {
        System.out.println("Невірний логін або пароль");
    }
}


    private void handleRegisterClick(ActionEvent event) {
        switchScene("/register.fxml");
    }

    private void handleHomeClick(MouseEvent event) {
        switchScene("/main.fxml");
    }

    private void handlePlusClick(MouseEvent event) {
        switchScene("/newsurvey.fxml");
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
}
