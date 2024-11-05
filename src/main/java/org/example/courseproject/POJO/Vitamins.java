package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Vitamins {
    private int vitaminsID;
    private final DoubleProperty a = new SimpleDoubleProperty();
    private final DoubleProperty d = new SimpleDoubleProperty();
    private final DoubleProperty e = new SimpleDoubleProperty();
    private final DoubleProperty k = new SimpleDoubleProperty();
    private final DoubleProperty c = new SimpleDoubleProperty();
    private final DoubleProperty b12 = new SimpleDoubleProperty();

    public Vitamins() {
    }

    public Vitamins(int vitaminsID, double a, double d, double e, double c, double k, double b12) {
        this.vitaminsID = vitaminsID;
        this.a.set(a);
        this.d.set(d);
        this.e.set(e);
        this.k.set(k);
        this.c.set(c);
        this.b12.set(b12);
    }

    public int getVitaminsID() {
        return vitaminsID;
    }

    public void setVitaminsID(int vitaminsID) {
        this.vitaminsID = vitaminsID;
    }

    public double getA() {
        return a.get();
    }

    public void setA(double a) {
        this.a.set(a);
    }

    public DoubleProperty aProperty() {
        return a;
    }

    public double getD() {
        return d.get();
    }

    public void setD(double d) {
        this.d.set(d);
    }

    public DoubleProperty dProperty() {
        return d;
    }

    public double getE() {
        return e.get();
    }

    public void setE(double e) {
        this.e.set(e);
    }

    public DoubleProperty eProperty() {
        return e;
    }

    public double getK() {
        return k.get();
    }

    public void setK(double k) {
        this.k.set(k);
    }

    public DoubleProperty kProperty() {
        return k;
    }

    public double getC() {
        return c.get();
    }

    public void setC(double c) {
        this.c.set(c);
    }

    public DoubleProperty cProperty() {
        return c;
    }

    public double getB12() {
        return b12.get();
    }

    public void setB12(double b12) {
        this.b12.set(b12);
    }

    public DoubleProperty b12Property() {
        return b12;
    }
}
