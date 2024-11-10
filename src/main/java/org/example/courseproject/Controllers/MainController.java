package org.example.courseproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;
import org.example.courseproject.POJO.User;

import java.io.IOException;

public class MainController {
    @FXML
    private Label contentLabel;
    @FXML
    private VBox mainContent;

    public User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleHome() {
        contentLabel.setText("Добро пожаловать!");
        VBox homeContent = new VBox();
        homeContent.setAlignment(Pos.CENTER);
        homeContent.setSpacing(5);

        Label welcomeLabel = new Label("Добро пожаловать!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label description1 = new Label("Это приложение призвано помочь построить сбалансированный рацион");
        description1.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label description2 = new Label("на основе мониторинга состава продуктов питания.");
        description2.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label healthStatement = new Label("Правильное питание — ключ к здоровью и профилактике заболеваний.");
        healthStatement.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label deficiencyStatement = new Label("Недостаток питательных веществ может привести к проблемам со здоровьем.");
        deficiencyStatement.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label projectInfo = new Label("Разработано в рамках курсового проекта 5 семестра на Java.");
        projectInfo.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label contactStatement = new Label("В случае возникновения проблем, напишите нам:");
        contactStatement.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");

        Label contactEmail = new Label("shimchyonokat@gmail.com");
        contactEmail.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-alignment: center;");

        homeContent.getChildren().addAll(welcomeLabel, description1, description2, healthStatement, deficiencyStatement, projectInfo, contactStatement, contactEmail);
        mainContent.getChildren().setAll(homeContent);
    }

    @FXML
    private void handleProfile() {
        contentLabel.setText("Личный кабинет");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Node profileContent = loader.load();
            ProfileController profileController = loader.getController();
            profileController.init(this);
            mainContent.getChildren().setAll(profileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDailyIntake() {
        contentLabel.setText("Мои суточные нормы");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/daily_intake.fxml"));
            Node dailyIntakeContent = loader.load();
            DailyIntakeController dailyIntakeController = loader.getController();
            dailyIntakeController.setUser(user);
            mainContent.getChildren().setAll(dailyIntakeContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenu() {
        contentLabel.setText("Меню");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
            Node menuContent = loader.load();
            MenuController menuController = loader.getController();
            mainContent.getChildren().setAll(menuContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlan() {
        contentLabel.setText("План питания");
        // Подобная логика для отображения соответствующего содержимого
    }

    @FXML
    private void handleStats() {
        contentLabel.setText("Моя статистика");
        // Подобная логика для отображения соответствующего содержимого
    }

    @FXML
    private void handleUsers() {
        contentLabel.setText("Пользователи");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/users.fxml"));
            Node usersContent = loader.load();
            UsersController usersController = loader.getController();
            usersController.setCurrentEmail(user.getEmail());
            mainContent.getChildren().setAll(usersContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProducts() {
        contentLabel.setText("Управление продуктами");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/products.fxml"));
            Node productsContent = loader.load();
            ProductsController productsController = loader.getController();
            mainContent.getChildren().setAll(productsContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCategories() {
        contentLabel.setText("Продуктовые категории");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categories.fxml"));
            Node categoriesContent = loader.load();
            CategoriesController catsController = loader.getController();
            mainContent.getChildren().setAll(categoriesContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) mainContent.getScene().getWindow();
            ClientApp app = new ClientApp();
            app.showStartView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
