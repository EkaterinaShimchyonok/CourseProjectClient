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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import org.example.courseproject.POJO.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;

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

    private PrintWriter out;
    private BufferedReader in;

    private ObservableList<Product> productsList;

    static private ArrayList<String> categories = new ArrayList<>();

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
        productsTable.setEditable(true);  // Включение редактирования таблицы

        // Настройка редактируемых столбцов
        setEditableColumns();
    }


    private void setEditableColumns() {
        // Настройка редактируемого столбца для названия продукта
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setName(event.getNewValue());
            handleEditProduct(product);
        });

        // Настройка редактируемого столбца для статуса готовности продукта
        isCookedColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(true, false)));
        isCookedColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setIsCooked(Boolean.parseBoolean(event.getNewValue().toString()));
            handleEditProduct(product);
        });

        // Настройка редактируемых столбцов для числовых полей
        setupNumericColumn(caloriesColumn, (product, newValue) -> product.getNutrients().getMacroNutrients().setCalories(newValue));
        setupNumericColumn(proteinsColumn, (product, newValue) -> product.getNutrients().getMacroNutrients().setProteins(newValue));
        setupNumericColumn(fatsColumn, (product, newValue) -> product.getNutrients().getMacroNutrients().setFats(newValue));
        setupNumericColumn(carbsColumn, (product, newValue) -> product.getNutrients().getMacroNutrients().setCarbs(newValue));
        setupNumericColumn(aColumn, (product, newValue) -> product.getNutrients().getVitamins().setA(newValue));
        setupNumericColumn(cColumn, (product, newValue) -> product.getNutrients().getVitamins().setC(newValue));
        setupNumericColumn(dColumn, (product, newValue) -> product.getNutrients().getVitamins().setD(newValue));
        setupNumericColumn(eColumn, (product, newValue) -> product.getNutrients().getVitamins().setE(newValue));
        setupNumericColumn(kColumn, (product, newValue) -> product.getNutrients().getVitamins().setK(newValue));
        setupNumericColumn(b12Column, (product, newValue) -> product.getNutrients().getVitamins().setB12(newValue));
        setupNumericColumn(caColumn, (product, newValue) -> product.getNutrients().getMinerals().setCa(newValue));
        setupNumericColumn(feColumn, (product, newValue) -> product.getNutrients().getMinerals().setFe(newValue));
        setupNumericColumn(mgColumn, (product, newValue) -> product.getNutrients().getMinerals().setMg(newValue));
        setupNumericColumn(znColumn, (product, newValue) -> product.getNutrients().getMinerals().setZn(newValue));
        setupNumericColumn(cuColumn, (product, newValue) -> product.getNutrients().getMinerals().setCu(newValue));
        setupNumericColumn(seColumn, (product, newValue) -> product.getNutrients().getMinerals().setSe(newValue));
    }

    private void setupNumericColumn(TableColumn<Product, Double> column, BiConsumer<Product, Double> setter) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            setter.accept(product, event.getNewValue());
            handleEditProduct(product);
        });
    }


    private void handleEditProduct(Product product) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String productJson = mapper.writeValueAsString(product);
            System.out.println(productJson);
            out.println("product;update;" + productJson);

            // Ожидаем ответ от сервера
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Изменение продукта");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();

                        // Обновляем таблицу, чтобы отобразить изменения
                        handleFetchProductsByCategory(product.getCategoryName());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCategorySelectionDialog() {
        categories.clear();
        categories.add("Все");
        out.println("category;fetchnames;.");

        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null && !response.equals("end")) {
                    categories.add(response);
                }

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
                    grid.add(new Label("Категория "), 0, 0);
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
            out.println("product;fetchcat;" + category);
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

    static public Product parseProduct(String serverResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            Product product = mapper.readValue(serverResponse, Product.class);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Product showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Добавление нового продукта");
        dialog.setHeaderText("Введите данные нового продукта");

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Название продукта");

        ComboBox<String> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(categories.subList(1, categories.size())));
        categoryComboBox.setPromptText("Категория");
        categoryComboBox.getSelectionModel().selectFirst();


        TextField caloriesField = new TextField();
        caloriesField.setPromptText("Калории (ккал)");

        TextField proteinsField = new TextField();
        proteinsField.setPromptText("Белки (г)");

        TextField fatsField = new TextField();
        fatsField.setPromptText("Жиры (г)");

        TextField carbsField = new TextField();
        carbsField.setPromptText("Углеводы (г)");

        TextField aField = new TextField();
        aField.setPromptText("Витамин A (мкг)");

        TextField cField = new TextField();
        cField.setPromptText("Витамин C (мг)");

        TextField dField = new TextField();
        dField.setPromptText("Витамин D (мкг)");

        TextField eField = new TextField();
        eField.setPromptText("Витамин E (мг)");

        TextField kField = new TextField();
        kField.setPromptText("Витамин K (мкг)");

        TextField b12Field = new TextField();
        b12Field.setPromptText("Витамин B12 (мкг)");

        TextField caField = new TextField();
        caField.setPromptText("Кальций (мг)");

        TextField feField = new TextField();
        feField.setPromptText("Железо (мг)");

        TextField mgField = new TextField();
        mgField.setPromptText("Магний (мг)");

        TextField znField = new TextField();
        znField.setPromptText("Цинк (мг)");

        TextField cuField = new TextField();
        cuField.setPromptText("Медь (мг)");

        TextField seField = new TextField();
        seField.setPromptText("Селен (мкг)");

        CheckBox isCookedCheckbox = new CheckBox();
        Label isCookedLabel = new Label("Приготовлен:");

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Категория:"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(new Label("Калории (ккал):"), 0, 2);
        grid.add(caloriesField, 1, 2);
        grid.add(new Label("Белки (г):"), 0, 3);
        grid.add(proteinsField, 1, 3);
        grid.add(new Label("Жиры (г):"), 0, 4);
        grid.add(fatsField, 1, 4);
        grid.add(new Label("Углеводы (г):"), 0, 5);
        grid.add(carbsField, 1, 5);
        grid.add(new Label("Витамин A (мкг):"), 0, 6);
        grid.add(aField, 1, 6);
        grid.add(new Label("Витамин C (мг):"), 0, 7);
        grid.add(cField, 1, 7);
        grid.add(new Label("Витамин D (мкг):"), 0, 8);
        grid.add(dField, 1, 8);
        grid.add(new Label("Витамин E (мг):"), 0, 9);
        grid.add(eField, 1, 9);
        grid.add(new Label("Витамин K (мкг):"), 0, 10);
        grid.add(kField, 1, 10);
        grid.add(new Label("Витамин B12 (мкг):"), 0, 11);
        grid.add(b12Field, 1, 11);
        grid.add(new Label("Кальций (мг):"), 0, 12);
        grid.add(caField, 1, 12);
        grid.add(new Label("Железо (мг):"), 0, 13);
        grid.add(feField, 1, 13);
        grid.add(new Label("Магний (мг):"), 0, 14);
        grid.add(mgField, 1, 14);
        grid.add(new Label("Цинк (мг):"), 0, 15);
        grid.add(znField, 1, 15);
        grid.add(new Label("Медь (мг):"), 0, 16);
        grid.add(cuField, 1, 16);
        grid.add(new Label("Селен (мкг):"), 0, 17);
        grid.add(seField, 1, 17);
        grid.add(isCookedLabel, 0, 18);
        grid.add(isCookedCheckbox, 1, 18);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String name = nameField.getText();
                    String cat_name = categoryComboBox.getSelectionModel().getSelectedItem();
                    double calories = caloriesField.getText().isEmpty() ? 0.0 : parseDouble(caloriesField.getText(), "Калории");
                    double proteins = proteinsField.getText().isEmpty() ? 0.0 : parseDouble(proteinsField.getText(), "Белки");
                    double fats = fatsField.getText().isEmpty() ? 0.0 : parseDouble(fatsField.getText(), "Жиры");
                    double carbs = carbsField.getText().isEmpty() ? 0.0 : parseDouble(carbsField.getText(), "Углеводы");
                    double a = aField.getText().isEmpty() ? 0.0 : parseDouble(aField.getText(), "Витамин A");
                    double c = cField.getText().isEmpty() ? 0.0 : parseDouble(cField.getText(), "Витамин C");
                    double d = dField.getText().isEmpty() ? 0.0 : parseDouble(dField.getText(), "Витамин D");
                    double e = eField.getText().isEmpty() ? 0.0 : parseDouble(eField.getText(), "Витамин E");
                    double k = kField.getText().isEmpty() ? 0.0 : parseDouble(kField.getText(), "Витамин K");
                    double b12 = b12Field.getText().isEmpty() ? 0.0 : parseDouble(b12Field.getText(), "Витамин B12");
                    double ca = caField.getText().isEmpty() ? 0.0 : parseDouble(caField.getText(), "Кальций");
                    double fe = feField.getText().isEmpty() ? 0.0 : parseDouble(feField.getText(), "Железо");
                    double mg = mgField.getText().isEmpty() ? 0.0 : parseDouble(mgField.getText(), "Магний");
                    double zn = znField.getText().isEmpty() ? 0.0 : parseDouble(znField.getText(), "Цинк");
                    double cu = cuField.getText().isEmpty() ? 0.0 : parseDouble(cuField.getText(), "Медь");
                    double se = seField.getText().isEmpty() ? 0.0 : parseDouble(seField.getText(), "Селен");

                    Category category = new Category(cat_name);
                    Vitamins vits = new Vitamins(a, c, d, e, k, b12);
                    Minerals mins = new Minerals(ca, fe, mg, zn, cu, se);
                    MacroNutrients macs = new MacroNutrients(calories, proteins, fats, carbs);
                    Nutrients nutrients = new Nutrients(macs, vits, mins);
                    boolean isCooked = isCookedCheckbox.isSelected();
                    return new Product(name, isCooked, category, nutrients);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Неверное значение в поле: " + e.getMessage());
                    alert.showAndWait();
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private double parseDouble(String value, String fieldName) throws NumberFormatException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName);
        }
    }

    @FXML
    private void handleAddProduct() {
        Product newProduct = showAddProductDialog();
        if (newProduct != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.registerModule(new Jdk8Module());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                // Преобразуем продукт в JSON-строку
                String productJson = mapper.writeValueAsString(newProduct);
                System.out.println(productJson);

                // Отправляем новый продукт на сервер
                out.println("product;add;" + productJson);

                // Ожидаем ответ от сервера
                new Thread(() -> {
                    try {
                        String response = in.readLine();
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Добавление продукта");
                            alert.setHeaderText(null);
                            alert.setContentText(response);
                            alert.showAndWait();

                            productsList.add(newProduct);
                            handleFetchProductsByCategory(newProduct.getCategory().getName());

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
    private void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        String cat = selectedProduct.getCategoryName();
        if (selectedProduct != null) {
            int productId = selectedProduct.getProductID();
            productsList.remove(selectedProduct);
            out.println("product;delete;" + productId);

            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Удаление продукта");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();
                        productsList.remove(selectedProduct);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FXML
    private void handleSearchProduct() {
        String searchText = searchField.getText().toLowerCase();
        boolean found = false;
        for (Product product : productsList) {
            if (product.getName().toLowerCase().contains(searchText)) {
                productsTable.getSelectionModel().select(product);
                productsTable.scrollTo(product);
                found = true;
                break;
            }
        }
        if (!found) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Продукт не найден");
            alert.setHeaderText(null);
            alert.setContentText("Продукт с названием \"" + searchText + "\" не найден.");
            alert.showAndWait();
        }
    }
}
