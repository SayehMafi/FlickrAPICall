package com.nfstech.sayeh_flickr_flicks.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.nfstech.sayeh_flickr_flicks.R;
import com.nfstech.sayeh_flickr_flicks.adapter.viewholder.PhotoGridViewHolder;
import com.nfstech.sayeh_flickr_flicks.view.model.PhotoListModel;

import java.util.List;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridViewHolder> {

    private List<PhotoListModel> mPhotos;

    // onclick listener
    public static onItemClickListener mListener;

    public static interface onItemClickListener {
        void onItemClick(int positon);
    }
    public static void setOnItemClickListener(onItemClickListener listener){
        mListener=listener;
    }
//end onclick

    public PhotoGridAdapter(List<PhotoListModel> photos) {
        mPhotos = photos;
    }

    public void setDataSource(List<PhotoListModel> photos) {
        if (mPhotos != null) {
            mPhotos.clear();
        }
        mPhotos = photos;
    }

    public void addPhotos(List<PhotoListModel> photos) {
        if (mPhotos != null) {
            mPhotos.addAll( photos );
        }
    }

    @NonNull
    @Override
    public PhotoGridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PhotoGridViewHolder(
                LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.layout_photo_item, viewGroup, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGridViewHolder photoGridViewHolder, int i) {
        ImageView imageView = (ImageView) photoGridViewHolder.itemView;
        FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) imageView.getLayoutParams();
        //This is just a basic logic to show differently sized images using the Flexbox layout
        if (i % 2 == 0) {
            layoutParams.setWidth( 250 );
        } else {
            layoutParams.setWidth( 400 );
        }
        Glide.with( photoGridViewHolder.itemView.getContext() )
                .load( mPhotos.get( i ).getUrl() )
                .diskCacheStrategy( DiskCacheStrategy.DATA )
                .into( imageView );
    }

    @Override
    public int getItemCount() {
        return mPhotos != null ? mPhotos.size() : 0;
    }
}
