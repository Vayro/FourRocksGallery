package com.vayrotech.fourrocksgallery.FolderFragmentStuff;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;
import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import java.util.ArrayList;
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
    ArrayList<Model_images> this_al_images;


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
        setHasOptionsMenu(true); //set fragment-specific menu
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

        Log.d("AL_IMAGES TO STRING", FoldersFragment.al_images.toString());
        gridView = getView().findViewById(R.id.gv_photos);
        int_position = this.getArguments().getInt("position");
        adapter = new GridViewAdapter(getContext(), FoldersFragment.al_images, int_position, PhotosFragment.this);
        gridView.setAdapter(adapter);
        DB = new ModelDatabase(this.getContext()); //create the database object
        this_al_images = FoldersFragment.al_images;

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
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




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String position = String.valueOf(int_position);
        String[] menuSort = {"", position};

        switch (item.getItemId()) {
            case R.id.dateDescending:
                //sort date ascending stuff here
                menuSort[0] = MediaStore.Images.Media.DATE_TAKEN;
                Toast.makeText(getActivity(), "Sorted by date (ascending)!", Toast.LENGTH_SHORT).show();
                refreshFragment(menuSort);
               // showImages("dateA");
                return true;
            case R.id.nameAscending:
                //sort name ascending stuff here
                menuSort[0] = "1";
                Toast.makeText(getActivity(), "Sorted by name (ascending)!", Toast.LENGTH_SHORT).show();
                refreshFragment(menuSort);
              //  showImages("nameA");
                return true;
            case R.id.nameDescending:
                //sort name descending stuff here
                menuSort[0] = "2";
                Toast.makeText(getActivity(), "Sorted by name (descending)!", Toast.LENGTH_SHORT).show();
                refreshFragment(menuSort);
               // showImages("nameD");
                return true;

        }

        return true;
    }

    public void refreshFragment(String[] menuSort){
        //sends back to folderFragment with new sort code, which should start a new PhotosFragment
        //bundle URI and change fragment
        Bundle bundle = new Bundle();
        bundle.putStringArray("MenuSort", menuSort);
        bundle.putBoolean("passing", true);
        bundle.putParcelableArrayList("passedArrayList", (ArrayList<? extends Parcelable>) this_al_images);
        Log.d("menuSort", "MenuSort= "+ menuSort[0] + " " + menuSort[1]);
        Log.d("this_al_images", this_al_images.toString());
        FoldersFragment foldersFragment = new FoldersFragment();
        foldersFragment.setArguments(bundle);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, foldersFragment).addToBackStack(null).commit();




    }





    }






