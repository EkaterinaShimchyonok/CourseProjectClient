package org.example.courseproject.POJO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Category")
public class Category {
    @XmlTransient
    private int categoryID;
    private String name;
    @XmlTransient
    private String image;

    public Category() {
        image = "products.png";
    }

    public Category(String name) {
        this.name = name;
        image = "products.png";
    }

    public Category(int categoryID, String name, String image) {
        this.categoryID = categoryID;
        this.name = name;
        this.image = image;
        if (image.equals(" ") || image.isEmpty()) {
            this.image = "products.png";
        }
    }

    @XmlTransient
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
