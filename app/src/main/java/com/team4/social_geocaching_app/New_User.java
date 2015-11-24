package com.team4.social_geocaching_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

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
                    Toast.makeText(getApplicationContext(), "Username and password are good!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private boolean CreateAccount() {
        EditText userNameBox = (EditText)findViewById(R.id.username);
        EditText passwordBox = (EditText)findViewById(R.id.password);
        String username = userNameBox.getText().toString();
        String password = passwordBox.getText().toString();

        Drawable drawable = getResources().getDrawable(R.drawable.defaultface);
        Bitmap bp = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] imageByteStream = outputStream.toByteArray();


        this.dbHelp = new DatabaseHelper(this);

        if ((!username.equals(""))
                && (!password.equals(""))&&password.length() > 5 && password.matches("[A-Za-z0-9]")&&password.matches(".*[0-9].*")&&!password.equals(password.toLowerCase())&&!password.equals(password.toUpperCase())&&!this.dbHelp.usernameTaken(username)) {
            this.dbHelp.insertAccount(username, password, imageByteStream);
            return true;
        }
        else if(this.dbHelp.usernameTaken(username)){
            Toast.makeText(getApplicationContext(), "Username taken!", Toast.LENGTH_LONG).show();
        }
        else if(password.length() < 6){
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters!", Toast.LENGTH_LONG).show();
        }else if(!password.matches("[A-Za-z0-9]")){
            Toast.makeText(getApplicationContext(), "Password must contain only numbers and letters!", Toast.LENGTH_LONG).show();
        }else if(!password.matches(".*[0-9].*")){
            Toast.makeText(getApplicationContext(), "Password must contain at least one number!", Toast.LENGTH_LONG).show();
        }else if(password.equals(password.toLowerCase())){
            Toast.makeText(getApplicationContext(), "Password must contain at least one uppercase character!", Toast.LENGTH_LONG).show();
        }else if(password.equals(password.toUpperCase())){
            Toast.makeText(getApplicationContext(), "Password must contain at least one lowercase character!", Toast.LENGTH_LONG).show();
        }
        return false;
    }



}
