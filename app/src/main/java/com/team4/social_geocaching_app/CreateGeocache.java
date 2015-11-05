package com.team4.social_geocaching_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGeocache extends AppCompatActivity implements OnClickListener {

    Button submitGeocache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cache);
        submitGeocache = (Button)findViewById(R.id.submitCreateGeoCache);
        submitGeocache.setOnClickListener(this);
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
            case R.id.submitCreateGeoCache:
                EditText titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                EditText location = (EditText)findViewById(R.id.editLocation);
                EditText description = (EditText)findViewById(R.id.editDescription);
                if(createNewGeocache(titleBox.getText().toString(),location.getText().toString(),description.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Geocache " + titleBox.getText().toString() + " created!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, HomeScreen.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to create Geocache!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public boolean createNewGeocache(String title, String location, String description){
        //TODO: make query that adds geocache to database
        return true;
    }
}
