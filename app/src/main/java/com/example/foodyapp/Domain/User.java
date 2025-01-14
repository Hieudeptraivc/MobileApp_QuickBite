package com.example.foodyapp.Domain;

public class User {
    public String name;
    public String email;
    public String password;
    public String uid;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String profileImage;


    public User(String name, String email, String password, String uid,String profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
