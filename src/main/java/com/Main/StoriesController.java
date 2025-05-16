package com.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        String sql = "SELECT id, title, description FROM surveys WHERE user_id = ? ORDER BY id DESC LIMIT 10";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String title = rs.getString("title");
            String description = rs.getString("description");

            VBox surveyBox = new VBox(5);
            surveyBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9;");
            surveyBox.getChildren().addAll(
                new javafx.scene.control.Label("Назва: " + title),
                new javafx.scene.control.Label("Опис: " + description)
            );

            surveyList.getChildren().add(surveyBox);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


}
