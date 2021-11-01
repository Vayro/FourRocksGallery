package com.vayrotech.fourrocksgallery.DatabaseStuff;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.vayrotech.fourrocksgallery.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vayrotech.fourrocksgallery.GalleryFragmentStuff.Cell;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseListAdapter extends ArrayAdapter<Cell> {

    private ArrayList<Cell> dbList;
    private Context context;


    public DatabaseListAdapter(Context context, List<Cell> cells) {
        super(context, 0, cells);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Cell cell = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.db_cell, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.titleText);
        TextView tvDate = (TextView) convertView.findViewById(R.id.dateText);
        TextView tvPath = (TextView) convertView.findViewById(R.id.pathText);
        TextView tvClassification = (TextView) convertView.findViewById(R.id.classificationText);



        // Populate the data into the template view using the data object
        tvTitle.setText(cell.getTitle());




        try{
        Date d = cell.getDate();
        if(d!=null){
        tvDate.setText(d.toString());}
        else{
            tvDate.setText("No Date Modified Information");
        }
        }catch (Exception e){
            tvDate.setText("No Date Modified Information");
        }






        tvPath.setText(cell.getPath());
        tvClassification.setText(cell.getClassification());





        return convertView;
    }





}
