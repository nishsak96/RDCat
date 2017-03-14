package com.example.nishit.rdcat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageList extends AppCompatActivity {
    static int limit=0;
    String linkurl,limiturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        Intent intent = getIntent();
        linkurl = intent.getStringExtra("link");
        limiturl = intent.getStringExtra("limit");
        String url=null;
        try
        {
            new DownloadImage().execute(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.e("chel","kya pata");
        //Toast.makeText(this," "+limit,Toast.LENGTH_SHORT).show();
    }


    class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {
            String str=null;

            Bitmap bitmap = null;
            try
            {
                // Create a URL for the desired page
                URL url = new URL(limiturl);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                str = in.readLine();
                in.close();
                if(!str.equals(null))
                    limit=Integer.parseInt(str);
            }
            catch (MalformedURLException e)
            {
            }
            catch (IOException e)
            {
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            final GridView gridview = (GridView) findViewById(R.id.gridview);
            ImageAdapter ia=new ImageAdapter(ImageList.this);
            String a[]=new String[limit];
            int i;
            for(i=1;i<limit;i++)
            {
                a[i-1]="http://ratnadeep.pe.hu/"+i+".jpg";
            }
            ia.arrayinit(a,limit);
            gridview.setAdapter(ia);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), ImageDetails.class);
                    i.putExtra("id", position);
                    startActivity(i);
                }
            });
        }
    }
}
