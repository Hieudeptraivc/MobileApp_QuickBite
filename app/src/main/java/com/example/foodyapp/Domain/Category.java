package com.example.foodyapp.Domain;

public class Category {
    private int Id;
    private String ImagePath;
    private String Name;

    public Category() {

    }
    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        this.ImagePath = imagePath;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }


}
