package com.example.computer1.locofferproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int LOCATION_PERMISSION = 1;
    private static GoogleApiClient mGoogleApiClient;
    private double userLat,userLon;
    GridView gv;
    public static String [] prgmNameList={"Food","Apparel","Salon","Grocery","Coffee","Bar"};
    public static int [] prgmImages={R.drawable.food,R.drawable.apparel,R.drawable.salon,R.drawable.grocery,R.drawable.coffee,R.drawable.bar};
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }

        setContentView(R.layout.activity_main1);
        gv=(GridView) findViewById(R.id.gridView1);
    }
        @Override
        public void onConnected(Bundle bundle) {
            startLocationTracking();
        }

    private void startLocationTracking() {
        if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            return;
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] result){
            super.onRequestPermissionsResult(requestCode, permissions, result);
            if (requestCode == LOCATION_PERMISSION && result[0] == PackageManager.PERMISSION_GRANTED){
                startLocationTracking();
            }
    }

    @Override
    public void onLocationChanged(Location location) {
            setLatLon(location.getLatitude(), location.getLongitude());
            return;
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

        public void setLatLon(double lat, double lon){
        userLat = lat;
        userLon = lon;
        gv.setAdapter(new CustomAdapter(this, R.layout.activity_main1, prgmNameList,prgmImages ,userLat, userLon));
    }
}
