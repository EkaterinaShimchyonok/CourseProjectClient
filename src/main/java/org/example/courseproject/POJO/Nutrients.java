package org.example.courseproject.POJO;

public class Nutrients {
    int nutrientsID;
    MacroNutrients macroNutrients;
    Vitamins vitamins;
    Minerals minerals;

    public Nutrients(){}
    public Nutrients(int id, MacroNutrients macroNutrients, Vitamins vitamins, Minerals minerals) {
        this.nutrientsID = id;
        this.macroNutrients = macroNutrients;
        this.vitamins = vitamins;
        this.minerals = minerals;
    }

    public int getNutrientsID() {
        return nutrientsID;
    }

    public void setNutrientsID(int nutrientsID) {
        this.nutrientsID = nutrientsID;
    }

    public MacroNutrients getMacroNutrients() {
        return macroNutrients;
    }

    public void setMacroNutrients(MacroNutrients macroNutrients) {
        this.macroNutrients = macroNutrients;
    }

    public Vitamins getVitamins() {
        return vitamins;
    }

    public void setVitamins(Vitamins vitamins) {
        this.vitamins = vitamins;
    }

    public Minerals getMinerals() {
        return minerals;
    }

    public void setMinerals(Minerals minerals) {
        this.minerals = minerals;
    }
}
