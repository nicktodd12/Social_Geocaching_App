package com.team4.social_geocaching_app;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
Class which handles all functionality on the About Goecache page of the application.
Contains methods for fetching database info about the requested cache and displaying it
on the screen.
 */
public class AboutGeocache extends AppCompatActivity implements View.OnClickListener{

    //Variables needed throughout the page
    Bundle b;
    int cN;
    DatabaseHelper dbHelp;
    Button checkInButton;
    TextView TimesFound;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the page initial state
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_geocache);

        //get the starting intent and all values needed to display the page correctly
        b = getIntent().getExtras();

        TextView GeocacheTitle = (TextView) findViewById(R.id.geocacheTitle);
        TimesFound = (TextView) findViewById(R.id.timesFound);
        TextView Description = (TextView) findViewById(R.id.textDescription);
        TextView Points = (TextView) findViewById(R.id.pointValue);
        TextView CreatedBy = (TextView) findViewById(R.id.createdByTitle);
        TextView Date = (TextView) findViewById(R.id.createdBy);
        TextView Latitude = (TextView) findViewById(R.id.editLatitude);
        TextView Longitude = (TextView) findViewById(R.id.editLongitude);

        //get the chacheNum which is required by the database
        cN = (Integer)b.get("CacheNum");

        //get the latitude and longitude which are required by the database
        Description.setText((String) b.get("Description"));
        GeocacheTitle.setText((String) b.get("Title"));
        if(b.get("Latitude") != null){
            Latitude.setText(Double.toString((Double)b.get("Latitude")));
            latitude = b.getDouble("Latitude");
        }

        if(b.get("Longitude") != null){
            Longitude.setText(Double.toString((Double)b.get("Longitude")));
            longitude = b.getDouble("Longitude");
        }

        //initialize the database helper and get the required geocache
        dbHelp = new DatabaseHelper(this);
        List<Geocache> gC = dbHelp.selectGeocaches(cN);

        //set the textviews to the values returned from the database
        CreatedBy.append(gC.get(0).getUsername());
        Date.setText(gC.get(0).getDate());
        TimesFound.setText(Integer.toString(dbHelp.selectActions("",cN).size()-1));
        int pointVal = gC.get(0).getPoints();
        if(b.get("Points") != null && pointVal > 0){
            Points.setText(Integer.toString(pointVal));
        }

        //create reference to the image buttons/displays
        ImageButton imageButton = (ImageButton) findViewById(R.id.geocacheImageButton);
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImage);
        checkInButton = (Button) findViewById(R.id.checkInButton);
        //set images and create listeners
        byte[] image = (byte[]) b.get("Image");
        if(b.get("Image") != null){
            imageButton.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
        imageButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        //set listener for the check in button
        checkInButton.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        TimesFound.setText(Integer.toString(dbHelp.selectActions("", cN).size() - 1));
        //check to make sure the username is in the shared preferences
        if(dbHelp.selectActions(getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken"),cN).size()!=0){
            checkInButton.setText("Can't check in here...");
            checkInButton.setEnabled(false);
        }
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
            case R.id.geocacheImageButton:
                //Does nothing on this page
                break;
            case R.id.mapImage:
                //set variables needed for the map screen
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                b.putDouble("latitude", latitude);
                b.putDouble("longitude", longitude);
                //start the map and pass the bundle
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
                break;
            case R.id.checkInButton:
                //set variables needed for found geocache screen
                Bundle bz = new Bundle();
                bz.putInt("CacheNum", cN);
                //start the found page and pass the bundle
                Intent foundGeoCacheIntent = new Intent(this, FoundGeocache.class);
                foundGeoCacheIntent.putExtras(bz);
                startActivity(foundGeoCacheIntent);
                break;
        }
    }
}
