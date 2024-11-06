package org.example.courseproject.POJO;

import java.util.ArrayList;
import java.util.Date;

public class FoodPlan {
    int planID;
    int userID;
    Nutrients norm;
    ArrayList<Product> products;
    double[] weights;
    Date date;
    double totalCal;

    public FoodPlan(int planID, ArrayList<Product> prods, double[] weights, int userID, Date date, double totalCal) {
        this.planID = planID;
        this.userID = userID;
        this.products = prods;
        this.weights = weights;
        this.date = date;
        this.totalCal = totalCal;
    }

    public int getPlanID() {
        return planID;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Nutrients getNorm() { return norm; }

    public void setNorm(Nutrients norm) { this.norm = norm; }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalCal() {
        return totalCal;
    }

    public void setTotalCal(double totalCal) {
        this.totalCal = totalCal;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }
}
