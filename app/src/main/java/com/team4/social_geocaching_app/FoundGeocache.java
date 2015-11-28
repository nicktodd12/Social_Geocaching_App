package com.team4.social_geocaching_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Class which creates and handles functionality on the found geocache page
 */
public class FoundGeocache extends AppCompatActivity implements View.OnClickListener {

    //variables used throughout the class
    EditText comments;
    Button submit;
    DatabaseHelper dbHelp;
    int cacheNum;
    Geocache geocache;
    ImageButton foundImage;
    Bitmap savedBp;
    private int CAMERA_REQUEST = 1;
    private int GALLERY_REQUEST = 2;
    private byte[] imageByteStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the layout and initialize variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_geocache);
        submit = (Button) findViewById(R.id.submitCheckIn);
        submit.setOnClickListener(this);
        foundImage = (ImageButton) findViewById(R.id.geocacheFoundImage);
        foundImage.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        cacheNum = (b.getInt("CacheNum"));
        dbHelp = new DatabaseHelper(this);
        //run a query to retrieve the info on the geocache
        geocache =  dbHelp.selectGeocaches(cacheNum).get(0);
        TextView title = (TextView) findViewById(R.id.checkInTitle);
        TextView creator = (TextView) findViewById(R.id.createdBy);
        TextView points = (TextView) findViewById(R.id.GeocachePoints);
        comments = (EditText) findViewById(R.id.commentBox);
        //populate the info for the geocache
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //save the image when the screen is rotated if one was taken
        if(savedBp != null) {
            savedInstanceState.putParcelable("image", savedBp);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //get the image from the saved state and display it
        savedBp = (Bitmap)savedInstanceState.get("image");
        if(savedBp != null){
            setImage(savedBp);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitCheckIn:
                //if the user clicks the check in button then insert  new action into the database
                String comment = comments.getText().toString();
                String username = getApplicationContext().getSharedPreferences("Preferences", 0).getString("userName", "Broken");
                if(imageByteStream != null){
                    dbHelp.insertAction(username, "found", cacheNum, comment, imageByteStream);
                    Toast.makeText(getApplicationContext(), "Cache " + geocache.getCacheName() + " Found!", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Take a photo!", Toast.LENGTH_LONG).show();
                }


                //finish the activity

                break;
            case R.id.geocacheFoundImage:
                //if the user clicks the image button create options to choose
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(FoundGeocache.this);
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    //set a listener inside the options list
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            //if the user chooses to take a photo then start the camera and wait for a result
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            //if the user chooses the gallery then start the gallery and wait for a result
                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_REQUEST);

                        }
                        else if (options[item].equals("Cancel")) {
                            //remove the dialog if the user cancels
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                //if the user took a picture then retrieve and set the picture button to the image
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                //bp = resizeImage(bp, findViewById(R.id.geocacheFoundImage).getWidth(), findViewById(R.id.geocacheFoundImage).getHeight());
                setImage(bp);
            } else if (requestCode == GALLERY_REQUEST) {
                //if the user selected then retrieve and set the picture button to the image
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap bp = (BitmapFactory.decodeFile(picturePath));

                bp = resizeImage(bp, findViewById(R.id.geocacheFoundImage).getWidth(), findViewById(R.id.geocacheFoundImage).getHeight());
                setImage(bp);
            }
        }
    }

    //Sets the bitmap image to the image button on the screen
    public void setImage(Bitmap bp){
        savedBp = bp;
        foundImage.setImageBitmap(bp);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        imageByteStream = outputStream.toByteArray();
    }

    //Resizes a selected image to appear inside of the image button
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
