package com.team4.social_geocaching_app;

import android.content.DialogInterface;
import android.util.Log;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AboutGeocache extends AppCompatActivity implements View.OnClickListener{

    Bundle b;
    int cN;
    DatabaseHelper dbHelp;
    Button checkInButton;
    TextView TimesFound;
    double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_geocache);

        b = getIntent().getExtras();

        TextView GeocacheTitle = (TextView) findViewById(R.id.geocacheTitle);
        TimesFound = (TextView) findViewById(R.id.timesFound);
        TextView Description = (TextView) findViewById(R.id.textDescription);
        TextView Points = (TextView) findViewById(R.id.pointValue);
        TextView CreatedBy = (TextView) findViewById(R.id.createdByTitle);
        TextView Date = (TextView) findViewById(R.id.createdBy);
        TextView Latitude = (TextView) findViewById(R.id.editLatitude);
        TextView Longitude = (TextView) findViewById(R.id.editLongitude);

        cN = (Integer)b.get("CacheNum");

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

        String created = (String)b.get("User");
        dbHelp = new DatabaseHelper(this);
        //List<Action> currentGCAction = dbHelp.selectActions("",(Integer)b.get("CacheNum"));
        List<Geocache> gC = dbHelp.selectGeocaches(cN);
        CreatedBy.append(gC.get(0).getUsername());
        Date.setText(gC.get(0).getDate());

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
        TimesFound.setText(Integer.toString(dbHelp.selectActions("",cN).size()-1));

        int pointVal = gC.get(0).getPoints();
        if(b.get("Points") != null && pointVal > 0){
            Points.setText(Integer.toString(pointVal));
        }




        ImageButton imageButton = (ImageButton) findViewById(R.id.geocacheImageButton);
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapImage);
        checkInButton = (Button) findViewById(R.id.checkInButton);
        byte[] image = (byte[]) b.get("Image");
        if(b.get("Image") != null){
            imageButton.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
        imageButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        checkInButton.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        TimesFound.setText(Integer.toString(dbHelp.selectActions("", cN).size() - 1));
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
//        latitude = (TextView)findViewById(R.id.editLatitude);
//        longitude = (TextView)findViewById(R.id.editLongitude);
        switch (v.getId()) {
            case R.id.geocacheImageButton:
                //TODO: launch app to take photo of geocache, set it to this button
                break;
            case R.id.mapImage:
                //TODO: launch google maps, show geocache on map, dont allow movement or zooming or placing
                //Toast.makeText(getApplicationContext(), "Place your geocache somewhere!", Toast.LENGTH_LONG).show();
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                b.putDouble("latitude", latitude);
                b.putDouble("longitude", longitude);
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivity(mapIntent);
                break;
            case R.id.checkInButton:
                //TODO: launch Found geocache activity
                Bundle bz = new Bundle();

                bz.putInt("CacheNum", cN);
                Intent foundGeoCacheIntent = new Intent(this, FoundGeocache.class);
                foundGeoCacheIntent.putExtras(bz);
                startActivity(foundGeoCacheIntent);
                break;
        }
    }
}
