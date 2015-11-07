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

import java.util.List;

public class Map extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DatabaseHelper dbHelp;
    List<Geocache> geocaches;
    private String previousScreen;
    GPSTracker gps;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle b = this.getIntent().getExtras();
        if(b.containsKey("previousScreen")) {

            previousScreen = (String) b.get("previousScreen");
        }
        if(b.get("latitude") != null){
            latitude = b.getDouble("latitude");
        }

        if(b.get("longitude") != null){
            longitude = b.getDouble("longitude");
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
            dbHelp = new DatabaseHelper(this);
            geocaches = dbHelp.selectGeocaches(0);
            for (Geocache g : geocaches) {
                LatLng temp = new LatLng(g.getLatitude(), g.getLongitude());
                mMap.addMarker(new MarkerOptions().position(temp).title(g.getCacheName()));
            }

            gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng temp = new LatLng(latitude, longitude);
                // Position the camera
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }else{
                gps.showSettingsAlert();
            }

            mMap.setOnMarkerClickListener( this);
            mMap.setOnInfoWindowClickListener(this);
        }else if(previousScreen.equals("CreateGeocache")){
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMarkerClickListener(this);

            gps = new GPSTracker(this);
            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng temp = new LatLng(latitude, longitude);
                // Position the camera
                mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            }else{
                gps.showSettingsAlert();
            }

        }else if(previousScreen.equals("AboutGeocache")){
            LatLng temp = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(temp));
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
            mMap.addMarker(new MarkerOptions().position(latLng).title("Create a Geocache Here"));

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(previousScreen.equals("HomeScreen")) {
            Intent aboutCacheIntent = new Intent(this, AboutGeocache.class);
            Bundle b = new Bundle();
            Geocache chosen = new Geocache();
            for (Geocache g : geocaches) {
                if (g.getLatitude() == marker.getPosition().latitude && g.getLongitude() == marker.getPosition().longitude) {
                    chosen = g;
                    break;
                }
            }
            b.putString("Title", chosen.getCacheName());
            b.putInt("CacheNum", chosen.getCacheNum());
            //TODO: make TimesFound actually do times found
            b.putInt("TimesFound", chosen.getPoints());
            b.putString("Description", chosen.getDescription());
            b.putDouble("Latitude", chosen.getLatitude());
            b.putDouble("Longitude", chosen.getLongitude());
            b.putInt("Points", chosen.getPoints());
            b.putByteArray("Image", chosen.getImage());
            aboutCacheIntent.putExtras(b);
            startActivity(aboutCacheIntent);
        }else if(previousScreen.equals("CreateGeocache")){
            Intent i = new Intent();
            i.putExtra("latitude", Double.toString(marker.getPosition().latitude));
            i.putExtra("longitude", Double.toString(marker.getPosition().longitude));
            setResult(RESULT_OK, i);
            finish();
        }
    }




}
