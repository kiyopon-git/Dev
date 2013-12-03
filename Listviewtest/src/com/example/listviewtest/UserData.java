package com.example.listviewtest;

import android.graphics.Bitmap;

public class UserData {
    private Bitmap image;
    private String name;
    private String date;
    private String introduction;
 
    public void setImage(Bitmap image) {
        this.image = image;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
 
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    
    public Bitmap getImage() {
        return image;
    }
 
    public String getName() {
        return name;
    }
    
    public String getDate() {
        return date;
    }
 
    public String getIntroduction() {
        return introduction;
    }
}