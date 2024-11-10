package org.example.courseproject.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.example.courseproject.POJO.Product;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuController {
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private VBox contentArea;

    @FXML
    private ScrollPane scrollPane;
    private final ArrayList<String> categories = new ArrayList<>();

    @FXML
    public void initialize() {
        // Инициализация категорий в выпадающем списке
        fetchCategoriesFromServer();
        categoryComboBox.getItems().addAll(categories);
        categoryComboBox.setOnAction(event -> handleCategorySelection()); // Добавлено для обработки изменения категории

        // Установка вертикальной полосы прокрутки
        scrollPane.setContent(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Загрузка меню при инициализации
        loadMenu();
    }

    @FXML
    private void handleCategorySelection() {
        String selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            contentArea.getChildren().clear(); // Очистка контента
            List<Product> productsList = handleFetchProductsByCategory(selectedCategory); // Загрузка продуктов выбранной категории
            displayProducts(selectedCategory, productsList); // Отображение продуктов
        } else {
            loadMenu(); // Загрузка всего меню, если категория не выбрана
        }
    }

    private List<Product> handleFetchProductsByCategory(String category) {
        List<Product> productsList = new ArrayList<>();
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        PrintWriter out = networkController.getOut();
        BufferedReader in = networkController.getIn();
        try {
            out.println("product;fetchcat;" + category);
            String response;
            while ((response = in.readLine()) != null && !response.equals("end")) {
                Product product = ProductsController.parseProduct(response);
                product.setCategoryName(product.getCategory().getName());
                productsList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }


    private void fetchCategoriesFromServer() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        PrintWriter out = networkController.getOut();
        BufferedReader in = networkController.getIn();
        categories.add("Все");
        try {
            out.println("category;fetchnames;.");
            String response;
            while ((response = in.readLine()) != null && !response.equals("end")) {
                categories.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayProducts(String category, List<Product> productsList) {
        HBox currentRow = null;
        int productCount = 0;

        // Добавить заголовок категории
        Text categoryTitle = new Text(category);
        categoryTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        contentArea.getChildren().add(categoryTitle);

        // Добавить разделяющую линию
        Line separator = new Line(0, 0, 800, 0); // Длина линии 800 пикселей
        separator.setStyle("-fx-stroke: green; -fx-stroke-width: 2;");
        contentArea.getChildren().add(separator);

        for (Product product : productsList) {
            if (productCount == 3) {
                // Добавить новую строку для продуктов
                currentRow = new HBox(10);
                contentArea.getChildren().add(currentRow);
                productCount = 0;
            }

            if (currentRow == null) {
                currentRow = new HBox(10);
                contentArea.getChildren().add(currentRow);
            }

            VBox productBox = new VBox(5);
            productBox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: green; -fx-background-color: lightgreen;");
            productBox.setAlignment(Pos.CENTER); // Выровнять содержимое по центру
            productBox.setMinWidth(160); // Задаем минимальную ширину для продуктов

            ImageView productImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/categories_images/" + product.getCategory().getImage()))));
            productImage.setFitHeight(100); // Делаем иконки фиксированного размера
            productImage.setFitWidth(100);
            productImage.setPreserveRatio(false);

            Text productNameText = new Text(product.getName());
            productNameText.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;"); // Увеличиваем шрифт и выравниваем по центру
            productNameText.setWrappingWidth(150); // Устанавливаем ширину для переноса текста на новую строку

            Button detailButton = new Button("Подробный состав");
            detailButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-pref-width: 160px;");
            detailButton.setOnAction(event -> showProductDetails(product)); // Добавляем обработчик события

            Button addButton = new Button("Добавить в план");
            addButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-pref-width: 160px;");

            productBox.getChildren().addAll(productImage, productNameText, detailButton, addButton);
            currentRow.getChildren().add(productBox);
            productCount++;
        }
    }

    private void loadMenu() {
        contentArea.getChildren().clear(); // Очистка контента перед загрузкой всего меню
        String category = categories.get(1);
        List<Product> productsList = handleFetchProductsByCategory(category);
        displayProducts(category, productsList);

    }

    @FXML
    private void handleSearchProduct() {
        String searchQuery = searchField.getText().toLowerCase();
        boolean productFound = false;

        // Убираем существующее выделение
        for (Node node : contentArea.getChildren()) {
            if (node instanceof HBox productRow) {
                for (Node productNode : productRow.getChildren()) {
                    if (productNode instanceof VBox productBox) {
                        productBox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; " +
                                "-fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: green; " +
                                "-fx-background-color: lightgreen;");
                    }
                }
            }
        }

        // Поиск продукта и прокрутка до строки с искомым продуктом
        for (Node node : contentArea.getChildren()) {
            if (node instanceof Text || node instanceof Line) {
                continue; // Пропустить заголовки категорий и разделители
            }

            if (node instanceof HBox productRow) {
                for (Node productNode : productRow.getChildren()) {
                    if (productNode instanceof VBox productBox) {
                        Text productNameText = (Text) productBox.getChildren().get(1);
                        String productName = productNameText.getText().toLowerCase();

                        if (productName.contains(searchQuery)) {
                            // Прокрутка к строке с искомым продуктом
                            Platform.runLater(() -> scrollPane.setVvalue(node.getBoundsInParent().getMinY() / contentArea.getHeight()));
                            productBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10; " +
                                    "-fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; " +
                                    "-fx-border-radius: 5; -fx-border-color: green;");
                            return;
                        }
                    }
                }
            }
        }

        if (!productFound) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Продукт не найден");
            alert.setHeaderText(null);
            alert.setContentText("Продукт с названием \"" + searchQuery + "\" не найден.");
            alert.showAndWait();
        }
    }

    private void showProductDetails(Product product) {
        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Подробный состав продукта");
        detailsAlert.setHeaderText(product.getName());

        String content = "КБЖУ на 100 г продукта:\n" +
                "Белки: " + product.getNutrients().getMacroNutrients().getProteins() + " г\n" +
                "Жиры: " + product.getNutrients().getMacroNutrients().getFats() + " г\n" +
                "Углеводы: " + product.getNutrients().getMacroNutrients().getCarbs() + " г\n" +
                "Ккал: " + product.getNutrients().getMacroNutrients().getCalories() + " ккал\n\n" +
                "Содержание витаминов:\n" +
                "Витамин A: " + product.getNutrients().getVitamins().getA() + " мкг\n" +
                "Витамин D: " + product.getNutrients().getVitamins().getD() + " мкг\n" +
                "Витамин E: " + product.getNutrients().getVitamins().getE() + " мг\n" +
                "Витамин K: " + product.getNutrients().getVitamins().getK() + " мкг\n" +
                "Витамин C: " + product.getNutrients().getVitamins().getC() + " мг\n" +
                "Витамин B12: " + product.getNutrients().getVitamins().getB12() + " мкг\n\n" +
                "Содержание минералов:\n" +
                "Кальций Ca: " + product.getNutrients().getMinerals().getCa() + " мг\n" +
                "Железо Fe: " + product.getNutrients().getMinerals().getFe() + " мг\n" +
                "Магний Mg: " + product.getNutrients().getMinerals().getMg() + " мг\n" +
                "Цинк Zn: " + product.getNutrients().getMinerals().getZn() + " мг\n" +
                "Медь Cu: " + product.getNutrients().getMinerals().getCu() + " мг\n" +
                "Селен Se: " + product.getNutrients().getMinerals().getSe() + " мкг\n";

        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);

        detailsAlert.getDialogPane().setContent(contentLabel);
        detailsAlert.showAndWait();
    }


}
