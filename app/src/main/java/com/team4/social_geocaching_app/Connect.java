package com.team4.social_geocaching_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Connect extends AppCompatActivity implements View.OnClickListener {
    //Variables used throughout the activity
    ListView connectActivities;
    ArrayList<RowItem> itemsList;
    List<Action> actionsList;
    List<Geocache> gC;
    DatabaseHelper dbHelp;
    Intent aboutCacheIntent;
    Intent viewCacheIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view and initialize variables
        setContentView(R.layout.activity_connect);
        itemsList = new ArrayList<>();
        dbHelp = new DatabaseHelper(getApplicationContext());
        //dbHelp.fillAppWithData();
        //get all most recent database actions
        actionsList = dbHelp.selectActions("", 0);
        aboutCacheIntent = new Intent(this,AboutGeocache.class);
        viewCacheIntent = new Intent(this,GeocacheVisit.class);

        //create local variables used in dynamic list
        String username, date, action;
        int cacheNum, points;

        //create a list of rowitems to dynamically display all recent actions performed
        for(int k = 0; k<actionsList.size(); k++){
            username = actionsList.get(k).getUsername();
            date = actionsList.get(k).getDate();
            action = actionsList.get(k).getAction();
            cacheNum = actionsList.get(k).getCacheNum();
            gC = dbHelp.selectGeocaches(cacheNum);
            if (action.equals("created")||action.equals("create")) {
                itemsList.add(k,new RowItem(username+" created "+gC.get(0).getCacheName(), date));
            }else{
                points = actionsList.get(k).getPoints();
                itemsList.add(k,new RowItem(username+" found "+gC.get(0).getCacheName()+" \n("+points+" points)",date));
            }


        }
        //create a list adapter to display the information on the screen
        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        connectActivities = (ListView) findViewById(R.id.connectList);
        connectActivities.setAdapter(currentAdapter);
        //set listeners for each item in the list to redirect to that cache's about page
        connectActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();
                gC = dbHelp.selectGeocaches(actionsList.get(position).getCacheNum());
                //put appropriate info in the bundle and start the correct intent
                if(actionsList.get(position).getAction().equals("created")||actionsList.get(position).getAction().equals("created"))
                {
                    //if the cache has been created then view the about cache page
                    b.putString("Title", gC.get(0).getCacheName());
                    b.putInt("CacheNum", gC.get(0).getCacheNum());
                    b.putInt("TimesFound", gC.get(0).getPoints());
                    b.putString("Description", gC.get(0).getDescription());
                    b.putDouble("Latitude", gC.get(0).getLatitude());
                    b.putDouble("Longitude", gC.get(0).getLongitude());
                    b.putInt("Points", gC.get(0).getPoints());
                    b.putByteArray("Image", gC.get(0).getImage());
                    aboutCacheIntent.putExtras(b);
                    startActivity(aboutCacheIntent);
                }else{
                    //if the cache has been visited then vist the cache visit page
                    b.putInt("CacheNum", gC.get(0).getCacheNum());
                    b.putString("Username", actionsList.get(position).getUsername());
                    viewCacheIntent.putExtras(b);
                    startActivity(viewCacheIntent);
                }
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectList:
                break;
        }
    }
}
