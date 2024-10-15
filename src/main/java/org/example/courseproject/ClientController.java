package org.example.courseproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

public class ClientController {
    @FXML
    private Label responseLabel;
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;

    private PrintWriter out;

    public void initialize() {
        connectToServer();
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

    // Изменение: Метод для валидации email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    // Изменение: Метод для валидации пароля
    private boolean isValidPassword(String password) {
        String passwordRegex = "^[a-zA-Z0-9_]{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        return passwordPattern.matcher(password).matches();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Новый поток для чтения ответов от сервера
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        final String response = serverResponse;
                        Platform.runLater(() -> responseLabel.setText(response));
                    }
                } catch (IOException e) {
                    System.err.println("Не удалось получить ответ от сервера: " + e.getMessage());
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        System.err.println("Не удалось закрыть соединение: " + e.getMessage());
                    }
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegisterAction() {
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
        out.println("register;" + email + ";" + hashPassword(password));
        // Очистка формы после нажатия на кнопку регистрации
        emailInput.clear();
        passwordInput.clear();
    }
}