package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;
//lists all the MF images in RecycleView

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Cell> galleryList;
    private GalleryListener mGalleryListener;
    private Context context;

    public MyAdapter(Context context, ArrayList<Cell> galleryList, GalleryListener galleryListener){
        this.galleryList = galleryList;
        this.context = context;
        this.mGalleryListener = galleryListener;


    }


    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell, viewGroup, false);
        return new MyAdapter.ViewHolder(view, mGalleryListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageFromPath(galleryList.get(i).getPath(), viewHolder.img, viewHolder.imgDate);


        /* move this to interface for better performance


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




        viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
               // Toast.makeText(v.getContext(), "this should open a menu",      Toast.LENGTH_LONG).show();
                deleteImage(v.getContext(), Uri.parse(galleryList.get(i).getPath()), galleryList.get(i).getTitle() );
                return true;
            }
        });
*/



    }



    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView imgDate;
        GalleryListener galleryListener;

        public ViewHolder(View view, GalleryListener galleryListener){
            super(view);

            img = (ImageView) view.findViewById(R.id.img);
            imgDate = (TextView) view.findViewById(R.id.imgDate);
            this.galleryListener = galleryListener;

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            galleryListener.onGalleryClick(getAdapterPosition());
        }
    }







    private void setImageFromPath(String path, ImageView image, TextView date){
        File imgFile = new File(path);
        if (imgFile.exists()){

         //   SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss a, vvv");
            String lastModDate = new SimpleDateFormat("MM/dd/yyyy ',' hh:mm:ss a").format(new Date(imgFile.lastModified()));
          //  Date lastModDate = new Date(imgFile.lastModified());



            Bitmap myBitmap = ImageHelper.decodeSampledBitmapFromPath(imgFile.getAbsolutePath(), 100,100);
            image.setImageBitmap(myBitmap);
            date.setText(lastModDate.toString());



        }

    }









public interface GalleryListener{
        void onGalleryClick(int position);

}










    /*

    private void deleteImage(Context context, Uri uri, String filename){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete");
        builder.setMessage("Delete image?");

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //delete file stuff


            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }




*/






}
