package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;
//lists all the MF images in RecycleView

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galleryList.get(i).getPath(), viewHolder.img, viewHolder.imgDate);
        viewHolder.img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Following toast shows full path
                Toast.makeText(context, galleryList.get(i).getPath(), Toast.LENGTH_SHORT).show();
                //Set the path of the photo picked in gallery to a Uri object

                Uri uri = Uri.parse(galleryList.get(i).getPath());


                //bundle URI and change fragment
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
        private TextView imgDate;
        public ViewHolder(View view){
            super(view);

            img = (ImageView) view.findViewById(R.id.img);
            imgDate = (TextView) view.findViewById(R.id.imgDate);


        }


    }

    private void setImageFromPath(String path, ImageView image, TextView date){
        File imgFile = new File(path);
        if (imgFile.exists()){

         //   SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss a, vvv");
            String lastModDate = new SimpleDateFormat("MM/dd/yyyy ',' hh:mm:ss a").format(new Date(imgFile.lastModified()));
          //  Date lastModDate = new Date(imgFile.lastModified());



            Bitmap myBitmap = ImageHelper.decodeSampledBitmapFromPath(imgFile.getAbsolutePath(), 200,200);
            image.setImageBitmap(myBitmap);
            date.setText(lastModDate.toString());



        }

    }












}
