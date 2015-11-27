package com.team4.social_geocaching_app;

/**
 * Created by Johnny on 11/5/2015.
 * Represents a row item in one of the dynamic lists
 */
public class RowItem {
    //the two elements in a row item
    public String activityText;
    public String dateTime;

    public RowItem(String activityText, String dateTime){
        super();
        this.activityText = activityText;
        this.dateTime = dateTime;
    }
}
