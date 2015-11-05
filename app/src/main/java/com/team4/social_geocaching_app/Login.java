package com.team4.social_geocaching_app;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements OnClickListener {

    Button btnLogin;
    Button btnCreate;
    private static final String TAG = "LoginActivity";
    private DatabaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.log_button);
        btnLogin.setOnClickListener(this);
        btnCreate = (Button)findViewById(R.id.create_account_button);
        btnCreate.setOnClickListener(this);
        this.dbHelp = new DatabaseHelper(this);
        if(this.dbHelp.firstTime()){
            Toast.makeText(getApplicationContext(), "Create an account!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            case R.id.log_button:
                if (validLogin()) {
                    startActivity(new Intent(this, HomeScreen.class));
                }
                break;
            case R.id.create_account_button:
                startActivity(new Intent(this, New_User.class));
                break;
        }
    }

    public boolean validLogin(){
        EditText userNameBox = (EditText)findViewById(R.id.username_edit);
        EditText passwordBox = (EditText)findViewById(R.id.password_edit);
        String currentUserName = userNameBox.getText().toString();
        String currentPassword = passwordBox.getText().toString();
        if(loginSuccess(currentUserName, currentPassword)){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Preferences",0);
            pref.edit().putString("userName", currentUserName).apply();
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "Username and password do not match!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean loginSuccess(String currentUserName, String currentPassword){
        dbHelp = new DatabaseHelper(this);
        return(dbHelp.selectAll(currentUserName, currentPassword).size() > 0);
    }
}
