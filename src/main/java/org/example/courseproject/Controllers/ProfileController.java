package org.example.courseproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.courseproject.POJO.User;

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
    private User user;

    public void setUser(User user) {
        this.user = user;
        emailField.setText(user.getEmail());
        nameField.setText(user.getInfo().getName());
        ageField.setText(String.valueOf(user.getInfo().getAge()));
        maleComboBox.setValue(user.getInfo().isMale() ? "Мужской" : "Женский");
        heightField.setText(String.valueOf(user.getInfo().getHeight()));
        weightField.setText(String.valueOf(user.getInfo().getWeight()));
        activityLevelComboBox.setValue(getActivityLevelString(user.getInfo().getActivityLevel()));
        goalField.setText(String.valueOf(user.getInfo().getGoal()));
    }

    @FXML
    private void handleApplyChanges() {
        user.getInfo().setName(nameField.getText());
        user.getInfo().setAge(Integer.parseInt(ageField.getText()));
        user.getInfo().setMale(maleComboBox.getValue().equals("Мужской"));
        user.getInfo().setHeight(Double.parseDouble(heightField.getText()));
        user.getInfo().setWeight(Double.parseDouble(weightField.getText()));
        user.getInfo().setActivityLevel(getActivityLevelInt(activityLevelComboBox.getValue()));
        user.getInfo().setGoal(Double.parseDouble(goalField.getText()));
        calculateDailyIntake();
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
        double height = user.getInfo().getHeight();
        double weight = user.getInfo().getWeight();
        int age = user.getInfo().getAge();
        boolean isMale = user.getInfo().isMale();
        int activityLevel = user.getInfo().getActivityLevel();

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
        user.getInfo().getNorm().getMacroNutrients().setCalories(round(tdee));
        user.getInfo().getNorm().getMacroNutrients().setProteins(round(tdee * 0.15));
        user.getInfo().getNorm().getMacroNutrients().setFats(round(tdee * 0.25));
        user.getInfo().getNorm().getMacroNutrients().setCarbs(round(tdee * 0.6));

        // Расчетные нормы витаминов и минералов
        user.getInfo().getNorm().getVitamins().setA(age > 18 ? (isMale ? 900 : 700) : 600); // мкг
        user.getInfo().getNorm().getVitamins().setD(age > 18 ? (isMale ? 20 : 15) : 10); // мкг
        user.getInfo().getNorm().getVitamins().setE(15); // мг
        user.getInfo().getNorm().getVitamins().setK(age > 18 ? (isMale ? 120 : 90) : 70); // мкг
        user.getInfo().getNorm().getVitamins().setC(age > 18 ? (isMale ? 90 : 75) : 60); // мкг
        user.getInfo().getNorm().getVitamins().setB12(age > 18 ? 2.4 : 1.8); // мкг
        user.getInfo().getNorm().getMinerals().setCa(age > 18 ? (isMale ? 1000 : 1300) : 1300); // мг
        user.getInfo().getNorm().getMinerals().setFe(age > 18 ? (isMale ? 8 : 18) : (isMale ? 8 : 10)); // мг
        user.getInfo().getNorm().getMinerals().setMg(age > 18 ? (isMale ? 400 : 310) : 300); // мг
        user.getInfo().getNorm().getMinerals().setZn(age > 18 ? (isMale ? 11 : 8) : (isMale ? 11 : 9)); // мг
        user.getInfo().getNorm().getMinerals().setCu(0.9); // мг
        user.getInfo().getNorm().getMinerals().setSe(age > 18 ? 55 : 40); // мкг
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
