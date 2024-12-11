package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "MacroNutrients")
public class MacroNutrients {
    @XmlTransient
    private int macronID;
    private final DoubleProperty calories = new SimpleDoubleProperty();
    private final DoubleProperty proteins = new SimpleDoubleProperty();
    private final DoubleProperty fats = new SimpleDoubleProperty();
    private final DoubleProperty carbs = new SimpleDoubleProperty();

    public MacroNutrients() {
        calories.set(0.0);
        proteins.set(0.0);
        fats.set(0.0);
        carbs.set(0.0);
    }

    public MacroNutrients(double calories, double proteins, double fats, double carbs) {
        this.calories.set(calories);
        this.proteins.set(proteins);
        this.fats.set(fats);
        this.carbs.set(carbs);
    }

    @XmlTransient
    public int getMacronID() {
        return macronID;
    }

    public void setMacronID(int macronID) {
        this.macronID = macronID;
    }

    @XmlElement
    public double getCalories() {
        return calories.get();
    }

    public void setCalories(double calories) {
        this.calories.set(calories);
    }

    public DoubleProperty caloriesProperty() {
        return calories;
    }

    @XmlElement
    public double getProteins() {
        return proteins.get();
    }

    public void setProteins(double proteins) {
        this.proteins.set(proteins);
    }

    public DoubleProperty proteinsProperty() {
        return proteins;
    }

    @XmlElement
    public double getFats() {
        return fats.get();
    }

    public void setFats(double fats) {
        this.fats.set(fats);
    }

    public DoubleProperty fatsProperty() {
        return fats;
    }

    @XmlElement
    public double getCarbs() {
        return carbs.get();
    }

    public void setCarbs(double carbs) {
        this.carbs.set(carbs);
    }

    public DoubleProperty carbsProperty() {
        return carbs;
    }

    public void add(MacroNutrients other, double weight) {
        this.calories.set(this.getCalories() + other.getCalories() * weight);
        this.proteins.set(this.getProteins() + other.getProteins() * weight);
        this.fats.set(this.getFats() + other.getFats() * weight);
        this.carbs.set(this.getCarbs() + other.getCarbs() * weight);
    }
}
