
package com.example.computer1.locofferproject;

import android.content.Intent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

    String [] result;
    Context context;
    int [] imageId;
    double userLat,userLon;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Context context, int resource, String[] prgmNameList, int[] prgmImages, double userLat, double userLon) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        imageId=prgmImages;
        this.userLat =userLat;
        this.userLon=userLon;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
       context = parent.getContext();

        rowView = inflater.inflate(R.layout.programlist, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, secondPageActivity.class);
                intent.putExtra("categoryName", result[position]);
                intent.putExtra("userLat", userLat);
                intent.putExtra("userLon", userLon);
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}