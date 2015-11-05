package com.team4.social_geocaching_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class New_User extends AppCompatActivity implements OnClickListener {
    private Button btnNewUser;
    private DatabaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__user);
        btnNewUser = (Button)findViewById(R.id.submit);
        btnNewUser.setOnClickListener(this);
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
            case R.id.submit:
                boolean success = this.CreateAccount();
                if(success){
                    startActivity(new Intent(this, Login.class));
                    Toast.makeText(getApplicationContext(), "Username and password are good!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean CreateAccount() {
        EditText userNameBox = (EditText)findViewById(R.id.username);
        EditText passwordBox = (EditText)findViewById(R.id.password);
        String username = userNameBox.getText().toString();
        String password = passwordBox.getText().toString();
        this.dbHelp = new DatabaseHelper(this);

        if ((!username.equals(""))
                && (!password.equals(""))&&password.length() > 5 && !this.dbHelp.usernameTaken(username)) {
            this.dbHelp.insertAccount(username, password);
            return true;
        }
        else if(this.dbHelp.usernameTaken(username)){
            Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_LONG).show();
        }
        else if(password.length() < 6){
            Toast.makeText(getApplicationContext(), "Make a longer password!", Toast.LENGTH_LONG).show();
        }
        return false;
    }



}
