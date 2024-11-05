package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MacroNutrients {
    private int macronID;
    private final DoubleProperty calories = new SimpleDoubleProperty();
    private final DoubleProperty proteins = new SimpleDoubleProperty();
    private final DoubleProperty fats = new SimpleDoubleProperty();
    private final DoubleProperty carbs = new SimpleDoubleProperty();

    public MacroNutrients() {
    }

    public MacroNutrients(int id, double calories, double proteins, double fats, double carbs) {
        this.macronID = id;
        this.calories.set(calories);
        this.proteins.set(proteins);
        this.fats.set(fats);
        this.carbs.set(carbs);
    }

    public int getMacronID() {
        return macronID;
    }

    public void setMacronID(int macronID) {
        this.macronID = macronID;
    }

    public double getCalories() {
        return calories.get();
    }

    public void setCalories(double calories) {
        this.calories.set(calories);
    }

    public DoubleProperty caloriesProperty() {
        return calories;
    }

    public double getProteins() {
        return proteins.get();
    }

    public void setProteins(double proteins) {
        this.proteins.set(proteins);
    }

    public DoubleProperty proteinsProperty() {
        return proteins;
    }

    public double getFats() {
        return fats.get();
    }

    public void setFats(double fats) {
        this.fats.set(fats);
    }

    public DoubleProperty fatsProperty() {
        return fats;
    }

    public double getCarbs() {
        return carbs.get();
    }

    public void setCarbs(double carbs) {
        this.carbs.set(carbs);
    }

    public DoubleProperty carbsProperty() {
        return carbs;
    }
}
