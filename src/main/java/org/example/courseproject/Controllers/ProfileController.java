package org.example.courseproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.courseproject.POJO.User;

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
        activityLevelComboBox.setValue(getActivityLevelString(user.getInfo().getActivity_level()));
        goalField.setText(String.valueOf(user.getInfo().getGoal()));
    }

    @FXML
    private void handleApplyChanges() {
        user.getInfo().setName(nameField.getText());
        user.getInfo().setAge(Integer.parseInt(ageField.getText()));
        user.getInfo().setMale(maleComboBox.getValue().equals("Мужской"));
        user.getInfo().setHeight(Double.parseDouble(heightField.getText()));
        user.getInfo().setWeight(Double.parseDouble(weightField.getText()));
        user.getInfo().setActivity_level(getActivityLevelInt(activityLevelComboBox.getValue()));
        user.getInfo().setGoal(Double.parseDouble(goalField.getText()));
    }

    private String getActivityLevelString(int level) {
        switch (level) {
            case 1: return "1 - Низкий";
            case 2: return "2 - Средний";
            case 3: return "3 - Высокий";
            default: return "";
        }
    }

    private int getActivityLevelInt(String level) {
        switch (level) {
            case "1 - Низкий": return 1;
            case "2 - Средний": return 2;
            case "3 - Высокий": return 3;
            default: return 0;
        }
    }
}
