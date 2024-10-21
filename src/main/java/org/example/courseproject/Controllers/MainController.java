package org.example.courseproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;
import org.example.courseproject.POJO.User;

public class MainController {
    @FXML
    private Label contentLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private VBox profileForm;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleProfile() {
        contentLabel.setText("Личный кабинет");
        emailField.setText(user.getEmail());
        nameField.setText(user.getInfo().getName());
        ageField.setText(String.valueOf(user.getInfo().getAge()));
    }

    @FXML
    private void handleApplyChanges() {
        user.getInfo().setName(nameField.getText());
        user.getInfo().setAge(Integer.parseInt(ageField.getText()));
    }

    @FXML
    private void handlePlan() {
        contentLabel.setText("План питания");
    }

    @FXML
    private void handleStats() {
        contentLabel.setText("Моя статистика");
    }

    @FXML
    private void handleUsers() {
        contentLabel.setText("Пользователи");
    }

    @FXML
    private void handleProducts() {
        contentLabel.setText("Управление продуктами");
    }

    @FXML
    private void handleLogout() {
        // Переключение на страницу входа
        Stage stage = (Stage) contentLabel.getScene().getWindow();
        ClientApp app = new ClientApp();
        app.showStartView(stage);
    }
}
