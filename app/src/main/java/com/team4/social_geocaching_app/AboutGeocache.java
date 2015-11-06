package com.team4.social_geocaching_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AboutGeocache extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_geocache);

        Bundle b = getIntent().getExtras();

        TextView GeocacheTitle = (TextView) findViewById(R.id.geocacheTitle);
        TextView TimesFound = (TextView) findViewById(R.id.timesFound);
        TextView Description = (TextView) findViewById(R.id.textDescription);
        TextView Points = (TextView) findViewById(R.id.points);

        Description.setText((String) b.get("Description"));
        GeocacheTitle.setText((String) b.get("Title"));
        int timesFound = (Integer)b.get("TimesFound");
        int pointVal = (Integer)b.get("Points");
        TimesFound.setText(Integer.toString(timesFound));
//        Points.setText(Integer.toString(pointVal));

        ImageButton foundButton = (ImageButton) findViewById(R.id.foundGeocacheButton);
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapButton);
        foundButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
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
            case R.id.foundGeocacheButton:
                //TODO: launch app to take photo of geocache, set it to this button
                Toast.makeText(getApplicationContext(), "navigate to found Geocache", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, FoundGeocache.class));
                break;
            case R.id.mapButton:
                //TODO: launch google maps, allow user to find geocache and place marker
                Toast.makeText(getApplicationContext(), "Place your geocache somewhere!", Toast.LENGTH_LONG).show();
                Bundle b = new Bundle();
                b.putString("previousScreen", "AboutGeocache");
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivityForResult(mapIntent, 0);
                break;
        }
    }
}
