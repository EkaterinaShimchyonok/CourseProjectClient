package org.example.courseproject.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.courseproject.POJO.FoodPlan;
import org.example.courseproject.POJO.Nutrients;
import org.example.courseproject.POJO.Product;
import org.example.courseproject.POJO.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class StatisticsController {
    private int userID;

    @FXML
    private ComboBox<String> nutrientComboBox;

    @FXML
    private LineChart<String, Number> nutrientChart;

    // Сохраним все даты в оси X для использования в графиках
    private final CategoryAxis xAxis = new CategoryAxis();

    private final Map<String, Double> nutrientNormMap = new HashMap<>();

    private final Map<LocalDate, Nutrients> nutrientDataMap = new HashMap<>();

    void setInOut() {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
    }

    void setUser(User user) {
        this.userID = user.getUserID();
        Nutrients norm = user.getInfo().getNorm();

        nutrientNormMap.put("Калории", norm.getMacroNutrients().getCalories());
        nutrientNormMap.put("Белки", norm.getMacroNutrients().getProteins());
        nutrientNormMap.put("Жиры", norm.getMacroNutrients().getFats());
        nutrientNormMap.put("Углеводы", norm.getMacroNutrients().getCarbs());
        nutrientNormMap.put("Витамин A", norm.getVitamins().getA());
        nutrientNormMap.put("Витамин C", norm.getVitamins().getC());
        nutrientNormMap.put("Витамин D", norm.getVitamins().getD());
        nutrientNormMap.put("Витамин E", norm.getVitamins().getE());
        nutrientNormMap.put("Витамин K", norm.getVitamins().getK());
        nutrientNormMap.put("Витамин B12", norm.getVitamins().getB12());
        nutrientNormMap.put("Кальций", norm.getMinerals().getCa());
        nutrientNormMap.put("Железо", norm.getMinerals().getFe());
        nutrientNormMap.put("Магний", norm.getMinerals().getMg());
        nutrientNormMap.put("Цинк", norm.getMinerals().getZn());
        nutrientNormMap.put("Медь", norm.getMinerals().getCu());
        nutrientNormMap.put("Селен", norm.getMinerals().getSe());
    }

    @FXML
    public void initialize() {
        setInOut();
        requestData();
        populateNutrientComboBox();
    }

    private void requestData() {
        new Thread(() -> {
            try {
                LocalDate now = LocalDate.now();
                LocalDate[] dates = new LocalDate[14];
                for (int i = 0; i < 14; i++) {
                    dates[i] = now.minusDays(13 - i);  // Заполняем массив дат в нужном порядке
                }

                // Обновляем ось X графика в правильном порядке
                Platform.runLater(() -> {
                    xAxis.getCategories().clear();
                    for (LocalDate date : dates) {
                        xAxis.getCategories().add(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    }
                });

                // Запрашиваем данные и сохраняем их во временную карту
                for (LocalDate date : dates) {
                    Nutrients totalNutrients = calculateTotalNutrients(date);
                    Platform.runLater(() -> nutrientDataMap.put(date, totalNutrients));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void populateNutrientComboBox() {
        nutrientComboBox.setItems(FXCollections.observableArrayList("Калории", "Белки", "Жиры", "Углеводы",
                "Витамин A", "Витамин C", "Витамин D", "Витамин E", "Витамин K", "Витамин B12", "Кальций", "Железо",
                "Магний", "Цинк", "Медь", "Селен"));
    }

    private Nutrients calculateTotalNutrients(LocalDate date) {
        PlanController planController = new PlanController();
        planController.setInOut();
        FoodPlan plan = planController.fetchPlan(userID, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        Nutrients totalNutrients = new Nutrients();
        for (int i = 0; i < plan.getProducts().size(); i++) {
            Product product = plan.getProducts().get(i);
            Nutrients nutrients = product.getNutrients();
            double weight = plan.getWeights().get(i) / 100.0;
            totalNutrients.add(nutrients, weight);
        }
        return totalNutrients;
    }

    @FXML
    private void onNutrientSelected() {
        String selectedNutrient = nutrientComboBox.getValue();
        if (selectedNutrient != null) {
            nutrientChart.getData().clear();
            updateChartTitle(selectedNutrient);
            displayDataForSelectedNutrient(selectedNutrient);
        }
    }

    private void updateChartTitle(String nutrient) {
        String unit = getUnitForNutrient(nutrient);
        nutrientChart.setTitle(nutrient + " " + unit);
    }

    private String getUnitForNutrient(String nutrient) {
        return switch (nutrient) {
            case "Калории" -> "(ккал)";
            case "Белки", "Жиры", "Углеводы" -> "(г)";
            case "Витамин A", "Витамин C", "Витамин D", "Витамин E", "Витамин K", "Витамин B12", "Кальций", "Железо",
                 "Магний", "Цинк", "Медь", "Селен" -> "(мг)";
            default -> "";
        };
    }

    private void displayDataForSelectedNutrient(String nutrient) {
        nutrientDataMap.forEach((date, totalNutrients) -> Platform.runLater(() -> updateChartForSelectedNutrient(nutrient, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), totalNutrients)));
    }

    private void updateChartForSelectedNutrient(String nutrient, String date, Nutrients totalNutrients) {
        double normValue = nutrientNormMap.getOrDefault(nutrient, 0.0);
        double value = getNutrientValue(totalNutrients, nutrient);
        updateChart(nutrientChart, date, value, normValue, "Ваша норма " + nutrient + " " + normValue + getUnitForNutrient(nutrient));
    }

    private double getNutrientValue(Nutrients nutrients, String nutrient) {
        return switch (nutrient) {
            case "Калории" -> nutrients.getMacroNutrients().getCalories();
            case "Белки" -> nutrients.getMacroNutrients().getProteins();
            case "Жиры" -> nutrients.getMacroNutrients().getFats();
            case "Углеводы" -> nutrients.getMacroNutrients().getCarbs();
            case "Витамин A" -> nutrients.getVitamins().getA();
            case "Витамин C" -> nutrients.getVitamins().getC();
            case "Витамин D" -> nutrients.getVitamins().getD();
            case "Витамин E" -> nutrients.getVitamins().getE();
            case "Витамин K" -> nutrients.getVitamins().getK();
            case "Витамин B12" -> nutrients.getVitamins().getB12();
            case "Кальций" -> nutrients.getMinerals().getCa();
            case "Железо" -> nutrients.getMinerals().getFe();
            case "Магний" -> nutrients.getMinerals().getMg();
            case "Цинк" -> nutrients.getMinerals().getZn();
            case "Медь" -> nutrients.getMinerals().getCu();
            case "Селен" -> nutrients.getMinerals().getSe();
            default -> 0.0;
        };
    }

    private void updateChart(LineChart<String, Number> chart, String date, double value, double normValue, String normLabel) {
        XYChart.Series<String, Number> series = chart.getData().isEmpty() ? new XYChart.Series<>() : chart.getData().get(0);
        if (chart.getData().isEmpty()) {
            chart.getData().add(series);
            series.setName("Показатель");
        }

        XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(date, value);
        series.getData().add(dataPoint);
        addDataLabel(dataPoint, String.valueOf(value));

        addNormLine(chart, normValue, normLabel);
    }

    private void addNormLine(LineChart<String, Number> chart, double normValue, String label) {
        XYChart.Series<String, Number> existingNormSeries = chart.getData().stream()
                .filter(series -> series.getName().equals(label))
                .findFirst()
                .orElse(null);

        if (existingNormSeries != null) {
            existingNormSeries.getData().clear();
        } else {
            existingNormSeries = new XYChart.Series<>();
            existingNormSeries.setName(label);
            chart.getData().add(existingNormSeries);
        }

        for (String date : xAxis.getCategories()) {
            XYChart.Data<String, Number> normDataPoint = new XYChart.Data<>(date, normValue);
            existingNormSeries.getData().add(normDataPoint);
            addDataLabel(normDataPoint, String.valueOf(normValue));
        }
    }

    private void addDataLabel(XYChart.Data<String, Number> dataPoint, String text) {
        Text dataText = new Text(text);
        dataText.setStyle("-fx-font-size: 10;");
        dataText.setFill(Color.BLUE); // Цвет текста
        StackPane stackPane = new StackPane(dataText);
        stackPane.setAlignment(Pos.TOP_CENTER);
        stackPane.setPadding(new Insets(-10, 0, 0, 0));
        dataPoint.setNode(stackPane);
    }
}

