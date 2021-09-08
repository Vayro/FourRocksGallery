package com.vayrotech.fourrocksgallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      toFrameActivity();


//test








    }



    //to main frame activity
    public void toFrameActivity() {
       // historyString = loadLog();
        Intent intent = new Intent(this, FrameActivity.class);
      //  intent.putExtra("log", historyString);
        //intent.putExtra("filename", FILE_NAME);
        startActivity(intent);
    }




}