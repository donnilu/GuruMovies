package com.ferr3t.don.gurumovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    View rootView;
    GridView gridView;
    List<Movie> movies = new ArrayList<>();


    public MainActivityFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
       // GetMoviesTask getMoviesTask = new GetMoviesTask();
       // getMoviesTask.execute();

    }

    public void sortMovies(String sort) {

        GetMoviesTask getMoviesTask = new GetMoviesTask();
        getMoviesTask.execute(sort);

    }

    public class GetMoviesTask extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String sortBy = params[0];
            //Contains the Json Result
            String moviesJson = null;

            try {

                final String API_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String API_KEY = "b96087f4a03b686eaf542c5df8037005";
                final String API_KEY_PARAM = "api_key";
                final String API_PAGE = "page";
                final String API_SORT_BY = "sort_by";

                Uri apiUri = Uri.parse(API_BASE_URL).buildUpon()
                        .appendQueryParameter(API_SORT_BY, sortBy)
                        .appendQueryParameter(API_PAGE, "1")
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(apiUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();


                if (inputStream == null) {
                    moviesJson = null;
                }else {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    moviesJson = null;
                }

                ////////////////////////////////////////////////////
                //////////Return the json string containing the movies////////////
                moviesJson = buffer.toString();

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                Log.d("Error: ", e.toString());
            }

            try {
                return getMoviesFromJson(moviesJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] jsonMovieData) {

            if (jsonMovieData != null) {
                try {
                    gridView.setAdapter(new ImageAdapter(getContext(), jsonMovieData));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {

                            //pass the movie to the Detail Activity
                            Intent i = new Intent(getActivity(), MovieDetail.class);
                            i.putExtra("movie", movies.get(position));
                            startActivity(i);

                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else

            {
                Toast.makeText(getContext(), "Nothing to show :(",
                        Toast.LENGTH_LONG).show();
            }

        }

            }


        private String[] getMoviesFromJson(String jsonMovieData) throws JSONException {

            //Json Object(s) to be extracted
            final String TMDB_POSTER = "poster_path";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_COUNT = "vote_count";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_BACKDROP = "backdrop_path";
            final String TMDB_BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w500/";
            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";

            JSONObject moviesJson = new JSONObject(jsonMovieData);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

            String[] posterPaths = new String[moviesArray.length()];

            if (movies != null) {
                movies.clear();
            }
            // Extract movie data and build movie objects
            for(int i = 0; i < moviesArray.length(); i++) {
                Movie movie = new Movie();
                JSONObject movieJson = moviesArray.getJSONObject(i);
                movie.posterPath = TMDB_BASE_POSTER_PATH + movieJson.optString(TMDB_POSTER).toString();
                posterPaths[i] = movie.posterPath;
                movie.title = movieJson.optString(TMDB_ORIGINAL_TITLE).toString();
                movie.description = movieJson.optString(TMDB_OVERVIEW).toString();
                movie.title = movieJson.optString(TMDB_ORIGINAL_TITLE).toString();
                movie.backDrop = movieJson.optString(TMDB_BACKDROP).toString();
                movie.voteAverage = movieJson.optString(TMDB_VOTE_AVERAGE).toString();
                movie.numOfVotes = movieJson.optString(TMDB_VOTE_COUNT).toString();
                movies.add(movie);

            }

            return posterPaths;
        }



}
