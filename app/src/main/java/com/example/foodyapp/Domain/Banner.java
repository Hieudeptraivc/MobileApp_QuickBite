package com.example.foodyapp.Domain;

public class Banner {

    private String url;

    public Banner(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
