package org.example.courseproject.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;

public class StartController {
    @FXML
    private void handleLogin() {
        Stage stage = (Stage) ClientApp.getPrimaryStage();
        ClientApp app = new ClientApp();
        app.login(stage);
    }

    @FXML
    private void handleRegister() {
        Stage stage = ClientApp.getPrimaryStage();
        ClientApp app = new ClientApp();
        app.register(stage);
    }

    @FXML
    private void handleExit() {
            Platform.exit();
            System.exit(0);
    }

}
