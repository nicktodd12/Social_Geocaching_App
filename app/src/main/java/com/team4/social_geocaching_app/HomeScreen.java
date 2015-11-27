package com.team4.social_geocaching_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Class which creates and controls functionality of the Home Screen
 */
public class HomeScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //create listeners for all of the buttons on the screen
        Button btnMaps = (Button) findViewById(R.id.maps_button);
        btnMaps.setOnClickListener(this);
        Button btnNearMe = (Button) findViewById(R.id.near_me_button);
        btnNearMe.setOnClickListener(this);
        Button btnCreateCache = (Button) findViewById(R.id.create_cache_button);
        btnCreateCache.setOnClickListener(this);
        Button btnConnect = (Button) findViewById(R.id.connect_button);
        btnConnect.setOnClickListener(this);
        Button btnLeaderboards = (Button) findViewById(R.id.leaderboards_button);
        btnLeaderboards.setOnClickListener(this);
        Button btnAccount = (Button) findViewById(R.id.account_button);
        btnAccount.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.maps_button:
                //if the maps button is pressed create a bundle
                Bundle b = new Bundle();
                b.putString("previousScreen", "HomeScreen");
                //start the maps activity with the new bundle
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
                break;
            case R.id.near_me_button:
                //if near me is pressed start the near me activity
                startActivity(new Intent(this, NearMe.class));
                break;
            case R.id.create_cache_button:
                //if create geocache is pressed start the create geocache activity
                startActivity(new Intent(this, CreateGeocache.class));
                break;
            case R.id.connect_button:
                //if connect is pressed start the connect activity
                startActivity(new Intent(this, Connect.class));
                break;
            case R.id.leaderboards_button:
                //if leaderboards is pressed start the leaderboards activity
                startActivity(new Intent(this, Leaderboards.class));
                break;
            case R.id.account_button:
                //if account is pressed start the account activity
                startActivity(new Intent(this, MyAccount.class));
                break;

        }
    }
}
