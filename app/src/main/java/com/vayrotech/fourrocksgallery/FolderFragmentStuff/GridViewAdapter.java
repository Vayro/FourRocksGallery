package com.vayrotech.fourrocksgallery.FolderFragmentStuff;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;
import com.vayrotech.fourrocksgallery.FolderFragmentStuff.Model_images;
import com.vayrotech.fourrocksgallery.R;


import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GridViewAdapter extends ArrayAdapter<Model_images> {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();
    int int_position;
    ModelDatabase DB;
    PhotosFragment Photosfragment;



    public GridViewAdapter(Context context, ArrayList<Model_images> al_menu,int int_position, PhotosFragment Photosfragment) {
        super(context, R.layout.adapter_photosfolder, al_menu);
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;
        this.Photosfragment = Photosfragment;



    }

    @Override
    public int getCount() {

        Log.e("ADAPTER LIST SIZE", al_menu.get(int_position).getAl_imagepath().size() + "");
        return al_menu.get(int_position).getAl_imagepath().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.get(int_position).getAl_imagepath().size() > 0) {
            return al_menu.get(int_position).getAl_imagepath().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public Map<String, String> getData(int position){


        String path = al_menu.get(int_position).getAl_imagepath().get(position);
        File file = new File(path);
        Date lastModified =new Date(file.lastModified());
        String date = lastModified.toString();
        String title = file.getName();

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("title", title);
        dataMap.put("path", path);
        dataMap.put("date", date);

        return dataMap;
    }






    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photosfolder, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.tv_folder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.tv_folder2);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setVisibility(View.GONE);
       // viewHolder.tv_foldersize.setVisibility(View.GONE);


        String path=al_menu.get(int_position).getAl_imagepath().get(position);
        File file = new File(path);
        Date lastModDate = new Date(file.lastModified());


        viewHolder.tv_foldersize.setText(lastModDate.toString());
        Glide.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image);



        Photosfragment.databaseUpdate(getData(position));






        return convertView;

    }

    private class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;



    }

















}