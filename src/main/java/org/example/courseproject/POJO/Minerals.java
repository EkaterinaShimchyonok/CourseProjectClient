package org.example.courseproject.POJO;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Minerals")
public class Minerals {
    @XmlTransient
    private int mineralsID;
    private final DoubleProperty ca = new SimpleDoubleProperty();
    private final DoubleProperty fe = new SimpleDoubleProperty();
    private final DoubleProperty mg = new SimpleDoubleProperty();
    private final DoubleProperty zn = new SimpleDoubleProperty();
    private final DoubleProperty cu = new SimpleDoubleProperty();
    private final DoubleProperty se = new SimpleDoubleProperty();

    public Minerals() {
        ca.set(0.0);
        fe.set(0.0);
        mg.set(0.0);
        zn.set(0.0);
        cu.set(0.0);
        se.set(0.0);
    }

    public Minerals(double ca, double fe, double mg, double zn, double cu, double se) {
        this.ca.set(ca);
        this.fe.set(fe);
        this.mg.set(mg);
        this.zn.set(zn);
        this.cu.set(cu);
        this.se.set(se);
    }

    @XmlTransient
    public int getMineralsID() {
        return mineralsID;
    }

    public void setMineralsID(int mineralsID) {
        this.mineralsID = mineralsID;
    }

    @XmlElement
    public double getCa() {
        return ca.get();
    }

    public void setCa(double ca) {
        this.ca.set(ca);
    }

    public DoubleProperty caProperty() {
        return ca;
    }

    @XmlElement
    public double getFe() {
        return fe.get();
    }

    public void setFe(double fe) {
        this.fe.set(fe);
    }

    public DoubleProperty feProperty() {
        return fe;
    }

    @XmlElement
    public double getMg() {
        return mg.get();
    }

    public void setMg(double mg) {
        this.mg.set(mg);
    }

    public DoubleProperty mgProperty() {
        return mg;
    }

    @XmlElement
    public double getZn() {
        return zn.get();
    }

    public void setZn(double zn) {
        this.zn.set(zn);
    }

    public DoubleProperty znProperty() {
        return zn;
    }

    @XmlElement
    public double getCu() {
        return cu.get();
    }

    public void setCu(double cu) {
        this.cu.set(cu);
    }

    public DoubleProperty cuProperty() {
        return cu;
    }

    @XmlElement
    public double getSe() {
        return se.get();
    }

    public void setSe(double se) {
        this.se.set(se);
    }

    public DoubleProperty seProperty() {
        return se;
    }

    public void add(Minerals other, double weight) {
        this.ca.set(this.getCa() + other.getCa() * weight);
        this.fe.set(this.getFe() + other.getFe() * weight);
        this.mg.set(this.getMg() + other.getMg() * weight);
        this.zn.set(this.getZn() + other.getZn() * weight);
        this.cu.set(this.getCu() + other.getCu() * weight);
        this.se.set(this.getSe() + other.getSe() * weight);
    }
}
