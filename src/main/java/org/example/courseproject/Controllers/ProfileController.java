package org.example.courseproject.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.courseproject.POJO.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProfileController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private ComboBox<String> maleComboBox;
    @FXML
    private TextField heightField;
    @FXML
    private TextField weightField;
    @FXML
    private ComboBox<String> activityLevelComboBox;
    @FXML
    private TextField goalField;

    public MainController main;

    public void setUser(MainController controller)
    {
        main = controller;
    }
    void init(MainController controller){
        main = controller;
        emailField.setText(main.user.getEmail());
        nameField.setText(main.user.getInfo().getName());
        ageField.setText(String.valueOf(main.user.getInfo().getAge()));
        maleComboBox.setValue(main.user.getInfo().isMale() ? "Мужской" : "Женский");
        heightField.setText(String.valueOf(main.user.getInfo().getHeight()));
        weightField.setText(String.valueOf(main.user.getInfo().getWeight()));
        activityLevelComboBox.setValue(getActivityLevelString(main.user.getInfo().getActivityLevel()));
        goalField.setText(String.valueOf(main.user.getInfo().getGoal()));
    }

    @FXML
    private void handleApplyChanges() {
        main.user.getInfo().setName(nameField.getText());
        main.user.getInfo().setAge(Integer.parseInt(ageField.getText()));
        main.user.getInfo().setMale(maleComboBox.getValue().equals("Мужской"));
        main.user.getInfo().setHeight(Double.parseDouble(heightField.getText()));
        main.user.getInfo().setWeight(Double.parseDouble(weightField.getText()));
        main.user.getInfo().setActivityLevel(getActivityLevelInt(activityLevelComboBox.getValue()));
        main.user.getInfo().setGoal(Double.parseDouble(goalField.getText()));
        calculateDailyIntake();
        updateUser();
    }

    void updateUser()
    {
        NetworkController networkController = NetworkController.getInstance();
        networkController.connectToServer();
        PrintWriter out = networkController.getOut();
        BufferedReader in = networkController.getIn();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.registerModule(new Jdk8Module());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String userJson = mapper.writeValueAsString(main.user);
            System.out.println(userJson);
            out.println("user;update;" + userJson);
            new Thread(() -> {
                try {
                    String response = in.readLine();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Обновление профиля");
                        alert.setHeaderText(null);
                        alert.setContentText(response);
                        alert.showAndWait();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getActivityLevelString(int level) {
        switch (level) {
            case 1: return "Низкий";
            case 3: return "Высокий";
            default: return "Средний";
        }
    }

    private int getActivityLevelInt(String level) {
        switch (level) {
            case "Низкий": return 1;
            case "Высокий": return 3;
            default: return 2;
        }
    }

    public void calculateDailyIntake() {
        double height = main.user.getInfo().getHeight();
        double weight = main.user.getInfo().getWeight();
        int age = main.user.getInfo().getAge();
        boolean isMale = main.user.getInfo().isMale();
        int activityLevel = main.user.getInfo().getActivityLevel();

        Nutrients nutrients = calcMicroNutr(age, isMale, activityLevel);
        nutrients.setMacroNutrients(calcMacroNutr(weight, height, age, isMale, activityLevel));
        main.user.getInfo().setNorm(nutrients);
    }

    public MacroNutrients calcMacroNutr(double weight, double height, int age, boolean isMale, int activityLevel){
        // Расчет базового метаболизма (BMR) с использованием формулы Миффлин-Сент Жеор
        double bmr;
        if (isMale) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Рассчитываем суммарные энергозатраты (TDEE) с учетом уровня активности
        double tdee;
        switch (activityLevel) {
            case 1:
                tdee = bmr * 1.2;    // Малоподвижный образ жизни
                break;
            case 2:
                tdee = bmr * 1.55;   // Умеренная активность
                break;
            case 3:
                tdee = bmr * 1.725;  // Высокая активность
                break;
            default:
                tdee = bmr * 1.2;    // Малоподвижный образ жизни по умолчанию
                break;
        }

        // Нормы КБЖУ по рекомендациям
        double calories = round(tdee);
        double proteins = round(weight * 1.2);  // Примерно 1.2 г белка на кг массы тела
        double fats = round((tdee * 0.25) / 9);  // 25% калорий от жиров, 1 г жира = 9 ккал
        double carbs = round((tdee - (proteins * 4 + fats * 9)) / 4);  // Оставшиеся калории из углеводов, 1 г углеводов = 4 ккал

        return new MacroNutrients(calories, proteins, fats, carbs);
    }

    public Nutrients calcMicroNutr(int age, boolean isMale, int activityLevel) {
        Vitamins vitamins = new Vitamins();
        Minerals minerals = new Minerals();

        // Витамины
        vitamins.setA(round(age >= 18 ? (isMale ? 900 : 700) : 600)); // мкг
        vitamins.setD(15); // мкг
        vitamins.setE(15); // мг
        vitamins.setK(round(age >= 18 ? (isMale ? 120 : 90) : 70)); // мкг
        vitamins.setC(round(age >= 18 ? (isMale ? 90 : 75) : 60)); // мг
        vitamins.setB12(2.4); // мкг

        // Минералы
        minerals.setCa(round(age >= 18 ? 1000 : 1300)); // мг
        minerals.setFe(round(age >= 18 ? (isMale ? 8 : 18) : 10)); // мг
        minerals.setMg(round(age >= 18 ? (isMale ? 400 : 310) : 300)); // мг
        minerals.setZn(round(age >= 18 ? (isMale ? 11 : 8) : 11)); // мг
        minerals.setCu(0.9); // мг
        minerals.setSe(55); // мкг

        // Учет уровня физической активности
        if (activityLevel == 2) { // средний
            vitamins.setC(vitamins.getC() + 10); // корректировка витамина C
            minerals.setMg(minerals.getMg() + 50); // корректировка магния
        } else if (activityLevel == 3) { // высокий
            vitamins.setC(vitamins.getC() + 15);
            minerals.setMg(minerals.getMg() + 70);
        }

        return new Nutrients(vitamins, minerals);
    }

    private int round(double value) {
        return (int) Math.round(value);
    }


}
