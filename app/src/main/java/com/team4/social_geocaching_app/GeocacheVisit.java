package com.team4.social_geocaching_app;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Class which creates and controls functionality of the Geocache Visit page
 */
public class GeocacheVisit extends AppCompatActivity implements View.OnClickListener{

    //The geocache and activity information being viewed on the screen
    Geocache currentGeocache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create the display
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocache_visit);
        //get the passed bundle from the intent
        Bundle b = getIntent().getExtras();
        int cacheNum = (b.getInt("CacheNum"));
        String username = (b.getString("Username"));
        //create a database helper and run a query to get the database/action information
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        Action currentAction = dbHelp.selectActions(username, cacheNum).get(0);
        currentGeocache = dbHelp.selectGeocaches(cacheNum).get(0);

        //find all textviews to set correct values from queries
        TextView title = (TextView) findViewById(R.id.checkInTitle);
        TextView createdBy = (TextView) findViewById(R.id.createdBy);
        TextView date = (TextView) findViewById(R.id.dateFound);
        TextView points = (TextView) findViewById(R.id.GeocachePoints);
        TextView comment = (TextView) findViewById(R.id.commentBox);
        TextView latitude = (TextView) findViewById(R.id.editLatitude);
        TextView longitude = (TextView) findViewById(R.id.editLongitude);
        //set text values
        title.setText(currentGeocache.getCacheName());
        latitude.setText(Double.toString(currentGeocache.getLatitude()));
        longitude.setText(Double.toString(currentGeocache.getLongitude()));
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImage);
        //set a listener to the google maps button
        mapButton.setOnClickListener(this);
        if(username != null){
            createdBy.append(username);
        }
        date.append(currentAction.getDate());
        points.append(Integer.toString(currentGeocache.getPoints()));
        comment.setText(currentAction.getComment());

        //place the image for the action on the screen
        ImageButton cacheImage = (ImageButton) findViewById(R.id.geocacheVisitImage);
        byte[] image = currentAction.getImage();
        if(image != null && image.length != 0){
            cacheImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geocache_visit, menu);
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
            case R.id.mapImage:
                //if the user clicks the map icon
                //create a bundle with the cache title and location
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                b.putDouble("latitude", currentGeocache.getLatitude());
                b.putDouble("longitude", currentGeocache.getLatitude());
                //start the map activity
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
                break;
        }
    }
}
