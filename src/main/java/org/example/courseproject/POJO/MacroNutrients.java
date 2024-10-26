package org.example.courseproject.POJO;

public class MacroNutrients {
    int macronID;
    double calories;
    double proteins;
    double fats;
    double carbs;

    public MacroNutrients() {
        this.macronID = 0;
        this.calories = 0;
        this.proteins = 0;
        this.fats = 0;
        this.carbs = 0;
    }
    public MacroNutrients(int id, double calories, double proteins, double fats, double carbs) {
        this.macronID = id;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }
    public int getMacronID() {
        return macronID;
    }

    public void setMacronID(int macronID) {
        this.macronID = macronID;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
}
