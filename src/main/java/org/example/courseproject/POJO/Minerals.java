package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Minerals {
    private int mineralsID;
    private final DoubleProperty ca = new SimpleDoubleProperty();
    private final DoubleProperty fe = new SimpleDoubleProperty();
    private final DoubleProperty mg = new SimpleDoubleProperty();
    private final DoubleProperty zn = new SimpleDoubleProperty();
    private final DoubleProperty cu = new SimpleDoubleProperty();
    private final DoubleProperty se = new SimpleDoubleProperty();

    public Minerals() {
    }

    public Minerals(double ca, double fe, double mg, double zn, double cu, double se) {
        this.ca.set(ca);
        this.fe.set(fe);
        this.mg.set(mg);
        this.zn.set(zn);
        this.cu.set(cu);
        this.se.set(se);
    }

    public int getMineralsID() {
        return mineralsID;
    }

    public void setMineralsID(int mineralsID) {
        this.mineralsID = mineralsID;
    }

    public double getCa() {
        return ca.get();
    }

    public void setCa(double ca) {
        this.ca.set(ca);
    }

    public DoubleProperty caProperty() {
        return ca;
    }

    public double getFe() {
        return fe.get();
    }

    public void setFe(double fe) {
        this.fe.set(fe);
    }

    public DoubleProperty feProperty() {
        return fe;
    }

    public double getMg() {
        return mg.get();
    }

    public void setMg(double mg) {
        this.mg.set(mg);
    }

    public DoubleProperty mgProperty() {
        return mg;
    }

    public double getZn() {
        return zn.get();
    }

    public void setZn(double zn) {
        this.zn.set(zn);
    }

    public DoubleProperty znProperty() {
        return zn;
    }

    public double getCu() {
        return cu.get();
    }

    public void setCu(double cu) {
        this.cu.set(cu);
    }

    public DoubleProperty cuProperty() {
        return cu;
    }

    public double getSe() {
        return se.get();
    }

    public void setSe(double se) {
        this.se.set(se);
    }

    public DoubleProperty seProperty() {
        return se;
    }
}
