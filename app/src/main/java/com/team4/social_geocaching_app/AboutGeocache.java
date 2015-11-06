package com.team4.social_geocaching_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutGeocache extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_geocache);

        Bundle b = getIntent().getExtras();

        TextView GeocacheTitle = (TextView) findViewById(R.id.GeocacheTitle);
        TextView TimesFound = (TextView) findViewById(R.id.timesFound);
        TextView Description = (TextView) findViewById(R.id.textDescription);
        TextView Points = (TextView) findViewById(R.id.points);

        Description.setText((String) b.get("Description"));
        GeocacheTitle.setText((String) b.get("Title"));
        int timesFound = (Integer)b.get("TimesFound");
        int pointVal = (Integer)b.get("Points");
        TimesFound.setText(Integer.toString(timesFound));
        Points.setText(Integer.toString(pointVal));
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
}
