package org.example.courseproject.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.courseproject.POJO.FoodPlan;
import org.example.courseproject.POJO.Nutrients;
import org.example.courseproject.POJO.Product;
import org.example.courseproject.POJO.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PlanController {
    @FXML
    private VBox productListVBox = new VBox();
    @FXML
    private ScrollPane productListScrollPane = new ScrollPane();
    @FXML
    private Text totalCalories;
    @FXML
    private Text totalProteins;
    @FXML
    private Text totalFats;
    @FXML
    private Text totalCarbs;
    @FXML
    private Text totalVitaminA;
    @FXML
    private Text totalVitaminD;
    @FXML
    private Text totalVitaminE;
    @FXML
    private Text totalVitaminK;
    @FXML
    private Text totalVitaminC;
    @FXML
    private Text totalVitaminB12;
    @FXML
    private Text totalCalcium;
    @FXML
    private Text totalIron;
    @FXML
    private Text totalMagnesium;
    @FXML
    private Text totalZinc;
    @FXML
    private Text totalCopper;
    @FXML
    private Text totalSelenium;

    private FoodPlan plan;
    private Nutrients totalNutrients;

    private PrintWriter out;
    private BufferedReader in;

    void setInOut(){
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        out = networkController.getOut();
        in = networkController.getIn();
    }
    @FXML
    void initialize() {
        this.setInOut();
    }

    private void init() {
        plan = fetchPlan(plan.getUserID(), plan.getDate());
        loadPlan();
        calculatePlan();
    }

    void setUser(User user) {
        plan = new FoodPlan();
        plan.setUserID(user.getUserID());
        plan.setNorm(user.getInfo().getNorm());
        init();
    }

    private void loadPlan() {
        productListVBox.getChildren().clear();
        for (int i = 0; i < plan.getProducts().size(); i++) {
            Product product = plan.getProducts().get(i);
            double weight = plan.getWeights().get(i);

            GridPane productGrid = new GridPane(); // Используем GridPane для выравнивания
            productGrid.setHgap(10); // Горизонтальный отступ между элементами
            productGrid.setVgap(10); // Вертикальный отступ между элементами

            // Создаем элементы
            Label productName = new Label(product.getName());
            productName.setWrapText(true); // Разрешить перенос текста
            productName.setMaxWidth(200); // Ограничить ширину

            Button decreaseWeightButton = new Button("-");
            decreaseWeightButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");
            Label weightLabel = new Label(weight + " г");
            Button increaseWeightButton = new Button("+");
            increaseWeightButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white;");

            // Создаем кнопку с изображением для удаления продукта
            ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/trash.png")));
            trashIcon.setFitWidth(16); // Ширина изображения
            trashIcon.setFitHeight(16); // Высота изображения

            Button deleteProductButton = new Button();
            deleteProductButton.setStyle("-fx-background-color: white;");
            deleteProductButton.setGraphic(trashIcon);

            // Настраиваем кнопки
            int finalI = i;
            decreaseWeightButton.setOnAction(event -> {
                double newWeight = plan.getWeights().get(finalI) - 5;
                if (newWeight < 0) {
                    newWeight = 0;
                }
                plan.getWeights().set(finalI, newWeight);
                weightLabel.setText(newWeight + " г");
                calculatePlan();
            });

            increaseWeightButton.setOnAction(event -> {
                double newWeight = plan.getWeights().get(finalI) + 5;
                plan.getWeights().set(finalI, newWeight);
                weightLabel.setText(newWeight + " г");
                calculatePlan();
            });

            deleteProductButton.setOnAction(event -> {
                plan.getProducts().remove(finalI);
                plan.getWeights().remove(finalI);
                loadPlan();
                calculatePlan();
            });

            // Выравниваем метку веса и кнопку удаления по правому краю
            GridPane.setHalignment(weightLabel, HPos.RIGHT);
            GridPane.setHalignment(deleteProductButton, HPos.RIGHT);

            // Добавляем элементы в GridPane
            productGrid.add(productName, 0, 0, 1, 1); // Название продукта в первую колонку
            productGrid.add(decreaseWeightButton, 1, 0, 1, 1); // Кнопка уменьшения веса
            productGrid.add(weightLabel, 2, 0, 1, 1); // Метка веса
            productGrid.add(increaseWeightButton, 3, 0, 1, 1); // Кнопка увеличения веса
            productGrid.add(deleteProductButton, 4, 0, 1, 1); // Кнопка удаления продукта с изображением

            productListVBox.getChildren().add(productGrid);
        }
        productListScrollPane.setContent(productListVBox);
    }

    public FoodPlan fetchPlan(int uid, String d) {
        try {
            out.println("plan;fetch;" + uid + ',' + d);
            String response;
            response = in.readLine();
            System.out.println(response);
            if (response != null)
                return parsePlan(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FoodPlan();
    }

    @FXML
    private void handleUpdate() {
        updateDBPlan(plan);
    }

    public void updateDBPlan(FoodPlan foodPlan) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String planJson = mapper.writeValueAsString(foodPlan);
            System.out.println(planJson);
            out.println("plan;update;" + planJson);
            // Ожидаем ответ от сервера
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Сохранение плана питания");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FoodPlan parsePlan(String serverResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            return mapper.readValue(serverResponse, FoodPlan.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void calculatePlan() {
        totalNutrients = new Nutrients(); // Сбрасываем значения totalNutrients с использованием конструктора по умолчанию

        for (int i = 0; i < plan.getProducts().size(); i++) {
            Product product = plan.getProducts().get(i);
            Nutrients nutrients = product.getNutrients();
            double weight = plan.getWeights().get(i) / 100.0;
            totalNutrients.add(nutrients, weight);
        }

        Nutrients norm = plan.getNorm();

        totalCalories.setText("Калории: " + Math.round(totalNutrients.getMacroNutrients().getCalories() * 10.0) / 10.0 + " ккал (" + formatPercentage(totalNutrients.getMacroNutrients().getCalories(), norm.getMacroNutrients().getCalories()) + "%)");
        totalProteins.setText("Белки: " + Math.round(totalNutrients.getMacroNutrients().getProteins() * 10.0) / 10.0 + " г (" + formatPercentage(totalNutrients.getMacroNutrients().getProteins(), norm.getMacroNutrients().getProteins()) + "%)");
        totalFats.setText("Жиры: " + Math.round(totalNutrients.getMacroNutrients().getFats() * 10.0) / 10.0 + " г (" + formatPercentage(totalNutrients.getMacroNutrients().getFats(), norm.getMacroNutrients().getFats()) + "%)");
        totalCarbs.setText("Углеводы: " + Math.round(totalNutrients.getMacroNutrients().getCarbs() * 10.0) / 10.0 + " г (" + formatPercentage(totalNutrients.getMacroNutrients().getCarbs(), norm.getMacroNutrients().getCarbs()) + "%)");
        totalVitaminA.setText("Витамин A: " + Math.round(totalNutrients.getVitamins().getA() * 10.0) / 10.0 + " мкг (" + formatPercentage(totalNutrients.getVitamins().getA(), norm.getVitamins().getA()) + "%)");
        totalVitaminD.setText("Витамин D: " + Math.round(totalNutrients.getVitamins().getD() * 10.0) / 10.0 + " мкг (" + formatPercentage(totalNutrients.getVitamins().getD(), norm.getVitamins().getD()) + "%)");
        totalVitaminE.setText("Витамин E: " + Math.round(totalNutrients.getVitamins().getE() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getVitamins().getE(), norm.getVitamins().getE()) + "%)");
        totalVitaminK.setText("Витамин K: " + Math.round(totalNutrients.getVitamins().getK() * 10.0) / 10.0 + " мкг (" + formatPercentage(totalNutrients.getVitamins().getK(), norm.getVitamins().getK()) + "%)");
        totalVitaminC.setText("Витамин C: " + Math.round(totalNutrients.getVitamins().getC() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getVitamins().getC(), norm.getVitamins().getC()) + "%)");
        totalVitaminB12.setText("Витамин B12: " + Math.round(totalNutrients.getVitamins().getB12() * 10.0) / 10.0 + " мкг (" + formatPercentage(totalNutrients.getVitamins().getB12(), norm.getVitamins().getB12()) + "%)");
        totalCalcium.setText("Кальций: " + Math.round(totalNutrients.getMinerals().getCa() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getMinerals().getCa(), norm.getMinerals().getCa()) + "%)");
        totalIron.setText("Железо: " + Math.round(totalNutrients.getMinerals().getFe() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getMinerals().getFe(), norm.getMinerals().getFe()) + "%)");
        totalMagnesium.setText("Магний: " + Math.round(totalNutrients.getMinerals().getMg() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getMinerals().getMg(), norm.getMinerals().getMg()) + "%)");
        totalZinc.setText("Цинк: " + Math.round(totalNutrients.getMinerals().getZn() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getMinerals().getZn(), norm.getMinerals().getZn()) + "%)");
        totalCopper.setText("Медь: " + Math.round(totalNutrients.getMinerals().getCu() * 10.0) / 10.0 + " мг (" + formatPercentage(totalNutrients.getMinerals().getCu(), norm.getMinerals().getCu()) + "%)");
        totalSelenium.setText("Селен: " + Math.round(totalNutrients.getMinerals().getSe() * 10.0) / 10.0 + " мкг (" + formatPercentage(totalNutrients.getMinerals().getSe(), norm.getMinerals().getSe()) + "%)");

        plan.setTotalCal(Math.round(totalNutrients.getMacroNutrients().getCalories() * 10.0) / 10.0);
    }


    private String formatPercentage(double value, double norm) {
        double percentage = (value / norm) * 100;
        if (norm <= 0.0)
            percentage = 0.0;
        return String.format("%.1f", percentage);
    }

    @FXML
    void handleClearPlan() {
        plan.getProducts().clear();
        plan.setWeights(new ArrayList<>());
        loadPlan();
        calculatePlan();
    }
    @FXML
    void handleSaveXML() {
        // Показываем диалоговое окно для ввода имени файла
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Сохранить план питания");
        dialog.setHeaderText("Введите имя файла для сохранения плана питания:");
        dialog.setContentText("Имя файла:");

        dialog.showAndWait().ifPresent(fileName -> {
            if (!fileName.isEmpty()) {
                savePlanToFile(fileName);
            } else {
                showErrorAlert("Ошибка", "Имя файла не может быть пустым!");
            }
        });
    }

    private void savePlanToFile(String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(FoodPlan.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обновляем план питания суммарными показателями
            updatePlanWithTotals();

            File file = new File(fileName + ".xml");
            marshaller.marshal(plan, file);

            showConfirmationAlert("Успешно", "План питания успешно сохранен в файл " + fileName + ".xml");
        } catch (Exception e) {
            showErrorAlert("Ошибка", "Не удалось сохранить план питания: " + e.getMessage());
        }
    }

    private void updatePlanWithTotals() {
        totalNutrients = new Nutrients(); // Сбрасываем значения totalNutrients с использованием конструктора по умолчанию

        for (int i = 0; i < plan.getProducts().size(); i++) {
            Product product = plan.getProducts().get(i);
            Nutrients nutrients = product.getNutrients();
            double weight = plan.getWeights().get(i) / 100.0;
            totalNutrients.add(nutrients, weight);
        }
    }

    // Методы для отображения алертов
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
