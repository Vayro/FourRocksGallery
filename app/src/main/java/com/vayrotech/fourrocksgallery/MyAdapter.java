package com.vayrotech.fourrocksgallery;
//lists all the MF images in RecycleView

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Cell> galleryList;
    private Context context;

    public MyAdapter(Context context, ArrayList<Cell> galleryList){
        this.galleryList = galleryList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell, viewGroup, false);
        return new MyAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galleryList.get(i).getPath(), viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //send to selected from here
                Toast.makeText(context, "image clicked " + galleryList.get(i).getTitle(), Toast.LENGTH_SHORT).show();

                Uri uri = Uri.parse(galleryList.get(i).getPath());
                Bundle bundle = new Bundle();
                bundle.putParcelable("passedImage", uri);

                SelectedFragmentActivity SelectedFragmentActivity = new SelectedFragmentActivity();
                SelectedFragmentActivity.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, SelectedFragmentActivity).addToBackStack(null).commit();



            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        public ViewHolder(View view){
            super(view);

            img = (ImageView) view.findViewById(R.id.img);


        }


    }

    private void setImageFromPath(String path, ImageView image){
        File imgFile = new File(path);
        if (imgFile.exists()){
            Bitmap myBitmap = ImageHelper.decodeSampledBitmapFromPath(imgFile.getAbsolutePath(), 200,200);
            image.setImageBitmap(myBitmap);
        }

    }












}
