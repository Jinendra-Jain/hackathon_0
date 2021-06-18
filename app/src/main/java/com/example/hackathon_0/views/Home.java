package com.example.hackathon_0.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.hackathon_0.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2;
    ArFragment arFragment;
    private ModelRenderable bearRenderable;

    ImageView bear;
    private ImageButton fragment_loggedin_logOut;
    Button btnCapture;
    Bitmap bm;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        fragment_loggedin_logOut = findViewById(R.id.fragment_loggedin_logOut);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        //View

        bear = (ImageView) findViewById(R.id.bear);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        askForPermissions();

        bear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ModelRenderable.builder()
                        .setSource(Home.this, R.raw.bear)
                        .build().thenAccept(renderable -> bearRenderable = renderable)
                        .exceptionally(
                                throwable -> {
                                    Toast.makeText(Home.this, "Unnable to load bear model", Toast.LENGTH_SHORT).show();
                                    return null;
                                }
                        );
            }
        });

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                // When user tap on plane, we will add model

                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
                bear.setParent(anchorNode);
                bear.setRenderable(bearRenderable);
                bear.select();

            }
        });

        fragment_loggedin_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vHomeIntentLoginOTPPageActivity = new Intent(Home.this, MainActivity.class);
                startActivity(vHomeIntentLoginOTPPageActivity);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArSceneView sceneView = arFragment.getArSceneView();
                bm = getScreenShot(sceneView);
                imageUri = getImageUri(Home.this, bm);

                Intent PIHA = new Intent(Home.this, Photo.class);
                PIHA.putExtra("imageUri", imageUri.toString());
                startActivity(PIHA);
            }
        });

    }

    private void askForPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Home.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }


    public static Bitmap getScreenShot(View view) {

        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image", null);
        return Uri.parse(path);

    }

}