package org.example.courseproject.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.courseproject.POJO.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class UsersController {

    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Boolean> isAdminColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button addUserButton;
    @FXML
    private Button editUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button searchUserButton;

    private PrintWriter out;
    private BufferedReader in;

    private ObservableList<User> usersList;

    @FXML
    public void initialize() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();
        usersList = FXCollections.observableArrayList();

        // Инициализация столбцов таблицы
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        usersTable.setItems(usersList);
        handleFetchAllUsers();
    }

    @FXML
    private void handleAddUser() {
        // Логика для добавления пользователя
    }

    @FXML
    private void handleEditUser() {
        // Логика для редактирования пользователя
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Открываем диалоговое окно для редактирования выбранного пользователя
        }
    }

    @FXML
    private void handleDeleteUser() {
        // Логика для удаления пользователя
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            usersList.remove(selectedUser);
            // Дополнительная логика для удаления пользователя из базы данных
        }
    }

    @FXML
    private void handleSearchUser() {
        String searchText = searchField.getText();
        // Логика для поиска пользователя
    }

    @FXML
    private void handleFetchAllUsers() {
        new Thread(() -> {
            out.println("userfetchall");
            String response;
            usersList.clear();
            while (true) {
                try {
                    response = in.readLine();
                    System.out.println(response);
                    String[] parts = response.split(";");
                    User user = new User();
                    user.setEmail(parts[0]);
                    user.setAdmin(Boolean.parseBoolean(parts[1]));
                    user.setUserName(parts[2]);
                    usersList.add(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
