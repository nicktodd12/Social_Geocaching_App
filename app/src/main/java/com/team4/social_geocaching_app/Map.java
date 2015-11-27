package com.team4.social_geocaching_app;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which controls the display and functionality of the Map
 */
public class Map extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{

    //variables used throughout the activity
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DatabaseHelper dbHelp;
    List<Geocache> geocaches;
    private String previousScreen;
    GPSTracker gps;
    double latitude, longitude;
    ArrayList<LatLng> latlngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //get the bundle to determine the previous activity
        Bundle b = this.getIntent().getExtras();
        if(b.containsKey("previousScreen")) {

            previousScreen = (String) b.get("previousScreen");
        }
        //get the latitude and longitude if available
        if(b.get("latitude") != null){
            latitude = b.getDouble("latitude");
        }

        if(b.get("longitude") != null){
            longitude = b.getDouble("longitude");
        }
        //set up the map
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //save the placed marker locations on screen rotation
        if (latlngs.size() > 0){
            savedInstanceState.putParcelableArrayList("latlngs", latlngs);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        //place the markers back on the screen after loading the rotation
        if(savedInstanceState.get("latlngs") != null){
            latlngs = savedInstanceState.getParcelableArrayList("latlngs");
            for(LatLng l : latlngs){
                mMap.addMarker(new MarkerOptions().position(l).title("Create a Geocache Here"));
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if(previousScreen.equals("HomeScreen")) {
            //if coming from the home screen get all of the geocaches from the database
            dbHelp = new DatabaseHelper(this);
            geocaches = dbHelp.selectGeocaches(0);
            //place markers for the geocaches
            for (Geocache g : geocaches) {
                LatLng temp = new LatLng(g.getLatitude(), g.getLongitude());
                mMap.addMarker(new MarkerOptions().position(temp).title(g.getCacheName()));
            }
            //get the user's location if possible
            gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng temp = new LatLng(latitude, longitude);
                // Position the camera
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }else{
                //show the settings alert if the location cannot be determined
                gps.showSettingsAlert();
            }
            //set listeners for the map markers
            mMap.setOnMarkerClickListener( this);
            mMap.setOnInfoWindowClickListener(this);
        }else if(previousScreen.equals("CreateGeocache")){
            //if coming from the create page then set listeners
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMarkerClickListener(this);

            //attempt to get the user's location
            gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng temp = new LatLng(latitude, longitude);
                // Position the camera
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }else{
                //show settings if the location can not be determined
                gps.showSettingsAlert();
            }

        }else if(previousScreen.equals("AboutGeocache")){
            //if coming from the about geocache screen set a marker at the geocache's location
            LatLng temp = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(temp));
            //zoom in to the location
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));

        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(previousScreen.equals("CreateGeocache")){

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(previousScreen.equals("CreateGeocache")){
            //create a marker on a long click on the create page
            mMap.addMarker(new MarkerOptions().position(latLng).title("Create a Geocache Here"));
            latlngs.add(latLng);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //show the information window if a marker is clicked
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(previousScreen.equals("HomeScreen")) {
            //if the info window is clicked create a new intent and bundle
            Intent aboutCacheIntent = new Intent(this, AboutGeocache.class);
            Bundle b = new Bundle();
            //get the latitude and longitude of the chosen geocache
            Geocache chosen = new Geocache();
            for (Geocache g : geocaches) {
                if (g.getLatitude() == marker.getPosition().latitude && g.getLongitude() == marker.getPosition().longitude) {
                    chosen = g;
                    break;
                }
            }
            //put the cache info in the bundle
            b.putString("Title", chosen.getCacheName());
            b.putInt("CacheNum", chosen.getCacheNum());
            b.putInt("TimesFound", chosen.getPoints());
            b.putString("Description", chosen.getDescription());
            b.putDouble("Latitude", chosen.getLatitude());
            b.putDouble("Longitude", chosen.getLongitude());
            b.putInt("Points", chosen.getPoints());
            b.putByteArray("Image", chosen.getImage());
            //start the about cache activity
            aboutCacheIntent.putExtras(b);
            startActivity(aboutCacheIntent);
        }else if(previousScreen.equals("CreateGeocache")){
            //if on the create page create a new intent to return
            Intent i = new Intent();
            i.putExtra("latitude", Double.toString(marker.getPosition().latitude));
            i.putExtra("longitude", Double.toString(marker.getPosition().longitude));
            //finish the actiivty and return the chosen latitude and longitude
            setResult(RESULT_OK, i);
            finish();
        }
    }




}
