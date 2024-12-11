package org.example.courseproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.example.courseproject.Controllers.LoginController;

public class ClientApp extends Application {
    public static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public void register(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            primaryStage.setTitle("Регистрация");
            primaryStage.setScene(new Scene(root, 500, 275));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void login(Stage primaryStage)
    {
        try {
            LoginController.setPrimaryStage(primaryStage);
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            primaryStage.setTitle("Вход");
            primaryStage.setScene(new Scene(root, 500, 275));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showStartView(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
            primaryStage.setTitle("Главная");
            primaryStage.setScene(new Scene(root, 500, 275)); // Увеличен размер окна
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