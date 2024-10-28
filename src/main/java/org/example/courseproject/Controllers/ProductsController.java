package org.example.courseproject.Controllers;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.courseproject.GsonUtils;
import org.example.courseproject.POJO.Product;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {

    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Boolean> isCookedColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Double> caloriesColumn;
    @FXML
    private TableColumn<Product, Double> proteinsColumn;
    @FXML
    private TableColumn<Product, Double> fatsColumn;
    @FXML
    private TableColumn<Product, Double> carbsColumn;
    @FXML
    private TableColumn<Product, Double> aColumn;
    @FXML
    private TableColumn<Product, Double> cColumn;
    @FXML
    private TableColumn<Product, Double> dColumn;
    @FXML
    private TableColumn<Product, Double> eColumn;
    @FXML
    private TableColumn<Product, Double> kColumn;
    @FXML
    private TableColumn<Product, Double> b12Column;
    @FXML
    private TableColumn<Product, Double> caColumn;
    @FXML
    private TableColumn<Product, Double> feColumn;
    @FXML
    private TableColumn<Product, Double> mgColumn;
    @FXML
    private TableColumn<Product, Double> znColumn;
    @FXML
    private TableColumn<Product, Double> cuColumn;
    @FXML
    private TableColumn<Product, Double> seColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Button addProductButton;
    @FXML
    private Button editProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Button searchProductButton;


    private PrintWriter out;
    private BufferedReader in;

    private ObservableList<Product> productsList;

    @FXML
    public void initialize() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();
        productsList = FXCollections.observableArrayList();

        // Открытие модального диалогового окна для выбора категории
        Platform.runLater(() -> showCategorySelectionDialog());

        // Инициализация столбцов таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        isCookedColumn.setCellValueFactory(new PropertyValueFactory<>("isCooked"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        caloriesColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMacroNutrients().caloriesProperty().asObject());
        proteinsColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMacroNutrients().proteinsProperty().asObject());
        fatsColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMacroNutrients().fatsProperty().asObject());
        carbsColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMacroNutrients().carbsProperty().asObject());
        aColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().aProperty().asObject());
        cColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().cProperty().asObject());
        dColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().dProperty().asObject());
        eColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().eProperty().asObject());
        kColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().kProperty().asObject());
        b12Column.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getVitamins().b12Property().asObject());
        caColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().caProperty().asObject());
        feColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().feProperty().asObject());
        mgColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().mgProperty().asObject());
        znColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().znProperty().asObject());
        cuColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().cuProperty().asObject());
        seColumn.setCellValueFactory(cellData -> cellData.getValue().getNutrients().getMinerals().seProperty().asObject());

        productsTable.setItems(productsList);
    }

    private void showCategorySelectionDialog() {
        List<String> categories = new ArrayList<>();

        // Запрос на получение категорий с сервера
        out.println("categoryfetchnames");

        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null && !response.equals("end")) {
                    categories.add(response);
                }

                // Добавляем строку "Все" в выпадающий список
                categories.add(0, "Все");

                // Обновляем UI после получения категорий
                Platform.runLater(() -> {
                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Выбор категории");
                    dialog.setHeaderText("Выберите категорию");

                    ComboBox<String> categoryComboBox = new ComboBox<>();
                    categoryComboBox.setMinWidth(200);
                    categoryComboBox.setItems(FXCollections.observableArrayList(categories));

                    ButtonType selectButtonType = new ButtonType("Выбрать", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
                    dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, cancelButtonType);


                    GridPane grid = new GridPane();
                    grid.add(new Label("Категория:"), 0, 0);
                    grid.add(categoryComboBox, 1, 0);
                    dialog.getDialogPane().setContent(grid);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == selectButtonType) {
                            return categoryComboBox.getSelectionModel().getSelectedItem();
                        }
                        return null;
                    });

                    dialog.showAndWait().ifPresent(category -> {
                        if (category != null) {
                            handleFetchProductsByCategory(category);
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @FXML
    private void handleFetchProductsByCategory(String category) {
        try {
            out.println("productfetchcat;" + category);
            String response;
            productsList.clear();
            while ((response = in.readLine()) != null && !response.equals("end")) {
                Product product = parseProduct(response);
                product.setCategoryName(product.getCategory().getName());
                productsList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Product parseProduct(String serverResponse) {
        Gson gson = GsonUtils.getGson();
        Product product = gson.fromJson(serverResponse, Product.class);
        System.out.println(serverResponse);
        return product;
    }


    @FXML
    private void handleAddProduct() {
        // Логика для добавления продукта
    }

    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Открываем диалоговое окно для редактирования выбранного продукта
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productsList.remove(selectedProduct);
            // Дополнительная логика для удаления продукта из базы данных
        }
    }

    @FXML
    private void handleSearchProduct() {
        String searchText = searchField.getText();
        // Логика для поиска продукта по названию
    }

}
