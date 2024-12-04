package Models;

public class Product {
    String name;
    int quantity;
    int category;

    public Product(String name, int quantity, int category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory(){
        return this.category;
    }

}
