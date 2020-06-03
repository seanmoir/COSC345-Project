package com.app.boozespy;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

/**
 * Type class, used for data returned from outside websites
 *
 * @author Sean Moir
 */
public class Product {

    private String name;
    private String imgUrl;
    private String url;
    private double price;
    private String store;
    private Bitmap image;

    /**
     * Empty constructor, sets defaults ("", 0, null)
     */
    public Product() {
        this.name = "";
        this.price = 0.00;
        this.imgUrl = "";
        this.url = "";
        this.store = "";
        this.image = null;
    }

    /**
     * Constructor to initalise a non-empty products
     *
     * @param name   name of product
     * @param price  price of product
     * @param imgUrl URL to image of product
     * @param url    URL to product
     * @param store  stores name
     * @param image  bitmap image of product
     */
    public Product(String name, double price, String imgUrl, String url, String store, Bitmap image) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.imgUrl = imgUrl;
        this.store = store;
        this.image = image;
    }


    /* Getters and Setters */
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
    /* -------------------------------------------- */


    /**
     * Textual representation of product, image is missing for obvious reasons
     *
     * @return String that represent products properties
     */
    @NonNull
    @Override
    public String toString() {
        return "{ name=" + getName() + ", imgUrl=" + getImgUrl() + ", url=" + getUrl() + ", price="
                + getPrice() + ", store=" + getStore() + " }";
    }


}
