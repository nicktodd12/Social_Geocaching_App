package com.team4.social_geocaching_app;

/**
 * Created by Nick on 11/6/2015.
 * Class which respresents the information of a user account.
 */
public class Account {
    //a user is represented by their name, points, and image
    private String username;
    private int points;
    private byte[] image;

    //create getters and setters for all of the variables above

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
