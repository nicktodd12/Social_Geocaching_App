package com.team4.social_geocaching_app;

/**
 * Created by Nick on 11/5/2015.
 */
public class Action {
    private String username;
    private String action;
    private int cacheNum;
    private String date;
    private int points;
    private String comment;

    public void setUsername(String username){
        this.username = username;
    }

    public void setAction(String action){
        this.action = action;
    }

    public void setCacheNum(int cacheNum){
        this.cacheNum = cacheNum;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPoints(int points){
        this.points = points;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getUsername(){
        return username;
    }

    public String getAction(){
        return action;
    }

    public int getCacheNum(){
        return cacheNum;
    }

    public String getDate(){
        return date;
    }

    public int getPoints(){
        return points;
    }

    public String getComment(){
        return comment;
    }
}
