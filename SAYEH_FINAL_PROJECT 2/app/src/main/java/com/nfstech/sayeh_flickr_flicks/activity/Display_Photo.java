package com.nfstech.sayeh_flickr_flicks.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfstech.sayeh_flickr_flicks.R;
import com.nfstech.sayeh_flickr_flicks.common.DaoToUi;
import com.nfstech.sayeh_flickr_flicks.dao.Photo;
import com.nfstech.sayeh_flickr_flicks.view.model.PhotoListModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Display_Photo extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ImageView ivPhoto;
    TextView tvInfo;
    Photo myPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display__photo );

        initialize();

        getMyIntent();

        displayPhoto();
    }
    private void displayPhoto() {

        tvInfo.setText(myPhoto.getClass().getName());
        String photoUrl = myPhoto.getPhotoUrl();


        // load image for cell ................................................


        try {

            URL url = new URL(photoUrl);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            //         textViewDisplay.setText(data);
            ivPhoto.setImageBitmap(bmp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMyIntent() {

        myPhoto = (Photo) getIntent().getSerializableExtra("currentObject");
    }

    private void initialize() {

        ivPhoto = findViewById(R.id.ivPhoto);
        tvInfo = findViewById(R.id.tvInfo);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Photo p1;

        //TODO

DaoToUi photoList;






        Intent intent = new Intent(this, Display_Photo.class);
      //  intent.putExtra("currentObject", p1);


        startActivityForResult(intent,1);


    }
}
