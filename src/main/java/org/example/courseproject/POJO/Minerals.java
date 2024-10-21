package org.example.courseproject.POJO;

public class Minerals {
    int mineralsID;
    double ca;
    double fe;
    double mg;
    double zn;
    double cu;
    double se;

    public Minerals(int id, double ca, double fe, double mg, double zn, double cu, double se) {
        this.mineralsID = id;
        this.ca = ca;
        this.fe = fe;
        this.mg = mg;
        this.zn = zn;
        this.cu = cu;
        this.se = se;
    }

    public int getMineralsID() {
        return mineralsID;
    }

    public void setMineralsID(int mineralsID) {
        this.mineralsID = mineralsID;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public double getFe() {
        return fe;
    }

    public void setFe(double fe) {
        this.fe = fe;
    }

    public double getMg() {
        return mg;
    }

    public void setMg(double mg) {
        this.mg = mg;
    }

    public double getZn() {
        return zn;
    }

    public void setZn(double zn) {
        this.zn = zn;
    }

    public double getCu() {
        return cu;
    }

    public void setCu(double cu) {
        this.cu = cu;
    }

    public double getSe() {
        return se;
    }

    public void setSe(double se) {
        this.se = se;
    }
}
