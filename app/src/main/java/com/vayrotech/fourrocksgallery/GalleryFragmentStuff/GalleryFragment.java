package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;
import com.vayrotech.fourrocksgallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


// TODO: THIS IS A TEST FOR LOAD MORE FRAGMENT

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment  {

    View view;
    List<Cell> allFilesPaths;
    final String defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    String folderPath = defaultPath;
    ModelDatabase DB;

    RecyclerView recyclerView;
    LoadMoreAdapter LoadMoreAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
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
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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

            recyclerView = getView().findViewById(R.id.recyclerViewGallery);
            populateData();
            initAdapter();
            initScrollListener();



        }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
    }


    // this shows the images on the screen
    private void populateData() {
        int i = 0;
        while (i < 10) {
            rowsArrayList.add("Item " + i);
            i++;
        }
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
        for(Cell c : allFilesPaths) {
            String penis = c.getTitle();
            Log.d("Title", penis);
            if(penis.startsWith("."))
            {
                Log.e("SHOULD DELETE THIS CELL ", penis);
            }



        }
        allFilesPaths.removeIf(c -> c.getTitle().startsWith("."));

        //we'll update the database with the new arraylist
      /* TODO: DATABASE STUFF
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

*/

/* TODO: SORT STUFF

        switch (sort) {
            case "dateA":
                Collections.sort(allFilesPaths);
                break;
            case "dateD":
                Collections.sort(allFilesPaths, Collections.reverseOrder());
                break;


        }
*/
        Log.d("test", "test");
        Log.d("list", allFilesPaths.toString());
        int itemCount= allFilesPaths.size();
        Log.d("list", "allfilespaths size is " + itemCount);


        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewGallery);
        recyclerView.setHasFixedSize(true);

        //this makes a list with 3 columns
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        //optimizations
        recyclerView.setItemViewCacheSize(40);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ArrayList<Cell> Cells = prepareData();


        LoadMoreAdapter adapter = new LoadMoreAdapter(rowsArrayList, getActivity().getApplicationContext(), Cells);
        recyclerView.setAdapter(adapter);



        //MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(), Cells, this);
        //recyclerView.setAdapter(adapter);
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
                populateData();
            } else {
                Toast.makeText(getActivity(), "Permission not granted!", Toast.LENGTH_SHORT).show();

            }

        }
    }






    private void initAdapter() {

        //moved elsewhere


/*
        LoadMoreAdapter = new LoadMoreAdapter(rowsArrayList, getActivity().getApplicationContext(), Cells);
        recyclerView.setAdapter(LoadMoreAdapter);*/






    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        rowsArrayList.add(null);
        LoadMoreAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                LoadMoreAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                LoadMoreAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1000);


    }





























}



