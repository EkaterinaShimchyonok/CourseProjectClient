package org.example.courseproject.Controllers;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;
import org.example.courseproject.GsonUtils;
import org.example.courseproject.POJO.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController {
    private static Stage primaryStage;
    @FXML
    private TextField emailInput;
    @FXML
    private Label responseLabel;
    @FXML
    private PasswordField passwordInput;

    private PrintWriter out;
    private BufferedReader in;

    @FXML
    public void initialize() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();

        new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    if (serverResponse.equals("Не удалось войти в систему. Попробуйте ещё раз"))
                    {
                        final String response = serverResponse;
                        Platform.runLater(() -> responseLabel.setText(response));
                    }
                    else
                    {
                        final User user = parseUser(serverResponse);
                        Platform.runLater(() -> showMainPage(user));
                    }
                }
            } catch (IOException e) {
                System.err.println("Не удалось получить ответ от сервера: " + e.getMessage());
            }
        }).start();
    }

    private User parseUser(String serverResponse) {
        Gson gson = GsonUtils.getGson();
        User user = gson.fromJson(serverResponse, User.class);
        System.out.println(serverResponse);
        return user;
    }

    private void showMainPage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            MainController mainController = loader.getController();
            mainController.setUser(user); // Передаем объект User контроллеру новой страницы
            String title = "Здравствуйте ";
            if(user.getInfo().getName()!=null){
                title += user.getInfo().getName();}
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) ClientApp.getPrimaryStage();
        ClientApp app = new ClientApp();
        app.showStartView(stage);
    }

    @FXML
    private void handleLogin() {
        String email = emailInput.getText();
        String password = passwordInput.getText();


        out.println("login;" + email + ";" + RegisterController.hashPassword(password));

        // Очистка формы после нажатия на кнопку регистрации
        emailInput.clear();
        passwordInput.clear();
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
}
