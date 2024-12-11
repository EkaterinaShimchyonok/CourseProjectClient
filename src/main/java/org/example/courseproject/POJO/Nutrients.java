package org.example.courseproject.POJO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Nutrients")
public class Nutrients {
    @XmlTransient
    private int nutrientsID;
    private MacroNutrients macroNutrients;
    private Vitamins vitamins;
    private Minerals minerals;

    public Nutrients() {
        this.nutrientsID = 0;
        this.macroNutrients = new MacroNutrients();
        this.vitamins = new Vitamins();
        this.minerals = new Minerals();
    }

    public Nutrients(MacroNutrients macroNutrients, Vitamins vitamins, Minerals minerals) {
        this.macroNutrients = macroNutrients;
        this.vitamins = vitamins;
        this.minerals = minerals;
    }

    public Nutrients( Vitamins vitamins, Minerals minerals) {
        this.vitamins = vitamins;
        this.minerals = minerals;
    }

    @XmlTransient
    public int getNutrientsID() {
        return nutrientsID;
    }

    public void setNutrientsID(int nutrientsID) {
        this.nutrientsID = nutrientsID;
    }

    @XmlElement
    public MacroNutrients getMacroNutrients() {
        return macroNutrients;
    }

    public void setMacroNutrients(MacroNutrients macroNutrients) {
        this.macroNutrients = macroNutrients;
    }

    @XmlElement
    public Vitamins getVitamins() {
        return vitamins;
    }

    public void setVitamins(Vitamins vitamins) {
        this.vitamins = vitamins;
    }

    @XmlElement
    public Minerals getMinerals() {
        return minerals;
    }

    public void setMinerals(Minerals minerals) {
        this.minerals = minerals;
    }

    public void add(Nutrients other, double weight) {
        this.macroNutrients.add(other.getMacroNutrients(), weight);
        this.vitamins.add(other.getVitamins(), weight);
        this.minerals.add(other.getMinerals(), weight);
    }
}
