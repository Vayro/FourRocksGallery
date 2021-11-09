package com.vayrotech.fourrocksgallery.FolderFragmentStuff;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;
import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment  {

    int int_position;
    private GridView gridView;
    GridViewAdapter adapter;
    ModelDatabase DB;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        super.onCreate(savedInstanceState);


        gridView = getView().findViewById(R.id.gv_photos);
        int_position = this.getArguments().getInt("position");
        adapter = new GridViewAdapter(getContext(), FoldersFragment.al_images, int_position, PhotosFragment.this);
        gridView.setAdapter(adapter);
        DB = new ModelDatabase(this.getContext()); //create the database object


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
/*
                Toast.makeText(getContext(), "hi " + position, Toast.LENGTH_SHORT).show();
                String path = FoldersFragment.al_images.get(position).getStr_folder();
                Uri uri = adapter.getUri(position);
                Toast.makeText(getContext(), "path " + uri, Toast.LENGTH_SHORT).show();
*/

               // Uri uri = adapter.getUri(position);

                Map<String, String> data = adapter.getData(position);
                Toast.makeText(getContext(), "Title " + data.get("title"), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(data.get("path"));


                //bundle URI and change fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("passedImage", uri);
                Log.d("imagepath", "imagepath "+ uri.toString());
                SelectedFragmentActivity SelectedFragmentActivity = new SelectedFragmentActivity();
                SelectedFragmentActivity.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, SelectedFragmentActivity).addToBackStack(null).commit();




            }
        });




    }

    public void databaseUpdate(Map<String, String> dataMap){
        String dbPath = dataMap.get("path");
        String dbTitle =  dataMap.get("title");
        String dbDate = dataMap.get("date");

          /* TODO: DATABASE STUFF
        for(Cell i : allFilesPaths) {
            String dbPath = i.getTitle();
            String dbTitle = i.getPath();
            String dbDate = i.getDate().toString();
*/
            //TODO THIS VARIABLE IS FOR TENSOR FLOW CLASSIFICATION
            String  dbClass = "test";


            Boolean checkinsertdata = DB.insertData(dbPath, dbTitle, dbDate, dbClass);
            if (checkinsertdata == true) {
                Toast.makeText(this.getContext(), "New Entry Inserted in database", Toast.LENGTH_SHORT).show();
                Log.d("datebase", "databaseUpdate: Path " + dbPath + "| Title " + dbTitle + "| Date " + dbDate);

            } else {
                // Toast.makeText(this.getContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        }


    }






