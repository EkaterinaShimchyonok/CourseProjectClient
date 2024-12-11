package org.example.courseproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.courseproject.POJO.User;

public class DailyIntakeController {
    @FXML
    private Label title;
    @FXML
    private Label vitaminsLabel;
    @FXML
    private Label mineralsLabel;
    @FXML
    private Label macroNutrientsLabel;

    private User user = new User();

    public void setUser(User us) {
        this.user = us;
        double condition = user.getInfo().getAge() * user.getInfo().getHeight() * user.getInfo().getWeight();
        if (condition != 0.0) {
            vitaminsLabel.setText(formatVitamins());
            mineralsLabel.setText(formatMinerals());
            macroNutrientsLabel.setText(formatMacroNutrients());
        } else title.setText("Ваш профиль не заполнен");
    }

    private String formatVitamins() {
        return "A: " + user.getInfo().getNorm().getVitamins().getA() + " мкг\n" +
                "D: " + user.getInfo().getNorm().getVitamins().getD() + " мкг\n" +
                "E: " + user.getInfo().getNorm().getVitamins().getE() + " мг\n" +
                "K: " + user.getInfo().getNorm().getVitamins().getK() + " мкг\n" +
                "C: " + user.getInfo().getNorm().getVitamins().getC() + " мг\n" +
                "B12: " + user.getInfo().getNorm().getVitamins().getB12() + " мкг";
    }

    private String formatMinerals() {
        return "Кальций: " + user.getInfo().getNorm().getMinerals().getCa() + " мг\n" +
                "Железо: " + user.getInfo().getNorm().getMinerals().getFe() + " мг\n" +
                "Магний: " + user.getInfo().getNorm().getMinerals().getMg() + " мг\n" +
                "Цинк: " + user.getInfo().getNorm().getMinerals().getZn() + " мг\n" +
                "Медь: " + user.getInfo().getNorm().getMinerals().getCu() + " мг\n" +
                "Селен: " + user.getInfo().getNorm().getMinerals().getSe() + " мкг";
    }

    private String formatMacroNutrients() {
        return "Калории: " + user.getInfo().getNorm().getMacroNutrients().getCalories() + " ккал\n" +
                "Белки: " + user.getInfo().getNorm().getMacroNutrients().getProteins() + " г\n" +
                "Жиры: " + user.getInfo().getNorm().getMacroNutrients().getFats() + " г\n" +
                "Углеводы: " + user.getInfo().getNorm().getMacroNutrients().getCarbs() + " г";
    }
}
