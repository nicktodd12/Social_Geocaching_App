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

public class CreateGeocache extends AppCompatActivity implements OnClickListener {

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
    private ImageView mImageView;
    private Bitmap savedBp;
    private byte[] imageByteStream;
    private int CAMERA_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//        latitude = (TextView)findViewById(R.id.editLatitude);
//        longitude = (TextView)findViewById(R.id.editLongitude);
        switch (v.getId()) {
            case R.id.submitCreateGeoCache:
                titleBox = (EditText)findViewById(R.id.editGeocacheTitle);
                description = (EditText)findViewById(R.id.editDescription);
                latitude = (TextView)findViewById(R.id.editLatitude);
                longitude = (TextView)findViewById(R.id.editLongitude);
                points = (EditText)findViewById(R.id.editGeocachePoints);
                if(latitude.getText().toString().length() > 0 && createNewGeocache(titleBox.getText().toString(), points.getText().toString(), latitude.getText().toString(),longitude.getText().toString(),description.getText().toString(), imageByteStream)){
                    Toast.makeText(getApplicationContext(), "Geocache " + titleBox.getText().toString() + " created!", Toast.LENGTH_LONG).show();
                    finish();
                }else if(latitude.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Click the map button!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to create Geocache!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.geocacheImage:
                //TODO: launch app to take photo of geocache, set it to this button
                Toast.makeText(getApplicationContext(), "Take a geocache Image!", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                break;
            case R.id.mapImage:
                //TODO: launch google maps, allow user to find geocache and place marker
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
            latitude.setText(data.getStringExtra("latitude"));
            longitude.setText(data.getStringExtra("longitude"));
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
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
        outState.putString("SavedLat", latitude.getText().toString());
        outState.putString("SavedLong", longitude.getText().toString());
        outState.putParcelable("image", savedBp);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState){
        String latString = "";
        String longString = "";
        if(outState.get("SavedLat") != null) {
            latString = outState.getString("SavedLat");
            longString = outState.getString("SavedLong");
        }

        if(latString.length()>0){
            latitude.setText(latString);
            longitude.setText(longString);
        }

        savedBp = (Bitmap)outState.get("image");
        if(savedBp!=null){
            setImage(savedBp);
        }
        super.onRestoreInstanceState(outState);
    }

    public void setImage(Bitmap bp){
        savedBp = bp;
        geocacheImage.setImageBitmap(bp);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        imageByteStream = outputStream.toByteArray();
    }

    public boolean createNewGeocache(String title, String points, String latitude, String longitude, String description, byte[] imageInBytes){
        if(latitude.equals("(Latitude)")|| longitude.equals("Longitude")){
            Toast.makeText(getApplicationContext(), "Click the map icon to get coordinates!", Toast.LENGTH_LONG).show();
            return false;
        }else if(title.length() == 0){
            Toast.makeText(getApplicationContext(), "Enter a title for your Geocache!", Toast.LENGTH_LONG).show();
            return false;
        }else if(!(points.matches("^-?\\d+$")&&Integer.parseInt(points)>0 && Integer.parseInt(points)<21)){
            Toast.makeText(getApplicationContext(), "Enter a positive int value for points between 1 and 20!", Toast.LENGTH_LONG).show();
            return false;
        }else if(imageInBytes == null){
            Toast.makeText(getApplicationContext(), "Include an image for your Geocache!", Toast.LENGTH_LONG).show();
            return false;
        }
        dbHelp.insertGeocache(currentUsername, Integer.parseInt(points), Double.parseDouble(latitude), Double.parseDouble(longitude), title, description, imageInBytes);
        List<Geocache> results = dbHelp.selectGeocaches(0);
        int cacheNum = results.get(0).getCacheNum();
        dbHelp.insertAction(currentUsername, "created", cacheNum, "", new byte[0]);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CacheList", 0);
        pref.edit().putString(Integer.toString(cacheNum), title).apply();
        return true;
    }

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
