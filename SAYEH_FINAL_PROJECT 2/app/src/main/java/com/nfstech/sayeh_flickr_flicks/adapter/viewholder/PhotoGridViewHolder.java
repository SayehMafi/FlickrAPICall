package com.nfstech.sayeh_flickr_flicks.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.nfstech.sayeh_flickr_flicks.adapter.PhotoGridAdapter;
import com.nfstech.sayeh_flickr_flicks.dao.Photo;

import static com.nfstech.sayeh_flickr_flicks.adapter.PhotoGridAdapter.mListener;

public class PhotoGridViewHolder extends RecyclerView.ViewHolder {

    public PhotoGridViewHolder(@NonNull View itemView) {
        super(itemView);
        FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.setFlexGrow(1f);

        // onitem click listener
        itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener !=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);

                    }
                }

            }
        } );
    }
}
