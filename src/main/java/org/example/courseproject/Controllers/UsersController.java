package org.example.courseproject.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.courseproject.ClientApp;
import org.example.courseproject.POJO.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class UsersController {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Boolean> isAdminColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TextField searchField;

    private PrintWriter out;
    private BufferedReader in;

    private ObservableList<User> usersList;

    String currentEmail;

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail;
    }

    @FXML
    public void initialize() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();
        usersList = FXCollections.observableArrayList();

        // Инициализация столбцов таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        usersTable.setItems(usersList);
        usersTable.setEditable(true);
        setEditableColumns();
        handleFetchAllUsers();
    }

    private void setEditableColumns() {
        // Настройка редактируемого столбца для admin
        isAdminColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(true, false)));
        isAdminColumn.setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setAdmin(event.getNewValue());
            handleEditUser(user);
        });
    }

    private void handleEditUser(User user) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String userJson = mapper.writeValueAsString(user);
            System.out.println(userJson);
            out.println("user;updateadmin;" + userJson);

            // Ожидаем ответ от сервера
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Изменение пользователя");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();

                        handleFetchAllUsers();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFetchAllUsers() {
        try {
            out.println("user;fetchall;.");
            String response;

            usersList.clear();

            while (!(response = in.readLine()).equals("end")) {
                User user = parseUser(response);
                user.setUserName(user.getInfo().getName());
                if (user != null) {
                    usersList.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User parseUser(String serverResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(serverResponse, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @FXML
    private void handleAddUser() {
        try {
            // Загружаем FXML файл для окна регистрации
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/newuser.fxml"));
            Parent root = loader.load();
            // Создаем новое окно (Stage) и задаем его параметры
            Stage stage = new Stage();
            stage.setTitle("Регистрация нового пользователя");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !currentEmail.equals(selectedUser.getEmail())) {
            out.println("user;delete;" +  selectedUser.getUserID());
            usersList.remove(selectedUser);
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Удаление пользователя");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();
                        handleFetchAllUsers();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FXML
    private void handleSearchUser() {
        String searchText = searchField.getText().toLowerCase();
        boolean found = false;
        for (User user : usersList) {
            if (user.getUserName().toLowerCase().contains(searchText) || user.getEmail().toLowerCase().contains(searchText)) {
                usersTable.getSelectionModel().select(user);
                usersTable.scrollTo(user);
                found = true;
                break;
            }
        }
        if (!found) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Пользователь не найден");
            alert.setHeaderText(null);
            alert.setContentText("Пользователь с данными \"" + searchText + "\" не найден.");
            alert.showAndWait();
        }
    }
}
