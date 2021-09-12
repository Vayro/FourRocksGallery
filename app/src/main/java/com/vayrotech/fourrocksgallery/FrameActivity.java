package com.vayrotech.fourrocksgallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FrameActivity extends AppCompatActivity {


    public static final int CAMERA_REQUEST_CODE = 69;
    public static final int CAMERA_PASS_REQUEST_CODE = 420;

    Bitmap selectedImage; //declare selected image bitmap; this will be passed to the "selected" fragment

    //declare buttons
    Button cameraButton, galleryButton, selectedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);


    //initialize bitmap placeholder
    selectedImage = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
    selectedImage.eraseColor(android.graphics.Color.RED);


    //initialize buttons
    cameraButton = findViewById(R.id.buttonCamera);
    galleryButton = findViewById(R.id.buttonGallery);
    selectedButton = findViewById(R.id.buttonSelected);

    //set onClickListeners
    cameraButton.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            Toast.makeText(FrameActivity.this,"Camera", Toast.LENGTH_SHORT).show();
     //stuff

            askCameraPermissions();








        }
    });















    galleryButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            replaceFragment(new GalleryFragmentActivity());


        }
    });

    selectedButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        replaceFragment(new SelectedFragmentActivity());
        }
    });

















    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askCameraPermissions() {

        //permission for camera request
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);}
        else
            {
                openCamera();
            }



        }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                openCamera();
            }else{
                Toast.makeText(this,"Requires permission to use camera.",Toast.LENGTH_SHORT).show();

            }


        }



    }


    //camera stuff ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            selectedImage = (Bitmap) data.getExtras().get("data");


        }


    }
//end camera stuff ~~~~~~~~~~~~~~~~~~~~~~~~~



    //load fragment
    private void replaceFragment(Fragment fragment) {


        Bundle bundle = new Bundle();
        bundle.putParcelable("passedImage", selectedImage);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();




    }








}
