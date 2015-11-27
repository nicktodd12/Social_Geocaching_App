package com.team4.social_geocaching_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Class which handles the setup and functionality of the Geocache Creation page
 */
public class CreateGeocache extends AppCompatActivity implements OnClickListener {

    //Variables needed throughout the entire activity
    Button submitGeocache;
    ImageButton mapImage;
    ImageButton geocacheImage;
    EditText titleBox;
    TextView latitude;
    TextView longitude;
    EditText description;
    EditText points;
    private DatabaseHelper dbHelp;
    private String currentUsername;
    private Bitmap savedBp;
    private byte[] imageByteStream;
    private int CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialize variables and set onclick listeners where required
        this.dbHelp = new DatabaseHelper(this);
        currentUsername = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
        super.onCreate(savedInstanceState);
        titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
        description = (EditText)findViewById(R.id.editDescription);
        points = (EditText)findViewById(R.id.editGeocachePoints);
        setContentView(R.layout.activity_create_cache);
        submitGeocache = (Button)findViewById(R.id.submitCreateGeoCache);
        submitGeocache.setOnClickListener(this);
        mapImage = (ImageButton)findViewById(R.id.mapImage);
        mapImage.setOnClickListener(this);
        geocacheImage = (ImageButton)findViewById(R.id.geocacheImage);
        geocacheImage.setOnClickListener(this);
        latitude = (TextView)findViewById(R.id.editLatitude);
        longitude = (TextView)findViewById(R.id.editLongitude);
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
            case R.id.submitCreateGeoCache:
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                latitude = (TextView)findViewById(R.id.editLatitude);
                longitude = (TextView)findViewById(R.id.editLongitude);
                points = (EditText)findViewById(R.id.editGeocachePoints);
                //if the submit button is pressed check to make sure all info is correct
                if(latitude.getText().toString().length() > 0 && createNewGeocache(titleBox.getText().toString(), points.getText().toString(), latitude.getText().toString(),longitude.getText().toString(),description.getText().toString(), imageByteStream)){
                    //if everything is correct and the cache was created successfully then display a created toast and finish the activity
                    Toast.makeText(getApplicationContext(), "Geocache " + titleBox.getText().toString() + " created!", Toast.LENGTH_LONG).show();
                    finish();
                }else if(latitude.getText().toString().length() == 0) {
                    //if a position is not selected display a toast
                    Toast.makeText(getApplicationContext(), "Click the map button!", Toast.LENGTH_LONG).show();
                }else{
                    //display a toast saying creation was not successful for any other reason
                    Toast.makeText(getApplicationContext(), "Unable to create Geocache!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.geocacheImage:
                //if the image button is pressed then start the camera and wait for a result
                Toast.makeText(getApplicationContext(), "Take a geocache Image!", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            case R.id.mapImage:
                //if the map image is pressed then start the map and wait for a result
                Toast.makeText(getApplicationContext(), "Place your geocache somewhere!", Toast.LENGTH_LONG).show();
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                Bundle b = new Bundle();
                b.putString("previousScreen", "CreateGeocache");
                Intent mapIntent = new Intent(this, Map.class);
                mapIntent.putExtras(b);
                startActivityForResult(mapIntent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK) {
            //on return from the map set the latitude and longitude picked
            latitude.setText(data.getStringExtra("latitude"));
            longitude.setText(data.getStringExtra("longitude"));
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //on return from the camera get the bitmap and display it
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            //bp = resizeImage(bp, findViewById(R.id.geocacheImage).getWidth(), findViewById(R.id.geocacheImage).getHeight());
            geocacheImage.setImageBitmap(bp);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imageByteStream = outputStream.toByteArray();
            setImage(bp);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        //save the image, latitude, and longitude during a screen rotation
        outState.putString("SavedLat", latitude.getText().toString());
        outState.putString("SavedLong", longitude.getText().toString());
        outState.putParcelable("image", savedBp);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState){
        //set latitude and longitude to empty strings if they were not chosen
        String latString = "";
        String longString = "";

        //if the latitude and longitude were picked then reset them on the screen
        if(outState.get("SavedLat") != null) {
            latString = outState.getString("SavedLat");
            longString = outState.getString("SavedLong");
        }

        if(latString.length()>0){
            latitude.setText(latString);
            longitude.setText(longString);
        }

        //if there was an image saved then display it on screen
        savedBp = (Bitmap)outState.get("image");
        if(savedBp!=null){
            setImage(savedBp);
        }
        super.onRestoreInstanceState(outState);
    }

    //Helper method which sets and displays the bitmap in the image button
    public void setImage(Bitmap bp){
        savedBp = bp;
        geocacheImage.setImageBitmap(bp);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        imageByteStream = outputStream.toByteArray();
    }

    //Handles creation and checking of inputs for cache creation
    public boolean createNewGeocache(String title, String points, String latitude, String longitude, String description, byte[] imageInBytes){
        if(latitude.equals("(Latitude)")|| longitude.equals("Longitude")){
            //do not create if the location has not been picked
            Toast.makeText(getApplicationContext(), "Click the map icon to get coordinates!", Toast.LENGTH_LONG).show();
            return false;
        }else if(title.length() == 0){
            //do not create if no title has been input
            Toast.makeText(getApplicationContext(), "Enter a title for your Geocache!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!(points.matches("^-?\\d+$")&&Integer.parseInt(points)>0 && Integer.parseInt(points)<21)){
            //do not create if the points is not a number between 1 and 20
            Toast.makeText(getApplicationContext(), "Enter a positive int value for points between 1 and 20!", Toast.LENGTH_LONG).show();
            return false;
        }else if(imageInBytes == null){
            //do not create if no image was taken
            Toast.makeText(getApplicationContext(), "Include an image for your Geocache!", Toast.LENGTH_LONG).show();
            return false;
        }
        //create a new cache and corresponding action for the creation of the cache
        dbHelp.insertGeocache(currentUsername, Integer.parseInt(points), Double.parseDouble(latitude), Double.parseDouble(longitude), title, description, imageInBytes);
        List<Geocache> results = dbHelp.selectGeocaches(0);
        int cacheNum = results.get(0).getCacheNum();
        dbHelp.insertAction(currentUsername, "created", cacheNum, "", new byte[0]);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CacheList", 0);
        pref.edit().putString(Integer.toString(cacheNum), title).apply();
        return true;
    }

    //used for resizing and image to fit inside the image button
    public Bitmap resizeImage(Bitmap bp, float targetWidth, float targetHeight){
        float width = bp.getWidth();
        float height = bp.getHeight();
        if(width > targetWidth || height > targetHeight){
            Matrix matrix = new Matrix();
            float scaleWidth = ( targetWidth/width );
            float scaleHeight = ( targetWidth/width );
            matrix.postScale(scaleWidth, scaleHeight);
            bp = Bitmap.createBitmap(bp, 0, 0, (int)width, (int)height, matrix, true);
        }
        return bp;
    }
}
