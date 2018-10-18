package com.study.android.camera1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";
    private static int REQ_CODE_IMAGE_CAPTURE =1001;

    ImageView imageView1;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 =findViewById(R.id.imageView1);


    }



    public void onBtn1Clicked(View v) {
        takePicture();
    }

    private void takePicture() {
        File externalStorageFile = Environment.getExternalStorageDirectory();
        Log.d(TAG, externalStorageFile.getAbsolutePath());
        String imageFilename = "capture01.jpg";

        imageUri = Uri.fromFile(new File(externalStorageFile, imageFilename));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQ_CODE_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_IMAGE_CAPTURE) {
            if (imageUri != null){
                showCapturedImage();
            }
        }
    }

    private void showCapturedImage() {
        String imagePath = imageUri.getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, false);
        bitmap.recycle();
        imageView1.setImageBitmap(scaledBitmap);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getApplicationContext(), "세로 방향으로 변경됨.", Toast.LENGTH_LONG).show();
            ;

            if (imageUri != null) {
                showCapturedImage();
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(getApplicationContext(), " 가로방향으로 변경됨.", Toast.LENGTH_LONG).show();
            if (imageUri != null) {
                showCapturedImage();
            }
        }
    }
}
