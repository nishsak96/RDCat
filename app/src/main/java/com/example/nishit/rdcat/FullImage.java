package com.example.nishit.rdcat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;


import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.bitmap;
import static android.R.attr.mode;


public class FullImage extends Activity {

    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;
    ProgressDialog pDialog;
    int position;

    ImageView img;
    Bitmap bitmap, wall;

    //TextView myTouchEvent;
    //ImageView myImageView;
    int bmpWidth, bmpHeight;

    //Touch event related variables
    int touchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    float dist0, distCurrent;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent i = getIntent();
        position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        img = (ImageView) findViewById(R.id.FullImage);
        String url = imageAdapter.getItem(position);
        new DownloadImage().execute(url);


    }

    private void drawMatrix(){
        float curScale = distCurrent/dist0;
        if (curScale < 0.1){
            curScale = 0.1f;
        }

        Bitmap resizedBitmap;
        int newHeight = (int) (bmpHeight * curScale);
        int newWidth = (int) (bmpWidth * curScale);
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        img.setImageBitmap(resizedBitmap);
    }

    View.OnTouchListener MyOnTouchListener
            = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub

            float distx, disty;

            switch(event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    //A pressed gesture has started, the motion contains the initial starting location.
                    //myTouchEvent.setText("ACTION_DOWN");
                    touchState = TOUCH;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //A non-primary pointer has gone down.
                    //myTouchEvent.setText("ACTION_POINTER_DOWN");
                    touchState = PINCH;

                    //Get the distance when the second pointer touch
                    distx = event.getX(0) - event.getX(1);
                    disty = event.getY(0) - event.getY(1);
                    dist0 = (float)Math.sqrt(distx * distx + disty * disty);

                    break;
                case MotionEvent.ACTION_MOVE:
                    //A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
                    //myTouchEvent.setText("ACTION_MOVE");

                    if(touchState == PINCH){
                        //Get the current distance
                        distx = event.getX(0) - event.getX(1);
                        disty = event.getY(0) - event.getY(1);
                        distCurrent = (float)Math.sqrt(distx * distx + disty * disty);

                        drawMatrix();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    //A pressed gesture has finished.
                    //myTouchEvent.setText("ACTION_UP");
                    touchState = IDLE;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //A non-primary pointer has gone up.
                    //myTouchEvent.setText("ACTION_POINTER_UP");
                    touchState = TOUCH;
                    break;
            }

            return true;
        }

    };


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        public DownloadImage()
        {
            super();
        }

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
            wall = result;
            myImage(wall);
        }
    }

    public void myImage(Bitmap bittu)
    {
        bitmap = bittu;
        bmpWidth = bitmap.getWidth();
        bmpHeight = bitmap.getHeight();

        distCurrent = 1; //Dummy default distance
        dist0 = 10;   //Dummy default distance
        drawMatrix();

        img.setOnTouchListener(MyOnTouchListener);
        touchState = IDLE;
    }

    public void FullClick(View v) {
        alert();
    }

    void alert() {
        new AlertDialog.Builder(this)
                .setNeutralButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        savewall();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();



    }


    void setwall() {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        try {
            myWallpaperManager.setBitmap(wall);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    void savewall() {
        try {
            File mydir = new File(Environment.getExternalStorageDirectory() + "./");

            if (!mydir.exists())
                mydir.mkdirs();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            wall.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            InputStream in = new ByteArrayInputStream(stream.toByteArray());

            //InputStream in = getResources().openRawResource(R.id.Fullimage);
            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + position + ".png");
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } catch (Exception e) {
            Toast.makeText(this, "kuch galat hai", Toast.LENGTH_SHORT).show();
        }
    }

//    void savewalll() {
//        try {
//            File sdCardFile = Environment.getExternalStorageDirectory();
//            sdCardFile.setWritable(true);
//            if (sdCardFile.canWrite() == true) {
//                File viewerFile = new File(sdCardFile, "Viewer");
//                viewerFile.mkdir();
//                File imageFile = new File(viewerFile, "IMG_" + position + ".png");
//                FileOutputStream fileStream = new FileOutputStream(imageFile);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
//                fileStream.close();
//            } else {
//                Log.e("TAG", "IOUtility - Cannot write to SD Card");
//            }
//
//        } catch (Exception e) {
//            Log.e("TAG", "IOUtility - Error - " + e);
//            e.printStackTrace();
//        }
//
//    }


//    void savewall1()
//    {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,position+".jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("TAG","ert");
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}





//public class IOUtility {
//    private static String mPrependFileName = "IMG_";
//    private static final String TAG = "IOUtility";
//
//    public static boolean saveImageToSDCard(Bitmap b, String appendFileName) {
//        if (b != null) {
//            try {
//                File sdCardFile = Environment.getExternalStorageDirectory();
//                if (sdCardFile.canWrite() == true) {
//                    File viewerFile = new File(sdCardFile, "Viewer");
//                    viewerFile.mkdirs();
//                    File imageFile = new File(viewerFile, mPrependFileName + appendFileName + ".png");
//                    FileOutputStream fileStream = new FileOutputStream(imageFile);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream);
//                    fileStream.close();
//                } else {
//                    Log.e(TAG, "IOUtility - Cannot write to SD Card");
//                }
//                return true;
//            } catch (Exception e) {
//                Log.e(TAG, "IOUtility - Error - " + e);
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//




//@Override
//public boolean onTouch(View v, MotionEvent event) {
//        TextView textView = (TextView) findViewById(R.id.text);
//
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//        case MotionEvent.ACTION_POINTER_DOWN:
//        oldDist = spacing(event);
//        Log.d(TAG, "oldDist=" + oldDist);
//        if (oldDist > 10f) {
//        mode = ZOOM;
//        Log.d(TAG, "mode=ZOOM" );
//        }
//        break;
//        case MotionEvent.ACTION_POINTER_UP:
//        mode = NONE;
//        break;
//        case MotionEvent.ACTION_MOVE:
//        if (mode == ZOOM) {
//        float newDist = spacing(event);
//        // If you want to tweak font scaling, this is the place to go.
//        if (newDist > 10f) {
//        float scale = newDist / oldDist;
//
//        if (scale > 1) {
//        scale = 1.1f;
//        } else if (scale < 1) {
//        scale = 0.95f;
//        }
//
//        float currentSize = textView.getTextSize() * scale;
//        if ((currentSize < MAX_FONT_SIZE && currentSize > MIN_FONT_SIZE)
//        ||(currentSize >= MAX_FONT_SIZE && scale < 1)
//        || (currentSize <= MIN_FONT_SIZE && scale > 1)) {
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentSize);
//        }
//        }
//        }
//        break;
//        }
//        return false;
//        }
