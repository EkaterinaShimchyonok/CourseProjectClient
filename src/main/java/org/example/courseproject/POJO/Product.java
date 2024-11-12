package org.example.courseproject.POJO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Product")
public class Product {
    @XmlTransient
    private int productID;
    private String name;
    private boolean isCooked;
    private Category category;
    private String categoryName;
    private Nutrients nutrients;

    public Product(){
        nutrients = new Nutrients();
        category = new Category();
    }

    public Product(String name, boolean isCoocked, Category category, Nutrients nutrients) {
        this.name = name;
        this.isCooked = isCoocked;
        this.category = category;
        this.nutrients = nutrients;
    }

    @XmlTransient
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public boolean getIsCooked() {
        return isCooked;
    }

    public void setIsCooked(boolean coocked) {
        isCooked = coocked;
    }

    @XmlElement
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @XmlElement
    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    @XmlElement
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
