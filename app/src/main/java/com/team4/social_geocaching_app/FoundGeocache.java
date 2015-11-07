package com.team4.social_geocaching_app;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FoundGeocache extends AppCompatActivity implements View.OnClickListener {

    EditText comments;
    Button submit;
    DatabaseHelper dbHelp;
    int cacheNum;
    Geocache geocache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_geocache);
        submit = (Button) findViewById(R.id.submitCheckIn);
        submit.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        cacheNum = (b.getInt("CacheNum"));
        dbHelp = new DatabaseHelper(this);
        geocache =  dbHelp.selectGeocaches(cacheNum).get(0);
        //List<Action> actionList = dbHelp.selectActions(geocache.getUsername(), cacheNum);
        TextView title = (TextView) findViewById(R.id.checkInTitle);
        TextView creator = (TextView) findViewById(R.id.createdBy);
        TextView points = (TextView) findViewById(R.id.GeocachePoints);
        comments = (EditText) findViewById(R.id.commentBox);
        title.append(" "+geocache.getCacheName());
        creator.append(" "+geocache.getUsername());
        points.append(" "+geocache.getPoints());
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
            case R.id.submitCheckIn:
                String comment = comments.getText().toString();
                String username = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
                dbHelp.insertAction(username, "found", cacheNum, comment);
                Toast.makeText(getApplicationContext(), "Cache "+ geocache.getCacheName() +" Found!", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                break;
        }
    }
}
