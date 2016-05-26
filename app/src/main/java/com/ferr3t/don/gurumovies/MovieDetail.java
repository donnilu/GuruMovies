package com.ferr3t.don.gurumovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Movie movie = intent.getParcelableExtra("movie");

        TextView textView = (TextView) findViewById(R.id.text_view_title);
        textView.setText(movie.title);

        TextView textView2 = (TextView) findViewById(R.id.text_view_description);
        textView2.setText(movie.description);

        TextView textView3 = (TextView) findViewById(R.id.text_view_rating);
        textView3.setText(movie.voteAverage);

        getSupportActionBar().setTitle(movie.title);

        ImageView imageViewBackground = (ImageView) findViewById(R.id.image_view_background);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/" + movie.backDrop).into(imageViewBackground);

        ImageView imageViewPoster = (ImageView) findViewById(R.id.image_view_poster);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/" + movie.posterPath)
                .resize(400, 0)
                .into(imageViewPoster);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

}
