package com.test.movieplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.movieplus.model.Movies;
import com.test.movieplus.utils.Utils;

public class MovieDetails extends AppCompatActivity {

    TextView txtTitle;
    TextView txtYear;
    TextView txtCnt;
    TextView txtOverview;
    ImageView imgPoster;
    ImageView imgBackdrop;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        txtTitle = findViewById(R.id.txtTitle);
        txtYear = findViewById(R.id.txtYear);
        txtOverview = findViewById(R.id.txtOverview);
        imgPoster = findViewById(R.id.imgPoster);
        imgBackdrop = findViewById(R.id.imgBackdrop);
        cardView = findViewById(R.id.cardView);
        String title;
        String year;
        String overview;


        Movies movies = (Movies) getIntent().getSerializableExtra("Movies");
        title = movies.getTitle();
        year = movies.getYear();
        overview = movies.getOverview();

        GlideUrl glideUrl = new GlideUrl( Utils.BASE_IMG_URL + movies.getPhoto_url(),new LazyHeaders.Builder().addHeader("User-Agent","Your-User-Agent").build());
        Glide.with(MovieDetails.this).load(glideUrl).into(imgPoster);
        Log.i("iii", Utils.BASE_IMG_URL + movies.getBackdrop_url());
        GlideUrl glideUrl1 = new GlideUrl( Utils.BASE_IMG_URL + movies.getBackdrop_url(),new LazyHeaders.Builder().addHeader("User-Agent","Your-User-Agent").build());
        Glide.with(MovieDetails.this).load(glideUrl1).into(imgBackdrop);

        txtTitle.setText(title);
        txtYear.setText(year);
        txtOverview.setText(overview);




    }
}