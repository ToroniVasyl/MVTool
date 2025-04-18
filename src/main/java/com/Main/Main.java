package com.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Pane root = loader.load();

            MainController controller = loader.getController();

            Scene scene = new Scene(root, 1366, 768);
            primaryStage.setTitle("MVTool");

            InputStream iconStream = getClass().getResourceAsStream("/icons/icon64.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            } else {
                System.err.println("⚠️ Іконку не знайдено: /icons/icon64.png");
            }

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
