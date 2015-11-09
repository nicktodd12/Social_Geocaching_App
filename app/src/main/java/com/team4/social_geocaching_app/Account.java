package com.team4.social_geocaching_app;

/**
 * Created by Nick on 11/6/2015.
 */
public class Account {
    private String username;
    private int points;
    private byte[] image;

    public void setUsername(String username){
        this.username=username;
    }

    public void setPoints(int points){
        this.points=points;
    }

    public void setImage(byte[] image){ this.image = image;}

    public String getUsername(){
        return username;
    }

    public int getPoints(){
        return points;
    }

    public byte[] getImage() {return image;}
}
