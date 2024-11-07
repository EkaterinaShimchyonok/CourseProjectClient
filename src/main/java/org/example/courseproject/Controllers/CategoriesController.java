package org.example.courseproject.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import org.example.courseproject.POJO.Category;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Optional;

public class CategoriesController {

    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, Integer> idColumn;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> imageColumn;
    @FXML
    private TextField searchField;
    private PrintWriter out;
    private BufferedReader in;
    private ObservableList<Category> categoriesList;

    @FXML
    public void initialize() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();
        categoriesList = FXCollections.observableArrayList();

        // Инициализация столбцов таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        categoriesTable.setItems(categoriesList);
        categoriesTable.setEditable(true);
        setEditableColumns();
        handleFetchCategories();
    }

    private void setEditableColumns() {
        // Настройка редактируемого столбца для названия категории
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setName(event.getNewValue());
            handleEditCategory(category);
        });

        // Настройка редактируемого столбца для URL изображения
        imageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        imageColumn.setOnEditCommit(event -> {
            Category category = event.getRowValue();
            category.setImage(event.getNewValue());
            handleEditCategory(category);
        });
    }

    private void handleEditCategory(Category category) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String categoryJson = mapper.writeValueAsString(category);
            System.out.println(categoryJson);
            out.println("categoryupdate;" + categoryJson);

            // Ожидаем ответ от сервера
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Изменение категории");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();

                        // Обновляем таблицу, чтобы отобразить изменения
                        handleFetchCategories();
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
    private void handleFetchCategories() {
        try {
            out.println("categoryfetchall");
            String response;

            categoriesList.clear();

            while (!(response = in.readLine()).equals("end")) {
                Category category = parseCategory(response);
                if (category != null) {
                    categoriesList.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Category parseCategory(String serverResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(serverResponse, Category.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Category showAddCategoryDialog() {
        Dialog<Category> dialog = new Dialog<>();
        dialog.setTitle("Добавление новой категории");
        dialog.setHeaderText("Введите данные новой категории");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Название категории");
        TextField imageField = new TextField();
        imageField.setPromptText("URL картинки");

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("URL картинки:"), 0, 1);
        grid.add(imageField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    String image = imageField.getText();
                    return new Category(0, name, image);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Произошла ошибка: " + e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        Optional<Category> result = dialog.showAndWait();
        return result.orElse(null);
    }

    @FXML
    private void handleAddCategory() {
        Category newCategory = showAddCategoryDialog();
        if (newCategory != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.registerModule(new Jdk8Module());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                String categoryJson = mapper.writeValueAsString(newCategory);
                System.out.println(categoryJson);

                out.println("categoryadd;" + categoryJson);

                new Thread(() -> {
                    try {
                        String response = in.readLine();
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Добавление категории");
                            alert.setHeaderText(null);
                            alert.setContentText(response);
                            alert.showAndWait();

                            handleFetchCategories();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoriesTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            int categoryId = selectedCategory.getCategoryID();
            categoriesList.remove(selectedCategory);
            out.println("categorydelete;" + categoryId);

            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Удаление категории");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();
                        categoriesList.remove(selectedCategory);
                        handleFetchCategories();

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FXML
    private void handleSearchCategory() {
        String searchText = searchField.getText().toLowerCase();
        boolean found = false;
        for (Category category : categoriesList) {
            if (category.getName().toLowerCase().contains(searchText)) {
                categoriesTable.getSelectionModel().select(category);
                categoriesTable.scrollTo(category);
                found = true;
                break;
            }
        }
        if (!found) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Категория не найдена");
            alert.setHeaderText(null);
            alert.setContentText("Категория с названием \"" + searchText + "\" не найдена.");
            alert.showAndWait();
        }
    }
}
