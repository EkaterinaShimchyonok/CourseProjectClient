package org.example.courseproject.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

public class LoginController {
    @FXML
    private Label responseLabel;
    @FXML
    private TextField emailInput;
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
                    final String response = serverResponse;
                    Platform.runLater(() -> responseLabel.setText(response));
                }
            } catch (IOException e) {
                System.err.println("Не удалось получить ответ от сервера: " + e.getMessage());
            }
        }).start();
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

        if (!isValidEmail(email)) {
            responseLabel.setText("Некорректный email");
            return;
        }

        if (!isValidPassword(password)) {
            responseLabel.setText("Пароль должен быть минимум 8 символов, содержать только латинские буквы, цифры и _");
            return;
        }

        out.println("login;" + email + ";" + hashPassword(password));
        // Очистка формы после нажатия на кнопку регистрации
        emailInput.clear();
        passwordInput.clear();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^[a-zA-Z0-9_]{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        return passwordPattern.matcher(password).matches();
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
