package main.java;

public class Product {
    private String productid, name, imageFile;
    private double price;

    /* constructor for product class*/
    public Product(String productid, String name, String imageFile, double price){
        this.productid=productid;
        this.name=name;
        this.imageFile=imageFile;
        this.price=price;
    }

    /* getter methods */
    public String getId(){return productid;}
    public String getName(){return name;}
    public String getImageFile(){return imageFile;}
    public double getPrice(){return price;}
}
