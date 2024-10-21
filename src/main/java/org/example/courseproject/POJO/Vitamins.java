package org.example.courseproject.POJO;

public class Vitamins {
    int vitaminsID;
    double a;
    double d;
    double e;
    double k;
    double c;
    double b12;

    public Vitamins(int vitaminsID, double a, double d, double e, double c, double k, double b12) {
        this.vitaminsID = vitaminsID;
        this.a = a;
        this.d = d;
        this.e = e;
        this.k = k;
        this.c = c;
        this.b12 = b12;
    }

    public int getVitaminsID() {
        return vitaminsID;
    }

    public void setVitaminsID(int vitaminsID) {
        this.vitaminsID = vitaminsID;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getB12() {
        return b12;
    }

    public void setB12(double b12) {
        this.b12 = b12;
    }
}
