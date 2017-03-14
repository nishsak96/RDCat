package com.example.nishit.rdcat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.io.InputStream;

import static com.example.nishit.rdcat.R.id.gridview;

public class ImageDetails extends AppCompatActivity {


        ProgressDialog pDialog;

        ImageView img;
        Bitmap wall;
        int position;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image_details);

            Intent i = getIntent();
            position = i.getExtras().getInt("id");
            ImageAdapter imageAdapter = new ImageAdapter(this);

            img = (ImageView) findViewById(R.id.imgDet);
            String url = imageAdapter.getItem(position);

            new DownloadImage().execute(url);
        }

        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... URL) {
                String str = null;
                String imageURL = URL[0];
                Bitmap bitmap = null;
                try {
                    InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                img.setImageBitmap(result);
                wall=result;
            }
        }


        public void toFullImage(View view) {
            Intent i = new Intent(getApplicationContext(), FullImage.class);
            i.putExtra("id",position );
            startActivity(i);
        }
    }

