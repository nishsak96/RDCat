package com.example.nishit.rdcat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void toGrid(View v) {

        Intent i = new Intent(this, ImageList.class);
        i.putExtra("link", "http://ratnadeep.pe.hu/");
        i.putExtra("limit", "http://ratnadeep.pe.hu/limit.txt");
        startActivity(i);

    }


    public void toGrid1(View v) {
        ArrayList<String> images = new ArrayList<>();
//        images.add("http://sourcey.com/images/stock/salvador-dali-metamorphosis-of-narcissus.jpg");
//        images.add("http://sourcey.com/images/stock/salvador-dali-the-dream.jpg");
//        images.add("http://sourcey.com/images/stock/salvador-dali-persistance-of-memory.jpg");
//        images.add("http://sourcey.com/images/stock/simpsons-persistance-of-memory.jpg");
//        images.add("http://sourcey.com/images/stock/salvador-dali-the-great-masturbator.jpg");

        Intent intent = new Intent(this, Gallery.class);
        intent.putStringArrayListExtra(Gallery.EXTRA_NAME, images);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Categories Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}