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


    /**
     * Constructor to partially initalise a non-empty products (excluding empty bitmap)
     *
     * @param name   name of product
     * @param price  price of product
     * @param imgUrl URL to image of product
     * @param url    URL to product
     * @param store  stores name
     */
    public Product(String name, double price, String imgUrl, String url, String store) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.imgUrl = imgUrl;
        this.store = store;
    }

    /**
     * Retrives name of Product
     *
     * @return name of product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of Product
     *
     * @param name updated name of product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrives image URL of Product
     *
     * @return image URL of product
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Sets image URL of Product
     *
     * @param imgUrl image URL of product
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Retrives URL of Product
     *
     * @return URL of product
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets URL of Product
     *
     * @param url name of product
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retrives price of Product
     *
     * @return price of product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price of Product
     *
     * @return price of product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrives store of Product
     *
     * @return store of product
     */
    public String getStore() {
        return store;
    }

    /**
     * Sets store of Product
     *
     * @param store store of product
     */
    public void setStore(String store) {
        this.store = store;
    }

    /**
     * Retrives image of Product
     *
     * @return image of product
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Sets image of Product
     *
     * @param image image of product
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }


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
