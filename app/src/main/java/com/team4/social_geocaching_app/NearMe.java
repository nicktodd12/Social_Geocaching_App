package com.team4.social_geocaching_app;

import android.location.Location;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class which creates and implements the functionality of the Near Me page
 */
public class NearMe extends AppCompatActivity {
    //variables used throughout the activity
    ListView connectActivities;
    ArrayList<RowItem> itemsList;
    List<Geocache> gC;
    Intent aboutCacheIntent;
    DatabaseHelper dbHelp;
    GPSTracker gps;
    List<Geocache> nearMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);
        //initialize the database helper and gps tracker
        dbHelp = new DatabaseHelper(this);
        gps = new GPSTracker(this);
        //get all of the geocaches
        gC = dbHelp.selectGeocaches(0);
        nearMe = new ArrayList<>();
        //try to get the user's location
        if(gps.canGetLocation()){
            Location myLoc = new Location("me");
            myLoc.setLatitude(gps.getLatitude());
            myLoc.setLongitude(gps.getLongitude());

            //find all of the geocaches within a specific distance
            for (Geocache g: gC) {
                Location geocache = new Location("cache");
                geocache.setLatitude(g.getLatitude());
                geocache.setLongitude(g.getLongitude());
                double distanceFromMe = (myLoc.distanceTo(geocache))*0.000621371;
                if(distanceFromMe<=1000000.0){
                    g.setDistanceFromMe(distanceFromMe);
                    nearMe.add(g);
                }
            }

            //sort the caches
            Collections.sort(nearMe, new NearMeComp());

        }else{
            //show the alert menu if the gps location cannout be determined
            gps.showSettingsAlert();
        }

        //create an item list and add the cache information to list
        itemsList = new ArrayList<>();
        aboutCacheIntent = new Intent(this,AboutGeocache.class);
        for(int k=0; k<nearMe.size(); k++){
            itemsList.add(k, new RowItem(nearMe.get(k).getCacheName(), Double.toString(nearMe.get(k).getDistanceFromMe())+" miles"));
        }
        //create a new list adapter to display the information
        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        connectActivities = (ListView) findViewById(R.id.nearMeList);
        connectActivities.setAdapter(currentAdapter);

        //create listeners for each item in the list
        connectActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();
                Geocache gC = nearMe.get(position);
                b.putString("Title", gC.getCacheName());
                b.putInt("CacheNum", gC.getCacheNum());
                b.putInt("TimesFound",gC.getPoints());
                b.putString("Description", gC.getDescription());
                b.putDouble("Latitude",gC.getLatitude());
                b.putDouble("Longitude", gC.getLongitude());
                b.putInt("Points", gC.getPoints());
                b.putByteArray("Image", gC.getImage());
                //start the about cache activity if a cache is clicked
                aboutCacheIntent.putExtras(b);
                startActivity(aboutCacheIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new__user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

/**
 * Comparator for use with geocache distances
 */
class NearMeComp implements Comparator<Geocache> {

    @Override
    public int compare(Geocache g1, Geocache g2) {
        if(g1.getDistanceFromMe() > g2.getDistanceFromMe()){
            return 1;
        } else {
            return -1;
        }
    }
}
