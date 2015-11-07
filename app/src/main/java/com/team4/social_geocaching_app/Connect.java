package com.team4.social_geocaching_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Connect extends AppCompatActivity implements View.OnClickListener {
    ListView connectActivities;
    ArrayList<RowItem> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        itemsList = new ArrayList<>();
        DatabaseHelper dbHelp = new DatabaseHelper(getApplicationContext());
        //dbHelp.fillAppWithData();
        List<Action> actionsList = dbHelp.selectActions("", 0);
        String username, date, action;
        List<Geocache> gC;
        int cacheNum, points;
        for(int k = 0; k<actionsList.size(); k++){
            username = actionsList.get(k).getUsername();
            date = actionsList.get(k).getDate();
            action = actionsList.get(k).getAction();
            cacheNum = actionsList.get(k).getCacheNum();
            gC = dbHelp.selectGeocaches(cacheNum);
            points = actionsList.get(k).getCacheNum();
            if (action.equals("created")||action.equals("create")) {
                itemsList.add(k,new RowItem(username+" created "+gC.get(0).getCacheName(), date));
            }else{
                itemsList.add(k,new RowItem(username+" found "+gC.get(0).getCacheName()+" ("+points+" points)",date));
            }


        }
        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        connectActivities = (ListView) findViewById(R.id.connectList);
        connectActivities.setAdapter(currentAdapter);
        connectActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getApplicationContext(), itemsList.get(position).activityText+"!"+position, Toast.LENGTH_LONG).show();
            }
        });



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
            case R.id.connectList:
                Toast.makeText(getApplicationContext(), "clicked on list", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
