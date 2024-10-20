package org.example.courseproject;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class StartController {

    @FXML
    private void handleLogin() {
        Stage stage = (Stage) ClientApp.getPrimaryStage();
        ClientApp app = new ClientApp();
        app.login(stage);
    }

    @FXML
    private void handleRegister() {
        Stage stage = (Stage) ClientApp.getPrimaryStage();
        ClientApp app = new ClientApp();
        app.register(stage);
    }
}
