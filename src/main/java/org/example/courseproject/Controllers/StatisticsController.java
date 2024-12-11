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
    private Nutrients norm;

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
        this.norm = user.getInfo().getNorm();

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
        prepareChartAxes();  // Инициализируем ось X с датами
        populateNutrientComboBox();
        requestData();       // Запрашиваем данные после подготовки оси
    }

    private void prepareChartAxes() {
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 14; i++) {
            LocalDate date = now.minusDays(13 - i);
            xAxis.getCategories().add(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }
        nutrientChart.getXAxis().setAnimated(false);
    }

    private void requestData() {
        new Thread(() -> {
            try {
                LocalDate now = LocalDate.now();
                LocalDate[] dates = new LocalDate[14];
                for (int i = 0; i < 14; i++) {
                    dates[i] = now.minusDays(13 - i);
                }

                for (LocalDate date : dates) {
                    Nutrients totalNutrients = calculateTotalNutrients(date);
                    Platform.runLater(() -> {
                        nutrientDataMap.put(date, totalNutrients);
                        if (nutrientComboBox.getValue() != null) {
                            updateChartForSelectedNutrient(nutrientComboBox.getValue(), date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), totalNutrients);
                        }
                    });
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
            case "Витамин A", "Витамин C", "Витамин D", "Витамин E", "Витамин K", "Витамин B12", "Кальций",
                 "Железо",
                 "Магний", "Цинк", "Медь", "Селен" -> "(мг)";
            default -> "";
        };
    }

    private void displayDataForSelectedNutrient(String nutrient) {
        Platform.runLater(() -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Показатель");

            // Сначала добавляем данные для норм
            addNormLine(nutrientChart, nutrientNormMap.getOrDefault(nutrient, 0.0), "Ваша норма " + nutrient);

            // Затем добавляем данные для выбранного нутриента
            for (LocalDate date : nutrientDataMap.keySet()) {
                Nutrients totalNutrients = nutrientDataMap.get(date);
                double value = getNutrientValue(totalNutrients, nutrient);
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), value);
                series.getData().add(dataPoint);
                addDataLabel(dataPoint, String.valueOf(value));
            }

            nutrientChart.getData().add(series);
        });
    }


    private void updateChartForSelectedNutrient(String nutrient, String date, Nutrients totalNutrients) {
        double normValue = nutrientNormMap.getOrDefault(nutrient, 0.0);
        double value = getNutrientValue(totalNutrients, nutrient);

        XYChart.Series<String, Number> series = nutrientChart.getData().stream()
                .filter(s -> "Показатель".equals(s.getName()))
                .findFirst()
                .orElseGet(() -> {
                    XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName("Показатель");
                    nutrientChart.getData().add(newSeries);
                    return newSeries;
                });

        XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(date, value);
        series.getData().add(dataPoint);
        addDataLabel(dataPoint, String.valueOf(value));

        addNormLine(nutrientChart, normValue, "Ваша норма " + nutrient + " " + normValue + getUnitForNutrient(nutrient));
    }


    private double getNutrientValue(Nutrients nutrients, String nutrient) {
        return switch (nutrient) {
            case "Калории" -> Math.round(nutrients.getMacroNutrients().getCalories());
            case "Белки" -> Math.round(nutrients.getMacroNutrients().getProteins());
            case "Жиры" -> Math.round(nutrients.getMacroNutrients().getFats());
            case "Углеводы" -> Math.round(nutrients.getMacroNutrients().getCarbs());
            case "Витамин A" -> Math.round(nutrients.getVitamins().getA());
            case "Витамин C" -> Math.round(nutrients.getVitamins().getC());
            case "Витамин D" -> Math.round(nutrients.getVitamins().getD());
            case "Витамин E" -> Math.round(nutrients.getVitamins().getE());
            case "Витамин K" -> Math.round(nutrients.getVitamins().getK());
            case "Витамин B12" -> Math.round(nutrients.getVitamins().getB12());
            case "Кальций" -> Math.round(nutrients.getMinerals().getCa());
            case "Железо" -> Math.round(nutrients.getMinerals().getFe());
            case "Магний" -> Math.round(nutrients.getMinerals().getMg());
            case "Цинк" -> Math.round(nutrients.getMinerals().getZn());
            case "Медь" -> Math.round(nutrients.getMinerals().getCu());
            case "Селен" -> Math.round(nutrients.getMinerals().getSe());
            default -> 0.0;
        };
    }


    private void addNormLine(LineChart<String, Number> chart, double normValue, String label) {
        XYChart.Series<String, Number> existingNormSeries = new XYChart.Series<>();
        existingNormSeries.setName(label);

        for (String date : xAxis.getCategories()) {
            XYChart.Data<String, Number> normDataPoint = new XYChart.Data<>(date, normValue);
            existingNormSeries.getData().add(normDataPoint);
            addDataLabel(normDataPoint, String.valueOf(normValue));
        }

        Platform.runLater(() -> chart.getData().add(existingNormSeries));
    }

    private void addDataLabel(XYChart.Data<String, Number> dataPoint, String text) {
        Text dataText = new Text(text);
        dataText.setStyle("-fx-font-size: 10;");
        StackPane stackPane = new StackPane(dataText);
        stackPane.setAlignment(Pos.TOP_CENTER);
        stackPane.setPadding(new Insets(-10, 0, 0, 0));
        dataPoint.setNode(stackPane);
    }
}

