package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;


import android.content.Context;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vayrotech.fourrocksgallery.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<String> mItemList;
    private ArrayList<Cell> galleryList;

    private Context context;

    public LoadMoreAdapter(List<String> itemList, Context context, ArrayList<Cell> galleryList) {
        mItemList = itemList;
        this.galleryList = galleryList;
        this.context = context;




        Log.d("Adapterlist", mItemList.toString());
        int itemCount= mItemList.size();
        Log.d("Adapterlist", "mItemList size is " + itemCount);

        Log.d("Adapterlist", galleryList.toString());
        int itemCount2= galleryList.size();
        Log.d("Adapterlist", "galleryList size is " + itemCount2);



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadmore_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, i);



        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, i);
        }

    }


    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        private ImageView img;
        private TextView imgDate;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            tvItem = view.findViewById(R.id.tvItem);
            img = (ImageView) view.findViewById(R.id.tvImage);
            imgDate = (TextView) view.findViewById(R.id.tvDate);









        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Cell cell = galleryList.get(position);
        String item = mItemList.get(position);
        //viewHolder.tvItem.setText(item);


        //setImageFromPath

        setImageFromPath(cell.getPath(), viewHolder.img, viewHolder.imgDate);


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


}
