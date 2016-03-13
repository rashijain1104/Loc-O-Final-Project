package com.example.computer1.locofferproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreInfoActivity extends AppCompatActivity {

    private TextView name,address, offer;
    private double storelat, storelon, currentlat, currentlon;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://maps.google.com/maps?saddr=" + currentlat + "," + currentlon + "&daddr=" + storelat + "," + storelon;
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            }
        });

        Bundle storeInfo = getIntent().getExtras();

        name= (TextView) findViewById(R.id.nameTextView);
        address= (TextView) findViewById(R.id.addressTextView);
        offer= (TextView) findViewById(R.id.offerTextView);
        imgView= (ImageView) findViewById(R.id.storeImageView);
        name.setText(storeInfo.getString("storeName"));
        address.setText(storeInfo.getString("storeAddress"));
        offer.setText(storeInfo.getString("storeOffer"));

        byte[] byteArray = storeInfo.getByteArray("storeImage");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imgView.setImageBitmap(bmp);

        storelat = storeInfo.getDouble("storeLat");
        storelon = storeInfo.getDouble("storeLong");
        currentlat = storeInfo.getDouble("currentLat");
        currentlon = storeInfo.getDouble("currentLong");
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

}
