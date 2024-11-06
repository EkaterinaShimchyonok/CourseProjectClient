package org.example.courseproject.POJO;

public class Product {
    int productID;
    String name;
    boolean isCooked;
    Category category;
    String categoryName;
    Nutrients nutrients;

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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsCooked() {
        return isCooked;
    }

    public void setIsCooked(boolean coocked) {
        isCooked = coocked;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
