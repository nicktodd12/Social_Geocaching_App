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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGeocache extends AppCompatActivity implements OnClickListener {

    Button submitGeocache;
    ImageButton mapImage;
    ImageButton geocacheImage;
    EditText titleBox;
    TextView latitude;
    TextView longitude;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
        description = (EditText)findViewById(R.id.editDescription);
        setContentView(R.layout.activity_create_cache);
        submitGeocache = (Button)findViewById(R.id.submitCreateGeoCache);
        submitGeocache.setOnClickListener(this);
        mapImage = (ImageButton)findViewById(R.id.mapImage);
        mapImage.setOnClickListener(this);
        geocacheImage = (ImageButton)findViewById(R.id.geocacheImage);
        geocacheImage.setOnClickListener(this);
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
        latitude = (TextView)findViewById(R.id.editLatitude);
        longitude = (TextView)findViewById(R.id.editLongitude);
        switch (v.getId()) {
            case R.id.submitCreateGeoCache:
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                latitude = (TextView)findViewById(R.id.editLatitude);
                longitude = (TextView)findViewById(R.id.editLongitude);
                if(createNewGeocache(titleBox.getText().toString(),latitude.getText().toString(),longitude.getText().toString(),description.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Geocache " + titleBox.getText().toString() + " created!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, HomeScreen.class));
                }else if(latitude.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(), "Click the map button!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to create Geocache!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.geocacheImage:
                //TODO: launch app to take photo of geocache, set it to this button
                Toast.makeText(getApplicationContext(), "Take a geocache Image!", Toast.LENGTH_LONG).show();
                break;
            case R.id.mapImage:
                //TODO: launch google maps, allow user to find geocache and place marker
                Toast.makeText(getApplicationContext(), "This should launch map!", Toast.LENGTH_LONG).show();
                latitude.setText("39.995363");
                longitude.setText("-83.002591");
                break;
        }
    }

    public static boolean createNewGeocache(String title, String latitude, String longitude, String description){
        //TODO: make query that adds geocache to database
        return true;
    }
}
