package com.team4.social_geocaching_app;

/**
 * Created by Nick on 11/4/2015.
 * Class which represents a geocache in the database
 */
public class Geocache {
    /*
     * contains a cache number, an owner, a point value for finding it, a latitude and longitude,
     * a name, a data of creation, a description of the cache, an image, and a distance from the user
     */

    private int cacheNum;
    private String username;
    private int points;
    private double latitude;
    private double longitude;
    private String cacheName;
    private String date;
    private String description;
    private byte[] image;
    private double distanceFromMe;

    //getters and setters for all of the class variables

    public int getCacheNum(){
        return cacheNum;
    }

    public String getUsername(){
        return username;
    }

    public int getPoints(){
        return points;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getCacheName(){return cacheName;}

    public String getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public byte[] getImage() { return image;}

    public double getDistanceFromMe(){
        return (double)Math.round(distanceFromMe * 100d) / 100d;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setCacheName(String cacheName){this.cacheName=cacheName;}

    public void setDate(String date){this.date=date;}

    public void setDescription(String description){
        this.description=description;
    }

    public void setImage(byte[] image){this.image = image;}

    public void setCachenum(int cachNum){
        this.cacheNum = cachNum;
    }

    public void setDistanceFromMe(double distanceFromMe){
        this.distanceFromMe = distanceFromMe;
    }
}
