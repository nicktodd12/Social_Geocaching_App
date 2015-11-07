package com.team4.social_geocaching_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NearMe extends AppCompatActivity {
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
        setContentView(R.layout.activity_near_me);
        dbHelp = new DatabaseHelper(getApplicationContext());
        gC = dbHelp.selectGeocaches(0);
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
