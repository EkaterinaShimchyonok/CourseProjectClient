package org.example.courseproject.POJO;

import java.util.ArrayList;
import java.util.Date;

public class FoodPlan {
    int planID;
    ArrayList<Product> products;
    double[] weights;
    int userID;
    User user;
    Date date;
    double totalCal;

    public FoodPlan(int planID, int userID, Date date, double totalCal) {
        this.planID = planID;
        this.userID = userID;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
