package com.team4.social_geocaching_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener{
    //TODO: create HomeScreen functionality, Gradle
    //BIG NERD RANCH DRESSING!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
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
                startActivity(new Intent(this, Map.class));
                break;
            case R.id.near_me_button:
                startActivity(new Intent(this, NearMe.class));
                break;
            case R.id.create_cache_button:
                startActivity(new Intent(this, CreateGeocache.class));
                break;
            case R.id.connect_button:
                startActivity(new Intent(this, Connect.class));
                break;
            case R.id.leaderboards_button:
                startActivity(new Intent(this, Leaderboards.class));
                break;
            case R.id.account_button:
                startActivity(new Intent(this, MyAccount.class));
                break;

        }
    }
}
