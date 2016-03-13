package com.example.computer1.locofferproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.*;
import java.util.List;


public class secondPageActivity extends AppCompatActivity {
    protected static final String TAG = "secondPageActivity";


    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected double currentLongitude,currentLatitude;
    protected  String categoryName;
    protected  ArrayList<String> couponList, ratingUriList;
    protected  ArrayList<Double> distanceList;
    protected  ArrayList<StoreData> storeList;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private  String consumerKey,consumerSecret,token,tokenSecret;
    private  String storeName, storeImageUri;
    protected  double[] coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        Firebase myFirebase = new Firebase("https://locationoffer.firebaseIO.com");

        Bundle position = getIntent().getExtras();
        categoryName = position.getString("categoryName");
        currentLatitude = position.getDouble("userLat");
        currentLongitude = position.getDouble("userLon");

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);

        myFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                couponList = new ArrayList<>();
                ratingUriList = new ArrayList<String>();
                storeList = new ArrayList<>();
                distanceList = new ArrayList<>();
                consumerKey = "ylAFXnFNueOKCNkAtM9DlA";
                consumerSecret = "grG4EFVszWiRfIIlUtspOVs0vRo";
                token = "fv95yldwZTOosB6AwYnRWI_cluD1Y7kJ";
                tokenSecret = "jk1ouFv9xh_dS5qC5w_IJF_Kqk0";
                for (DataSnapshot data : snapshot.getChildren()) {

                    StoreData store = data.getValue(StoreData.class);
                    storeName = store.getName();
                    storeImageUri = store.getImageFile();
                    String category = store.getCategory();
                    String address = store.getAddress();
                    if (category.equals(categoryName.toLowerCase())) {
                        coordinates = getStoreLocation(address);
                        boolean withinDistance = getLocationFromAddress(address);
                        if (withinDistance) {
                            store.setStoreLat(coordinates[0]);
                            store.setStoreLong(coordinates[1]);
                            storeList.add(store);
                            couponList.add(storeName);

                        }
                    }
                }
                ListView couponListView = (ListView) findViewById(R.id.second_list_view);
                couponListView.setAdapter(new StoresCustomAdapter(secondPageActivity.this, R.layout.store_custom_row, storeList, distanceList, currentLatitude, currentLongitude, categoryName));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_uninstall) {
            Uri packageURI = Uri.parse("package:com.example.computer1.locofferproject");
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            startActivity(uninstallIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public boolean onPrepareOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
    public void addUri(String result){
        ratingUriList.add(result);
    }

    public  double[]  getStoreLocation(String strAddress){
        double[] coordinates= new double[2];
        Geocoder coder = new Geocoder(this,Locale.ENGLISH);
        try {
            List<Address> destBuffer = coder.getFromLocationName(strAddress, 1);

            if (destBuffer != null && destBuffer.size() > 0) {
                Address dest1 = destBuffer.get(0);
                coordinates[0] = dest1.getLatitude();
                coordinates[1] = dest1.getLongitude();
                return coordinates;
            }
            else {
                Log.e(TAG, "Couldn't reverse geocode dest1");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public boolean getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this,Locale.ENGLISH);
        try {
            List<Address> destBuffer = coder.getFromLocationName(strAddress, 1);

            if (destBuffer != null && destBuffer.size() > 0) {
                Address dest1 = destBuffer.get(0);

                double earthRadius = 3958.75;
                double dLat = Math.toRadians(dest1.getLatitude()-currentLatitude);
                double dLng = Math.toRadians(dest1.getLongitude()-currentLongitude);
                double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(Math.toRadians(currentLatitude)) * Math.cos(Math.toRadians(dest1.getLatitude())) *
                                Math.sin(dLng/2) * Math.sin(dLng/2);

                double c = 2 * Math.asin(Math.sqrt(a));
                double dist = earthRadius * c;

                if(dist<4)
                {
                    distanceList.add(dist);
                    return true;
                }
                return false;
            }
            else {
                Log.e(TAG, "Couldn't reverse geocode dest1");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
