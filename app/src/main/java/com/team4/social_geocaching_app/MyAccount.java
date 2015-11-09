package com.team4.social_geocaching_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAccount extends AppCompatActivity implements View.OnClickListener{

    ImageView face;
    TextView points, geocachesFound, geocachesCreated, userName;
    private DatabaseHelper dbHelp;
    private int CAMERA_REQUEST = 1;
    private int GALLERY_REQUEST = 2;
    ListView connectActivities;
    ArrayList<RowItem> itemsList;
    Intent aboutCacheIntent;
    Intent viewCacheIntent;
    List<Action> actionsList;
    List<Account> accountsList;
    byte[] inputByteStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        face = (ImageView) findViewById(R.id.userImage);
        points = (TextView) findViewById(R.id.points);
        geocachesFound = (TextView) findViewById(R.id.found);
        geocachesCreated = (TextView) findViewById(R.id.created);
        userName = (TextView) findViewById(R.id.username);
        aboutCacheIntent = new Intent(this,AboutGeocache.class);
        viewCacheIntent = new Intent(this,GeocacheVisit.class);


        //TODO: pull total points from db, total number geocaches found
        //points.setText("5800");
        //geocachesFound.setText("14");
        //TODO: check if user has image uploaded

        Bundle b = this.getIntent().getExtras();
        String username = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
        if(this.getIntent().getStringExtra("accountName") != null){

            username = b.getString("accountName");

            if(username.equals(getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken"))){
                face.setOnClickListener(this);
            }
        }else{
            face.setOnClickListener(this);
        }
        userName.setText(username);



        this.dbHelp = new DatabaseHelper(this);

        inputByteStream = dbHelp.getAccountImage(username);
        face.setImageBitmap(BitmapFactory.decodeByteArray(inputByteStream, 0, inputByteStream.length));

        Drawable drawable = getResources().getDrawable(R.drawable.defaultface);
        Bitmap bp = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] imageByteStream = outputStream.toByteArray();
        if(Arrays.equals(inputByteStream, imageByteStream)) {
            Toast.makeText(getApplicationContext(), "Upload a profile photo!", Toast.LENGTH_LONG).show();
        }

        List<Action> results = dbHelp.selectActions(username, 0);
        int point = 0, found = 0, created=0;
        for (Action a : results) {
            if(a.getAction().equals("found")){
                point += a.getPoints();
                found += 1;
            }else {
                created += 1;
            }
        }
        points.setText(Integer.toString(point));
        geocachesFound.setText(Integer.toString(found));
        geocachesCreated.setText(Integer.toString(created));

        itemsList = new ArrayList<RowItem>();
        actionsList = dbHelp.selectActions(username, 0);
        String date, action;
        Geocache currentCache;

        int cacheNum, points;
        for(int k = 0; k<actionsList.size(); k++){
            date = actionsList.get(k).getDate();
            action = actionsList.get(k).getAction();
            cacheNum = actionsList.get(k).getCacheNum();
            currentCache = dbHelp.selectGeocaches(cacheNum).get(0);
            if (action.equals("created")||action.equals("create")) {
                itemsList.add(k,new RowItem(username+" created "+currentCache.getCacheName(), date));
            }else{
                points = actionsList.get(k).getPoints();
                itemsList.add(k,new RowItem(username+" found "+currentCache.getCacheName()+" \n("+points+" points)",date));
            }


        }
        ListAdapter currentAdapter = new ListAdapter(this, itemsList);
        connectActivities = (ListView) findViewById(R.id.activityList);
        connectActivities.setAdapter(currentAdapter);
        TextView activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText("Recent activities");
        connectActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();
                List<Geocache> gC = dbHelp.selectGeocaches(actionsList.get(position).getCacheNum());
                if (actionsList.get(position).getAction().equals("created") || actionsList.get(position).getAction().equals("created")) {
                    b.putString("Title", gC.get(0).getCacheName());
                    b.putInt("CacheNum", gC.get(0).getCacheNum());
                    //TODO: make TimesFound actually do times found??
                    b.putInt("TimesFound", gC.get(0).getPoints());
                    b.putString("Description", gC.get(0).getDescription());
                    b.putDouble("Latitude", gC.get(0).getLatitude());
                    b.putDouble("Longitude", gC.get(0).getLongitude());
                    b.putInt("Points", gC.get(0).getPoints());
                    b.putByteArray("Image", gC.get(0).getImage());
                    aboutCacheIntent.putExtras(b);
                    startActivity(aboutCacheIntent);
                } else {
                    b.putInt("CacheNum", gC.get(0).getCacheNum());
                    b.putString("Username", actionsList.get(position).getUsername());
                    viewCacheIntent.putExtras(b);
                    startActivity(viewCacheIntent);
                }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userImage:
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_REQUEST);

                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                face.setImageBitmap(bp);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                inputByteStream = outputStream.toByteArray();
                dbHelp.modifyAccountImage((String) userName.getText(), inputByteStream);
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap bp = (BitmapFactory.decodeFile(picturePath));
                face.setImageBitmap(bp);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                inputByteStream = outputStream.toByteArray();
                dbHelp.modifyAccountImage((String) userName.getText(), inputByteStream);

            }
        }
    }


}

