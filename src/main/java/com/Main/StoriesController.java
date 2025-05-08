package com.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
    public void initialize() {

        exitButton.setOnAction(this::handleExit);
    }

    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
