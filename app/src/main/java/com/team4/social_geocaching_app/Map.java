package com.team4.social_geocaching_app;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Set up Listeners

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

        // Create all Markers with their information
        // These will be dynamically loaded from the database along with relevant info
        // including descriptions, Images, Times Found, Creator, etc...

        LatLng sydney = new LatLng(-34, 151);
        LatLng bolz = new LatLng(40.002966, -83.015217);
        LatLng union = new LatLng(39.997934, -83.008211);
        LatLng michaelHouse = new LatLng(39.995363, -83.002591);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(bolz).title("Bolz Hall"));
        mMap.addMarker(new MarkerOptions().position(union).title("Ohio State Union"));
        mMap.addMarker(new MarkerOptions().position(michaelHouse).title("Michael's House").snippet(michaelHouse.toString()));

        // Position the camera
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bolz));

        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);


    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent aboutCacheIntent = new Intent(this, AboutGeocache.class);
        Bundle b = new Bundle();
        switch (marker.getTitle()){
            case "Michael's House":
                b.putString("Title", marker.getTitle());
                b.putInt("TimesFound", 7);
                b.putString("Description", "This Geocache is located at Michael's House");
                b.putDouble("Latitude", marker.getPosition().latitude);
                b.putDouble("Longitude", marker.getPosition().longitude);
                aboutCacheIntent.putExtras(b);
                startActivity(aboutCacheIntent);
        }
    }


//    @Override
//    public void onMapReady(GoogleMap map) {
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng columbus = new LatLng(40.002966, -83.015217);
//        map.addMarker(new MarkerOptions().position(columbus).title("Marker in Sydney"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(columbus));
//    }
}
