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
import org.example.courseproject.POJO.User;

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

    MainController main;

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
            out.println("userupdate;" + userJson);
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
            case 1: return "1 - Низкий";
            case 3: return "3 - Высокий";
            default: return "2 - Средний";
        }
    }

    private int getActivityLevelInt(String level) {
        switch (level) {
            case "1 - Низкий": return 1;
            case "3 - Высокий": return 3;
            default: return 2;
        }
    }

    private void calculateDailyIntake() {
        double height = main.user.getInfo().getHeight();
        double weight = main.user.getInfo().getWeight();
        int age = main.user.getInfo().getAge();
        boolean isMale = main.user.getInfo().isMale();
        int activityLevel = main.user.getInfo().getActivityLevel();

        // Алгоритм Маффина для расчета КБЖУ
        double bmr;
        if (isMale) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        double tdee;
        switch (activityLevel) {
            case 1:
                tdee = bmr * 1.2;
                break;
            case 2:
                tdee = bmr * 1.55;
                break;
            case 3:
                tdee = bmr * 1.725;
                break;
            default:
                tdee = bmr * 1.2;
                break;
        }

        // Округляем до 1 знака после запятой
        main.user.getInfo().getNorm().getMacroNutrients().setCalories(round(tdee));
        main.user.getInfo().getNorm().getMacroNutrients().setProteins(round(tdee * 0.15));
        main.user.getInfo().getNorm().getMacroNutrients().setFats(round(tdee * 0.25));
        main.user.getInfo().getNorm().getMacroNutrients().setCarbs(round(tdee * 0.6));

        // Расчетные нормы витаминов и минералов
        main.user.getInfo().getNorm().getVitamins().setA(age > 18 ? (isMale ? 900 : 700) : 600); // мкг
        main.user.getInfo().getNorm().getVitamins().setD(age > 18 ? (isMale ? 20 : 15) : 10); // мкг
        main.user.getInfo().getNorm().getVitamins().setE(15); // мг
        main.user.getInfo().getNorm().getVitamins().setK(age > 18 ? (isMale ? 120 : 90) : 70); // мкг
        main.user.getInfo().getNorm().getVitamins().setC(age > 18 ? (isMale ? 90 : 75) : 60); // мкг
        main.user.getInfo().getNorm().getVitamins().setB12(age > 18 ? 2.4 : 1.8); // мкг
        main.user.getInfo().getNorm().getMinerals().setCa(age > 18 ? (isMale ? 1000 : 1300) : 1300); // мг
        main.user.getInfo().getNorm().getMinerals().setFe(age > 18 ? (isMale ? 8 : 18) : (isMale ? 8 : 10)); // мг
        main.user.getInfo().getNorm().getMinerals().setMg(age > 18 ? (isMale ? 400 : 310) : 300); // мг
        main.user.getInfo().getNorm().getMinerals().setZn(age > 18 ? (isMale ? 11 : 8) : (isMale ? 11 : 9)); // мг
        main.user.getInfo().getNorm().getMinerals().setCu(0.9); // мг
        main.user.getInfo().getNorm().getMinerals().setSe(age > 18 ? 55 : 40); // мкг
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
