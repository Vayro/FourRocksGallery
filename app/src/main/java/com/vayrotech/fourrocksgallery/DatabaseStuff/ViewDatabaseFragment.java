package com.vayrotech.fourrocksgallery.DatabaseStuff;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vayrotech.fourrocksgallery.FrameActivity;
import com.vayrotech.fourrocksgallery.GalleryFragmentStuff.Cell;
import com.vayrotech.fourrocksgallery.GalleryFragmentStuff.GalleryFragmentActivity;
import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewDatabaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewDatabaseFragment extends Fragment {


    ListView dbList;
    List<Cell> allFilesDB;
    ModelDatabase DB;
    String tableName;
    boolean justCleared = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewDatabaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewDatabaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewDatabaseFragment newInstance(String param1, String param2) {
        ViewDatabaseFragment fragment = new ViewDatabaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        tableName=this.getArguments().getString("tableName");
        justCleared=getArguments().getBoolean("justCleared");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_database, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dbList = getView().findViewById(R.id.dbListView);
        DB = new ModelDatabase(this.getContext());
        allFilesDB = new ArrayList<>();






        dbList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String path = allFilesDB.get(i).getPath();
                Log.d("onclick", path);


                Bundle bundle = new Bundle();
                Uri uri = Uri.parse(path);
                bundle.putParcelable("passedImage", uri);

                SelectedFragmentActivity selectedFragmentActivity = new SelectedFragmentActivity();
                selectedFragmentActivity.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragmentActivity).addToBackStack(null).commit();














            }







        });








        if(justCleared!=true) {
            allFilesDB = listDBFiles();
            populateList(allFilesDB);









        }



    }


    //database custom menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.db_menu, menu);
    }


    private List<Cell> listDBFiles() {
        List<Cell> allFiles = new ArrayList<>();
        Cursor res = DB.getData();
        if (res.getCount() == 0) {
            Toast.makeText(getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();
            return null;
        }


        while (res.moveToNext()) {
            Cell cell = new Cell();
            cell.setTitle(res.getString(0));
            cell.setPath(res.getString(1));


            String sDate = new SimpleDateFormat("MM/dd/yyyy ',' hh:mm a").format(new Date(res.getString(2)));
            Date date = new Date(sDate);

            cell.setDate(date);

            cell.setClassification(res.getString(3));

            allFiles.add(cell);
        }


        return allFiles;


    }


    private void populateList(List<Cell> data) {



        DatabaseListAdapter arrayAdapter = new DatabaseListAdapter(getContext(), allFilesDB);
        dbList.setAdapter(arrayAdapter);



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearDBMenu:
                //clear database

                Toast.makeText(getActivity(), "clearing database", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to clear the whole database? (Can't be undone)");

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DB.clearDatabase(tableName);
                        dialog.dismiss();
                        fragmentRefresh();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


                return true;


        }
        return true;
    }

    private void fragmentRefresh() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("justCleared", true);

        ViewDatabaseFragment viewDatabaseFragment = new ViewDatabaseFragment();
        viewDatabaseFragment.setArguments(bundle);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, viewDatabaseFragment).addToBackStack(null).commit();
    }


    //refresh frag




}


