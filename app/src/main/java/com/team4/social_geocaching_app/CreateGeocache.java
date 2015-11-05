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
    EditText points;
    private DatabaseHelper dbHelp;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.dbHelp = new DatabaseHelper(this);
        currentUsername = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
        super.onCreate(savedInstanceState);
        titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
        description = (EditText)findViewById(R.id.editDescription);
        points = (EditText)findViewById(R.id.editGeocachePoints);
        setContentView(R.layout.activity_create_cache);
        submitGeocache = (Button)findViewById(R.id.submitCreateGeoCache);
        submitGeocache.setOnClickListener(this);
        mapImage = (ImageButton)findViewById(R.id.mapImage);
        mapImage.setOnClickListener(this);
        geocacheImage = (ImageButton)findViewById(R.id.geocacheImage);
        geocacheImage.setOnClickListener(this);
        latitude = (TextView)findViewById(R.id.editLatitude);
        longitude = (TextView)findViewById(R.id.editLongitude);
        Bundle b = this.getIntent().getExtras();
        if(b!=null ){
            if(b.containsKey("Latitude") && b.containsKey("Longitude")){
                latitude.setText( b.get("Latitude").toString());
                longitude.setText( b.get("Longitude").toString());
            }
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
//        latitude = (TextView)findViewById(R.id.editLatitude);
//        longitude = (TextView)findViewById(R.id.editLongitude);
        switch (v.getId()) {
            case R.id.submitCreateGeoCache:
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                latitude = (TextView)findViewById(R.id.editLatitude);
                longitude = (TextView)findViewById(R.id.editLongitude);
                points = (EditText)findViewById(R.id.editGeocachePoints);
                if(latitude.getText().toString().length() > 0 && createNewGeocache(titleBox.getText().toString(), points.getText().toString(), latitude.getText().toString(),longitude.getText().toString(),description.getText().toString())){
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
                Toast.makeText(getApplicationContext(), "Place your geocache somewhere!", Toast.LENGTH_LONG).show();
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                Bundle b = new Bundle();
                b.putString("previousScreen", "CreateGeocache");
                b.putString("Title", titleBox.getText().toString());
                b.putString("Description", description.getText().toString());
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
//                latitude.setText("20.995363");
//                longitude.setText("-20.002591");
                break;
        }
    }

    public boolean createNewGeocache(String title, String points, String latitude, String longitude, String description){
        if(latitude.equals("(Latitude)")|| longitude.equals("Longitude")){
            Toast.makeText(getApplicationContext(), "Click the map icon to get coordinates!", Toast.LENGTH_LONG).show();
            return false;
        }else if(title.length() == 0){
            Toast.makeText(getApplicationContext(), "Enter a title for your Geocache!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!(points.matches("^-?\\d+$")&&Integer.parseInt(points)>0 && Integer.parseInt(points)<21)){
            Toast.makeText(getApplicationContext(), "Enter a positive int value for points between 1 and 20!", Toast.LENGTH_LONG).show();
            return false;
        }
        dbHelp.insertGeocache(currentUsername, Integer.parseInt(points), Double.parseDouble(latitude), Double.parseDouble(longitude), title, description);
        return true;
    }
}
