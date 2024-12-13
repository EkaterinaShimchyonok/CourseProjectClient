package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Vitamins")
public class Vitamins {
    @XmlTransient
    private int vitaminsID;
    private final DoubleProperty a = new SimpleDoubleProperty();
    private final DoubleProperty d = new SimpleDoubleProperty();
    private final DoubleProperty e = new SimpleDoubleProperty();
    private final DoubleProperty k = new SimpleDoubleProperty();
    private final DoubleProperty c = new SimpleDoubleProperty();
    private final DoubleProperty b12 = new SimpleDoubleProperty();

    public Vitamins() {
        a.set(0.0);
        d.set(0.0);
        e.set(0.0);
        k.set(0.0);
        c.set(0.0);
        b12.set(0.0);
    }

    public Vitamins(double a, double d, double e, double c, double k, double b12) {
        this.a.set(a);
        this.d.set(d);
        this.e.set(e);
        this.k.set(k);
        this.c.set(c);
        this.b12.set(b12);
    }

    @XmlTransient
    public int getVitaminsID() {
        return vitaminsID;
    }

    public void setVitaminsID(int vitaminsID) {
        this.vitaminsID = vitaminsID;
    }

    @XmlElement
    public double getA() {
        return a.get();
    }

    public void setA(double a) {
        this.a.set(a);
    }

    public DoubleProperty aProperty() {
        return a;
    }

    @XmlElement
    public double getD() {
        return d.get();
    }

    public void setD(double d) {
        this.d.set(d);
    }

    public DoubleProperty dProperty() {
        return d;
    }

    @XmlElement
    public double getE() {
        return e.get();
    }

    public void setE(double e) {
        this.e.set(e);
    }

    public DoubleProperty eProperty() {
        return e;
    }

    @XmlElement
    public double getK() {
        return k.get();
    }

    public void setK(double k) {
        this.k.set(k);
    }

    public DoubleProperty kProperty() {
        return k;
    }

    @XmlElement
    public double getC() {
        return c.get();
    }

    public void setC(double c) {
        this.c.set(c);
    }

    public DoubleProperty cProperty() {
        return c;
    }

    @XmlElement
    public double getB12() {
        return b12.get();
    }

    public void setB12(double b12) {
        this.b12.set(b12);
    }

    public DoubleProperty b12Property() {
        return b12;
    }

    public void add(Vitamins other, double weight) {
        this.a.set(this.getA() + other.getA() * weight);
        this.d.set(this.getD() + other.getD() * weight);
        this.e.set(this.getE() + other.getE() * weight);
        this.k.set(this.getK() + other.getK() * weight);
        this.c.set(this.getC() + other.getC() * weight);
        this.b12.set(this.getB12() + other.getB12() * weight);
    }
}
