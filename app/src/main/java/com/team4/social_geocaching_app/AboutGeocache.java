package com.team4.social_geocaching_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AboutGeocache extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_geocache);

        Bundle b = getIntent().getExtras();

        TextView GeocacheTitle = (TextView) findViewById(R.id.geocacheTitle);
        TextView TimesFound = (TextView) findViewById(R.id.timesFound);
        TextView Description = (TextView) findViewById(R.id.textDescription);
        TextView Points = (TextView) findViewById(R.id.pointValue);
        TextView CreatedBy = (TextView) findViewById(R.id.createdByTitle);
        TextView Date = (TextView) findViewById(R.id.createdBy);
        TextView Latitude = (TextView) findViewById(R.id.editLatitude);
        TextView Longitude = (TextView) findViewById(R.id.editLongitude);

        Description.setText((String) b.get("Description"));
        GeocacheTitle.setText((String) b.get("Title"));
        if(b.get("Latitude") != null){
            Latitude.setText(Double.toString((Double)b.get("Latitude")));
        }

        if(b.get("Latitude") != null){
            Longitude.setText(Double.toString((Double)b.get("Longitude")));
        }
        
        String created = (String)b.get("User");
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        List<Action> currentGCAction = dbHelp.selectActions("",(Integer)b.get("CacheNum"));
        CreatedBy.append(currentGCAction.get(0).getUsername());
        Date.setText(currentGCAction.get(0).getDate());

        /*for(int k=0; k<currentGCAction.size(); k++){
            if(pref.getString(Integer.toString(currentGCAction.get(k).getCacheNum()),"OOPS").equals(b.get("Title"))){
               List<Geocache> currentGC = dbHelp.selectGeocaches(currentGCAction.get(k).getCacheNum());
                if(currentGC.size() == 1){
                    CreatedBy.setText(currentGC.get(0).getDate());
                }else{
                    Toast.makeText(getApplicationContext(), "SOMETHIN WRONG", Toast.LENGTH_LONG).show();
                }

            }
        }*/
        int timesFound = (Integer)b.get("TimesFound");
        if(b.get("TimesFound") != null && timesFound > -1){
            TimesFound.setText(Integer.toString(timesFound));
        }

        int pointVal = (Integer)b.get("Points");
        if(b.get("Points") != null && pointVal > 0){
            Points.setText(Integer.toString(pointVal));
        }


        ImageButton imageButton = (ImageButton) findViewById(R.id.geocacheImageButton);
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImage);
        Button checkInButton = (Button) findViewById(R.id.checkInButton);
        imageButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        checkInButton.setOnClickListener(this);
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
//        latitude = (TextView)findViewById(R.id.editLatitude);
//        longitude = (TextView)findViewById(R.id.editLongitude);
        switch (v.getId()) {
            case R.id.geocacheImageButton:
                //TODO: launch app to take photo of geocache, set it to this button
                break;
            case R.id.mapImage:
                //TODO: launch google maps, show geocache on map, dont allow movement or zooming or placing
                /*
                Toast.makeText(getApplicationContext(), "Place your geocache somewhere!", Toast.LENGTH_LONG).show();
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivityForResult(mapIntent, 0);*/
                break;
            case R.id.checkInButton:
                //TODO: launch Found geocache activity
                startActivity(new Intent(this, FoundGeocache.class));
                break;
        }
    }
}
