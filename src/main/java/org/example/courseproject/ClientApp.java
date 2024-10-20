package org.example.courseproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



public class ClientApp extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    void register(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            primaryStage.setTitle("Registration");
            primaryStage.setScene(new Scene(root, 300, 200));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void login(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, 300, 200));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void showStartView(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
            primaryStage.setTitle("Главная");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        ClientApp.primaryStage = primaryStage;
        showStartView(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}