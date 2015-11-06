package com.team4.social_geocaching_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MyAccount extends AppCompatActivity {

    ImageView face;
    TextView points, geocachesFound, userName;
    private DatabaseHelper dbHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        face = (ImageView) findViewById(R.id.userImage);
        //points = (TextView) findViewById(R.id.points);
        geocachesFound = (TextView) findViewById(R.id.found);
        userName = (TextView) findViewById(R.id.username);

        //TODO: pull total points from db, total number geocaches found
        //points.setText("5800");
        geocachesFound.setText("14");
        userName.setText(getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken"));
        //TODO: check if user has image uploaded

        if(true){
            face.setImageResource(R.drawable.defaultface);
            Toast.makeText(getApplicationContext(), "Upload a profile photo!", Toast.LENGTH_LONG).show();
        }

        this.dbHelp = new DatabaseHelper(this);
        String username = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
        List<Action> results = dbHelp.selectActionsByUser(username);
        int point = 0, found = 0;
        for (Action a : results) {
            if(a.getAction().equals("found")){
                point += a.getPoints();
                found += 1;
            }else {
                Log.d("MyAccount", a.getUsername() + " created Cache NO: " + a.getCacheNum() + " on " + a.getDate());
            }
        }
        points.setText(Integer.toString(point));
        geocachesFound.setText(Integer.toString(found));
        //TextView action1 = (TextView)findViewById(R.id.action1);
        //TextView date1 = (TextView)findViewById(R.id.action1);

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
