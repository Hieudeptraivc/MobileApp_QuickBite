package com.example.foodyapp.Domain;

import java.io.Serializable;

public class Foods implements Serializable {
    private static final long serialVersionUID = 1L;

    private int CategoryId;
    private String Description;
    private int Id;
    private String ImagePath;
    private double Price;
    private double Star;
    private String Title;
    private int numberInCart;
    private int TimeValue;
    private boolean Favorite;

    public Foods(int categoryId, String description, int id, String imagePath, double price, double star, String title, int numberInCart, int timeValue, boolean favorite) {
        CategoryId = categoryId;
        Description = description;
        Id = id;
        ImagePath = imagePath;
        Price = price;
        Star = star;
        Title = title;
        this.numberInCart = numberInCart;
        TimeValue = timeValue;
        Favorite = favorite;
    }

    public Foods() {
    }

    public int getTimeValue() {
        return TimeValue;
    }

    public void setTimeValue(int timeValue) {
        TimeValue = timeValue;
    }

    @Override
    public String toString() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isFavorite() {
        return Favorite;
    }

    public void setFavorite(boolean favorite) {
        Favorite = favorite;
    }
}
