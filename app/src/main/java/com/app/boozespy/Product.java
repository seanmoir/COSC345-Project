package com.app.boozespy;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private String imgUrl;
    private String url;
    private double price;
    private String store;
    private Bitmap image;

    public Product() {
        setName("");
        setPrice(0.00);
        setImgUrl("");
        setUrl("");
    }

    public Product(String name, double price, String imgUrl, String url) {
        setName(name);
        setPrice(price);
        setUrl(url);
        setImgUrl(imgUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{ name=" + getName() + ", imgUrl=" + getImgUrl() + ", url=" + getUrl() + ", price="
                + getPrice() + ", store=" + getStore() + " }";
    }


}
