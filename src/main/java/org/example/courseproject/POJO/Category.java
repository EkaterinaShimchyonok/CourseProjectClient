package org.example.courseproject.POJO;

public class Category {
    int categoryID;
    String name;
    String image;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(int categoryID, String name, String image) {
        this.categoryID = categoryID;
        this.name = name;
        this.image = image;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
