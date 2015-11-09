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

public class GeocacheVisit extends AppCompatActivity implements View.OnClickListener{

    Geocache currentGeocache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocache_visit);
        Bundle b = getIntent().getExtras();
        int cacheNum = (b.getInt("CacheNum"));
        String username = (b.getString("Username"));
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        Action currentAction = dbHelp.selectActions(username, cacheNum).get(0);
        currentGeocache = dbHelp.selectGeocaches(cacheNum).get(0);

        TextView title = (TextView) findViewById(R.id.checkInTitle);
        TextView createdBy = (TextView) findViewById(R.id.createdBy);
        TextView date = (TextView) findViewById(R.id.dateFound);
        TextView points = (TextView) findViewById(R.id.GeocachePoints);
        TextView comment = (TextView) findViewById(R.id.commentBox);
        TextView latitude = (TextView) findViewById(R.id.editLatitude);
        TextView longitude = (TextView) findViewById(R.id.editLongitude);
        title.setText(currentGeocache.getCacheName());
        latitude.setText(Double.toString(currentGeocache.getLatitude()));
        longitude.setText(Double.toString(currentGeocache.getLongitude()));
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImage);
        mapButton.setOnClickListener(this);
        if(username != null){
            createdBy.append(username);
        }
        date.append(currentAction.getDate());
        points.append(Integer.toString(currentGeocache.getPoints()));
        comment.setText(currentAction.getComment());

        ImageButton cacheImage = (ImageButton) findViewById(R.id.geocacheVisitImage);
        //TODO: replace with image from action
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
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                b.putDouble("latitude", currentGeocache.getLatitude());
                b.putDouble("longitude", currentGeocache.getLatitude());
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
                break;
        }
    }
}
