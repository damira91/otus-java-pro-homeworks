package ru.otus.kudaiberdieva.homework10.application.entity;

public class Product {
    private long id;
    private String title;
    private double price;

    public Product(long id, String title, double price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(){
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice() {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                '}';
    }
}
