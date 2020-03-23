package com.nfstech.sayeh_flickr_flicks.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.nfstech.sayeh_flickr_flicks.R;
import com.nfstech.sayeh_flickr_flicks.adapter.PhotoGridAdapter;
import com.nfstech.sayeh_flickr_flicks.adapter.viewholder.PhotoGridViewHolder;
import com.nfstech.sayeh_flickr_flicks.common.FlexLayoutScrollListener;
import com.nfstech.sayeh_flickr_flicks.common.enums.ErrorEnum;
import com.nfstech.sayeh_flickr_flicks.view.model.PhotoListModel;
import com.nfstech.sayeh_flickr_flicks.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
/*
* Created by sayeh last updated 7:00-6-1-2020  version 53
 */

public class MainActivity extends AppCompatActivity implements PhotoGridAdapter.onItemClickListener{

    private static final String LOGGER_TAG = MainActivity.class.getSimpleName();
    private MainViewModel mMainViewModel;
    private PhotoGridAdapter mPhotoGridAdapter;
    private int mInitialPhotoRange = 0;
    private boolean mIgnoreFetching = false;
    private ProgressBar mProgressBar = null;
    private EditText mEtSearchString;

    private Runnable mResetFetchingFlag = new Runnable() {
        @Override
        public void run() {
            if (!isFinishing()) {
                mIgnoreFetching = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mProgressBar = findViewById(R.id.progress);
        Button searchButton = findViewById(R.id.btn_search);
        mEtSearchString = findViewById(R.id.et_search);
        final RecyclerView recyclerView = findViewById(R.id.rv_photo_grid);

        mEtSearchString.clearFocus();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                mMainViewModel.onSearchTapped(mEtSearchString.getText().toString());
            }
        });

        //Observer to listen to different Errors
        mMainViewModel.getError().observe(this, new Observer<ErrorEnum>() {
            @Override
            public void onChanged(@Nullable ErrorEnum errorEnum) {
                if (errorEnum != null) {
                    switch (errorEnum) {
                        case UNABLE_TO_FETCH_DATA:
                            Toast.makeText(MainActivity.this, errorEnum.getErrorResource(), Toast.LENGTH_SHORT).show();
                            break;
                        case INVALID_DATA:
                            mEtSearchString.setError(getString(errorEnum.getErrorResource()));
                            break;
                    }
                }
            }
        });

        //Flexbox setup to Photos grid
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerView.setLayoutManager(layoutManager);
        List<PhotoListModel> photos = new ArrayList<>();
        mPhotoGridAdapter = new PhotoGridAdapter(photos);
        recyclerView.setAdapter(mPhotoGridAdapter);

        mPhotoGridAdapter.setOnItemClickListener( MainActivity.this );


        //Observer for showing/hiding progress bar while fetching data from web
        mMainViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean showLoading) {
                mProgressBar.setVisibility(showLoading == Boolean.TRUE ? View.VISIBLE : View.INVISIBLE);
            }
        });

        //Observer for photos fetched via web or DB
        mMainViewModel.getPhotos().observe(this, new Observer<List<PhotoListModel>>() {
            @Override
            public void onChanged(@Nullable List<PhotoListModel> photoListModels) {
                if (photoListModels != null) {
                    if (photoListModels.isEmpty()) {
                        /*
                            Empty data set indicates clearing of exiting data set of the adapter i.e show a blank list
                            which will happen when a search button is tapped as we have to clear existing data before showing new data
                        */
                        mPhotoGridAdapter.setDataSource(new ArrayList<PhotoListModel>());
                        mPhotoGridAdapter.notifyDataSetChanged();
                        mInitialPhotoRange = 0;
                    } else {
                        /*
                            We receive a full data set of all the photos that have been fetched from the web or DB. We could have returned only
                            the most recently fetched data but we have to handle the case where the phone rotates and we have show all the existing
                            photos. This is the reason that the MainViewModel is keeping all the photos fetched.
                         */
                        List<PhotoListModel> subList = photoListModels.subList(mInitialPhotoRange, mInitialPhotoRange + (photoListModels.size() - mInitialPhotoRange));
                        mPhotoGridAdapter.addPhotos(subList);
                        mPhotoGridAdapter.notifyItemRangeChanged(mInitialPhotoRange, subList.size());
                        Log.d(LOGGER_TAG, "SubList Size:" + subList.size() + " start:" + mInitialPhotoRange + " total:" + mPhotoGridAdapter.getItemCount());
                        mInitialPhotoRange = mInitialPhotoRange + (photoListModels.size() - mInitialPhotoRange);
                    }
                }
            }
        });
        recyclerView.addOnScrollListener(new FlexLayoutScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                if (!mIgnoreFetching) {
                    mIgnoreFetching = true;
                    new Handler(Looper.getMainLooper()).postDelayed(mResetFetchingFlag, 500);
                    Log.d(LOGGER_TAG, "Load more items called");
                    mMainViewModel.fetchPhotos();
                }
            }


            @Override
            public boolean isLastPage() {
                return mMainViewModel.isLastPageFetched();
            }

            @Override
            public boolean isLoading() {
                return mMainViewModel.isLoading();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    clearFocus();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void clearFocus() {
        mEtSearchString.clearFocus();
        Window currentWindow = getWindow();
        if (currentWindow != null) {
            //Hide the keyboard on launch or scroll
            View currentFocus = currentWindow.getDecorView();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                            //
    //                                             Menu                                           //
    //                                                                                            //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create a submenu: intentSubmenu ---------------------------------------------------------
        SubMenu intentSubmenu = menu.addSubMenu("intent options");
        intentSubmenu.add("Open Flickr.com").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                Intent intent = new Intent(Intent.ACTION_VIEW);

                // Define our ACTION_VIEW
                intent.setData( Uri.parse("http://www.Flickr.com"));

                startActivity(intent);

                return false;
            }
        });
        intentSubmenu.add("CameraActivity").setOnMenuItemClickListener
                ( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent( MainActivity.this, Camera.class );
                MainActivity.this.startActivity( intent );
                return false;
            }
        } );
        //------------------------------------------------------------------------------------------

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onItemClick(int positon) {
        Intent i = new Intent( this,Display_Photo.class );

    }
}
