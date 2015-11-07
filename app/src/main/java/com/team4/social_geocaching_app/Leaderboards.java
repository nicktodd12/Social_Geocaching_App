package com.team4.social_geocaching_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Leaderboards extends AppCompatActivity implements View.OnClickListener{
    ArrayList<RowItem> itemsList;
    DatabaseHelper dbHelp;
    ListView leaders;
    Intent viewAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        dbHelp = new DatabaseHelper(this);
        List<Account> results = dbHelp.getLeaderboard();
        List<Account> allAccounts = dbHelp.getAllAccounts();
        viewAccount = new Intent(this, MyAccount.class);
        itemsList = new ArrayList<>();
        for(int k = 0; k<results.size(); k++){
            itemsList.add(k, new RowItem(results.get(k).getUsername(), Integer.toString(results.get(k).getPoints())+" points"));
        }
        String currentUser;
        boolean print;

        for(int k = 0; k<allAccounts.size(); k++){
            currentUser = allAccounts.get(k).getUsername();
            print = true;
            for(int j = 0; j<results.size(); j++){
                if(currentUser.equals(results.get(j).getUsername())){
                    print = false;
                    j=results.size();
                }
            }
            if(print){
                itemsList.add(itemsList.size(), new RowItem(currentUser, "0 points"));
            }
        }

        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        leaders = (ListView) findViewById(R.id.leaderboardList);
        leaders.setAdapter(currentAdapter);
        leaders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();
                b.putString("accountName", itemsList.get(position).activityText);
                viewAccount.putExtras(b);
                startActivity(viewAccount);
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
