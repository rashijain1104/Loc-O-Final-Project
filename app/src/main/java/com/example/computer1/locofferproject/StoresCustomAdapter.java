package com.example.computer1.locofferproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StoresCustomAdapter extends ArrayAdapter<StoreData> {
    protected ArrayList<StoreData> storeList;
    protected ArrayList<Double> distanceList;
    private  String consumerKey,consumerSecret,token,tokenSecret,storeURI;
    Context context;
    Double currentLat, currentLon;
    String category;
    View row;
    byte[] decodedString;
    public StoresCustomAdapter(Context context, int resource, ArrayList<StoreData> storeList, ArrayList<Double> distanceList, Double currentLat, Double currentLon,String category) {
        super(context, resource, storeList);
        this.storeList = storeList;
        this.distanceList = distanceList;
        this.currentLat = currentLat;
        this.currentLon = currentLon;
        this.category= category;
        consumerKey = "ylAFXnFNueOKCNkAtM9DlA";
        consumerSecret = "grG4EFVszWiRfIIlUtspOVs0vRo";
        token = "fv95yldwZTOosB6AwYnRWI_cluD1Y7kJ";
        tokenSecret = "jk1ouFv9xh_dS5qC5w_IJF_Kqk0";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        final  StoreData store = storeList.get(position);
        Double distance = distanceList.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.store_custom_row, null);

        storeURI = store.getImageFile();
        decodedString = Base64.decode(storeURI, Base64.DEFAULT);
        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        new  AsyncTask<Object, Void, Bitmap>(){
            private View row;
            @Override
            protected Bitmap doInBackground(Object... params) {
                row = (View) params[0];
                Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
                String responses = yelp.search(store.getName(), store.getStoreLat(), store.getStoreLong());
                YelpParser yParser = new YelpParser();
                yParser.setResponse(responses);
                try {
                    yParser.parseBusiness();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int i = 0;
                String rating_url = yParser.getRatingURL(i);
                String b_name = yParser.getBusinessName(i);
                URL url = null;
                try {
                    url = new URL(rating_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {


                ImageView defaultImage = (ImageView) row.findViewById(R.id.defaultImage);
                defaultImage.setImageBitmap(decodedByte);

                ImageView ratingImage = (ImageView) row.findViewById(R.id.ratingImageView);
                ratingImage.setImageBitmap(result);

                row.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String sturi = store.getImageFile();
                        byte[] decodedStr = Base64.decode(sturi, Base64.DEFAULT);
                        Intent infoIntent = new Intent(context, StoreInfoActivity.class);
                        infoIntent.putExtra("storeName", store.getName());
                        infoIntent.putExtra("storeAddress", store.getAddress());
                        infoIntent.putExtra("storeImage", decodedStr);
                        infoIntent.putExtra("storeOffer", store.getOffer());
                        infoIntent.putExtra("storeLat", store.getStoreLat());
                        infoIntent.putExtra("storeLong", store.getStoreLong());
                        infoIntent.putExtra("currentLat", currentLat);
                        infoIntent.putExtra("currentLong", currentLon);
                        context.startActivity(infoIntent);
                    }
                });

            }
        }.execute(row);

        TextView storeNameTextView = (TextView) row.findViewById(R.id.storeNameTextView);
        storeNameTextView.setText(store.getName());

        double x = Math.floor(distance * 10) / 10;
        TextView distanceTextView = (TextView) row.findViewById(R.id.distanceTextView);
        distanceTextView.setText(String.valueOf(x)+"mi");
        return row;
    }
}
