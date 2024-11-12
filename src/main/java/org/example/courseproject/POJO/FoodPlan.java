package org.example.courseproject.POJO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "FoodPlan")
public class FoodPlan {
    @XmlTransient
    private int planID;
    @XmlTransient
    private int userID;
    private Nutrients norm;
    private ArrayList<Product> products;
    private ArrayList<Double> weights;
    private String date;
    private double totalCal;

    public FoodPlan() {
        norm = new Nutrients();
        products = new ArrayList<>();
        weights = new ArrayList<>();
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        date = formatter.format(today);
    }

    @XmlTransient
    public int getPlanID() {
        return planID;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }

    @XmlTransient
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @XmlElement
    public Nutrients getNorm() {
        return norm;
    }

    public void setNorm(Nutrients norm) {
        this.norm = norm;
    }

    @XmlElement
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlElement
    public double getTotalCal() {
        return totalCal;
    }

    public void setTotalCal(double totalCal) {
        this.totalCal = totalCal;
    }

    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "Product")
    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @XmlElementWrapper(name = "Weights")
    @XmlElement(name = "Weight")
    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }
}
