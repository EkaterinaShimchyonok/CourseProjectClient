package org.example.courseproject.POJO;

public class UserInfo {
    int uinfoID;
    String name;
    int age;
    boolean male;
    double height;
    double weight;
    int activity_level;
    double goal;
    Nutrients norm;

    public int getUinfoID() {
        return uinfoID;
    }

    public void setUinfoID(int uinfoID) {
        this.uinfoID = uinfoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(int activity_level) {
        this.activity_level = activity_level;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public Nutrients getNorm() {
        return norm;
    }

    public void setNorm(Nutrients norm) {
        this.norm = norm;
    }
}
