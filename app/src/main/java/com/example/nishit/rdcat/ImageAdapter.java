package com.example.nishit.rdcat;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    static int imageTotal;
    public static String[] mThumbIds=new String[10];

    public void arrayinit(String a[],int n)
    {
        int i;
        for(i=0;i<n;i++)
        {
            mThumbIds[i]=a[i];
        }
        imageTotal=n;
    }

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imageTotal;
    }

    @Override
    public String getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480, 480));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = getItem(position);

        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.icon1)
                .fit()
                .centerCrop().into(imageView);
        return imageView;
    }
}