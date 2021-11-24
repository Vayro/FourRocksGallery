package com.vayrotech.fourrocksgallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;


import com.vayrotech.fourrocksgallery.DatabaseStuff.ModelDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class SelectedFragmentActivity extends Fragment {
    private int mIS = 224;
    private String MP1 = "model_unquant.tflite";//my model from teachable machine
    private String MP ="mobilenet_v1_1.0_224.tflite";//The pretrain model with 1000 classes from tensorflow

    private String LP1 = "labels.txt";
    private String LP= "labels_mobilenet_v1_224.txt";// the pretrain model labels from tensorflow
    Classifier classifier;
    TextView TextView;
    Button classify;
    View view;
    ImageView selectedImage;
    Uri passedImageFrag;
    ModelDatabase DB;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SelectedFragmentActivity() {
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
    public static SelectedFragmentActivity newInstance(String param1, String param2) {
        SelectedFragmentActivity fragment = new SelectedFragmentActivity();
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

        passedImageFrag = this.getArguments().getParcelable("passedImage");

        //this takes whatever URI that was passed to this fragment and updates the selectedImage variable in the frameActivity so that if you leave this fragment and come back, the selectedImage will remain as the last image selected
        FrameActivity penIsland = (FrameActivity) getActivity();
        penIsland.selectedImage = passedImageFrag;


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_selected_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //created stuff goes here
        selectedImage = getView().findViewById(R.id.selectedView);
        TextView=getView().findViewById(R.id.textView);
        DB = new ModelDatabase(this.getContext()); //create the database object
        if (passedImageFrag != null) {
            selectedImage.setImageURI(passedImageFrag);
        } else
            selectedImage.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));

        try { initClassifier();

        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{     Bitmap bitmap = ((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap();


                    List<Classifier.Recognition> result = classifier.recognizeImage(bitmap);

                    //Toast.makeText(this, result.get(0).toString(),Toast.LENGTH_SHORT).show();
                    //for database update
                    String textViewUpdate = result.get(0).toString();
                    TextView.setText(textViewUpdate);
                    databaseUpdate(textViewUpdate);
                }
                catch(Exception e){
                    //TextView.setText("Unknown");

                    try {
                        classifier = new Classifier(getActivity().getAssets(),MP1, LP1, mIS);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

        });



    }

    private void initClassifier() throws IOException {
        classifier = new Classifier(getActivity().getAssets(),MP, LP, mIS);


    }



    private void databaseUpdate(String dbClass){


        File f = new File(passedImageFrag.getPath());
        Date lastModified =new Date(f.lastModified());

        String dbPath = passedImageFrag.getPath();
        String dbTitle =  f.getName();
        String dbDate = lastModified.toString();




        DB.updateData(dbPath, dbTitle, dbDate, dbClass);
        /*
        if (checkinsertdata == true) {
            Toast.makeText(this.getContext(), "New Entry Inserted in database", Toast.LENGTH_SHORT).show();
            Log.d("datebase", "databaseUpdate: Path " + dbPath + "| Title " + dbTitle + "| Date " + dbDate + "| Classification " + dbClass);

        } else {
             Toast.makeText(this.getContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
        }*/
    }




}










