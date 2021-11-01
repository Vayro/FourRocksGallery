package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;
import com.vayrotech.fourrocksgallery.R;
import com.vayrotech.fourrocksgallery.SelectedFragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class GalleryFragmentActivity extends Fragment implements MyAdapter.GalleryListener {


    View view;
    List<Cell> allFilesPaths;
    final String defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    String folderPath = defaultPath;
    ModelDatabase DB;
    TableLayout rView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public GalleryFragmentActivity() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragmentActivity newInstance(String param1, String param2) {
        GalleryFragmentActivity fragment = new GalleryFragmentActivity();
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_gallery_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //created stuff goes here


        DB = new ModelDatabase(this.getContext()); //create the database object


        folderPath = this.getArguments().getString("pathToPass");
        if (folderPath == null) {
            folderPath = defaultPath;
        } else {
            Log.d("PATH PASSED:", folderPath);
        }


        //for the storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&

                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            //show the images
            Toast.makeText(getActivity(), "images loading", Toast.LENGTH_SHORT).show();
            showImages("");




        }

/*
//onClick events
        rView = getView().findViewById(R.id.tableOnClick);
        rView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/














    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
    }


//Gallery Main Processes

    // this shows the images on the screen
    private void showImages(String sort) {
        //this is the folder with all the images
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/";
        String path;

        if (folderPath != defaultPath) {
            path = folderPath; //External/Public directory
            Log.d("Full Folder Path", "Folder Path: " + path);
        } else {
            path = folderPath;
            Log.d("Full Folder Path", "Default Path: " + path);
        }


        allFilesPaths = new ArrayList<>();
        allFilesPaths = listAllFiles(path);
        Collections.sort(allFilesPaths, Collections.reverseOrder());











        //i need to check array list for trashed items and delete them here:
        for(Cell i : allFilesPaths) {
            String penis = i.getTitle();
            Log.d("Title", penis);
            if(penis.startsWith("."))
            {
                Log.e("SHOULD DELETE THIS CELL ", penis);
            }



        }
        allFilesPaths.removeIf(i -> i.getTitle().startsWith("."));





        //we'll update the database with the new arraylist



        for(Cell i : allFilesPaths) {
          String dbPath = i.getTitle();
           String dbTitle = i.getPath();
           String dbDate = i.getDate().toString();

            //TODO THIS VARIABLE IS FOR TENSOR FLOW CLASSIFICATION
            String  dbClass = "test";


            Boolean checkinsertdata = DB.insertData(dbPath, dbTitle, dbDate, dbClass);
            if (checkinsertdata == true) {
                Toast.makeText(this.getContext(), "New Entry Inserted in database", Toast.LENGTH_SHORT).show();

            } else {
               // Toast.makeText(this.getContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
            }
        }







        switch (sort) {
            case "dateA":
                Collections.sort(allFilesPaths);
                break;
            case "dateD":
                Collections.sort(allFilesPaths, Collections.reverseOrder());
                break;


        }





        Log.d("test", "test");
        Log.d("list", allFilesPaths.toString());

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        //this makes a list with 3 columns
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        //optimizations
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ArrayList<Cell> Cells = prepareData();
        MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(), Cells, this);
        recyclerView.setAdapter(adapter);
    }

    //prepare the images for the list
    private ArrayList<Cell> prepareData() {
        ArrayList<Cell> allImages = new ArrayList<>();
        for (Cell c : allFilesPaths) {
            Cell cell = new Cell();
            cell.setTitle(c.getTitle());
            cell.setPath(c.getPath());
            cell.setDate(c.getDate());
            allImages.add(cell);

        }
        return allImages;
    }

    //loads all the files from the folder
    private List<Cell> listAllFiles(String pathName) {
        List<Cell> allFiles = new ArrayList<>();
        File file = new File(pathName);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {

                Date lastModDate = new Date(f.lastModified());


                Cell cell = new Cell();
                cell.setTitle(f.getName());
                cell.setPath(f.getAbsolutePath());
                cell.setDate(lastModDate);
                allFiles.add(cell);
            }
        }
        return allFiles;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //show the images
                showImages("");
            } else {
                Toast.makeText(getActivity(), "Permission not granted!", Toast.LENGTH_SHORT).show();

            }

        }
    }


    //In your fragment - call this method from onClickListener

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dateAscending:
                //sort date ascending stuff here

                Toast.makeText(getActivity(), "Sorted by date (ascending)!", Toast.LENGTH_SHORT).show();
                showImages("dateA");
                return true;
            case R.id.dateDescending:
                //sort date descending stuff here

                Toast.makeText(getActivity(), "Sorted by reverse date (descending)!", Toast.LENGTH_SHORT).show();
                showImages("dateD");
                return true;
            case R.id.nameAscending:
                //sort name ascending stuff here

                Toast.makeText(getActivity(), "Sorted by name (ascending)!", Toast.LENGTH_SHORT).show();
                showImages("nameA");
                return true;
            case R.id.nameDescending:
                //sort name descending stuff here

                Toast.makeText(getActivity(), "Sorted by name (descending)!", Toast.LENGTH_SHORT).show();
                showImages("nameD");
                return true;

        }
        return true;
    }

    @Override
    public void onGalleryClick(int position) {
        //to selected fragment
        Cell i = allFilesPaths.get(position);
        Toast.makeText(getActivity(),i.getPath(),Toast.LENGTH_SHORT).show();




        Uri uri = Uri.parse(i.getPath());


        //bundle URI and change fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("passedImage", uri);

        SelectedFragmentActivity SelectedFragmentActivity = new SelectedFragmentActivity();
        SelectedFragmentActivity.setArguments(bundle);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, SelectedFragmentActivity).addToBackStack(null).commit();














    }


//test2


}
