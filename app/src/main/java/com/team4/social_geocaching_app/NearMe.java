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

public class NearMe extends AppCompatActivity {
    ListView connectActivities;
    ArrayList<RowItem> itemsList;
    List<Geocache> gC;
    Intent aboutCacheIntent;
    DatabaseHelper dbHelp;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);
        dbHelp = new DatabaseHelper(this);
        gps = new GPSTracker(this);
        gC = dbHelp.selectGeocaches(0);
        List<Geocache> nearMe = new ArrayList<>();
        if(gps.canGetLocation()){
            Location myLoc = new Location("me");
            myLoc.setLatitude(gps.getLatitude());
            myLoc.setLongitude(gps.getLongitude());

            for (Geocache g: gC) {
                Location geocache = new Location("cache");
                geocache.setLatitude(g.getLatitude());
                geocache.setLongitude(g.getLongitude());
                double distanceFromMe = (myLoc.distanceTo(geocache))*0.000621371;
                if(distanceFromMe<=25.0){
                    g.setDistanceFromMe(distanceFromMe);
                    nearMe.add(g);
                }
            }

            Collections.sort(nearMe, new NearMeComp());

            for (Geocache g: nearMe) {
                Log.d("Near", g.getCacheName() + " " + g.getDistanceFromMe());
            }
        }else{
            gps.showSettingsAlert();
        }

        itemsList = new ArrayList<>();
        aboutCacheIntent = new Intent(this,AboutGeocache.class);
        for(int k=0; k<gC.size(); k++){
            itemsList.add(k, new RowItem(gC.get(k).getCacheName(), k+" miles"));
        }
        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        connectActivities = (ListView) findViewById(R.id.nearMeList);
        connectActivities.setAdapter(currentAdapter);

        connectActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();

                b.putString("Title", gC.get(position).getCacheName());
                b.putInt("CacheNum", gC.get(position).getCacheNum());
                b.putInt("TimesFound", gC.get(position).getPoints());
                b.putString("Description", gC.get(position).getDescription());
                b.putDouble("Latitude", gC.get(position).getLatitude());
                b.putDouble("Longitude", gC.get(position).getLongitude());
                b.putInt("Points", gC.get(position).getPoints());
                b.putByteArray("Image", gC.get(position).getImage());
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
