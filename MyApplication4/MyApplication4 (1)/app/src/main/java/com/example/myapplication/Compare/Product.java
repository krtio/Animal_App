package com.example.myapplication.Compare;

public class Product {
    private String title;
    private String imageUrl;
    private String price;
    private String mallName;
    private String link;

    public Product(String title, String imageUrl, String price, String mallName, String link) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.mallName = mallName;
        this.link = link;
    }

    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getPrice() { return price; }
    public String getMallName() { return mallName; }
    public String getLink() { return link; }
}
