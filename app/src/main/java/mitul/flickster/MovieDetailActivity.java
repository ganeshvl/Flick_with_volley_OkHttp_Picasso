package mitul.flickster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitul.flickster.db.MovieDataSource;
import mitul.flickster.model.Flick;


public class MovieDetailActivity extends Activity {
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static ArrayList<Flick> movie_list = new ArrayList<Flick>();
    private Flick movie;
    ProgressDialog pDialog;
    @InjectView(R.id.MovieTitle) TextView title;
    @InjectView(R.id.LongPlot) TextView plot;
    @InjectView(R.id.ContentType) TextView ContentType;
    @InjectView(R.id.genre) TextView Genre;
    @InjectView(R.id.release_date) TextView release_date;
    @InjectView(R.id.Cast) TextView Cast;
    @InjectView(R.id.Director) TextView director;
    @InjectView(R.id.ratingBar) RatingBar ratingBar;
    @InjectView(R.id.PartyButton) Button PartyButton;
    @InjectView (R.id.imageView) ImageView poster;
    private StringBuilder sb;
    private MovieDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.inject(this);
        mDataSource = new MovieDataSource(MovieDetailActivity.this);
        String movieUrl = getString(R.string.omdb_url) + getIntent().getStringExtra("MOVIE_TITLE").trim().replace(" ","+")+ getString(R.string.omdb_format);
        //String movieUrl = "http://www.omdbapi.com/?t=we are&y=&plot=full&r=json";
        title.setText(getIntent().getStringExtra("MOVIE_TITLE"));
        check_network(movieUrl);
        //use_volley(movieUrl);
        //use_json_object(movieUrl);
        PartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadMovieData();
                startActivity(new Intent(MovieDetailActivity.this, MovieListActivity.class));
            }
        });
    }

    private void use_json_object(String movieUrl) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, movieUrl,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(MovieDetailActivity.this,response.getString("Title")+response.getString("Year"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                System.out.println("Something went wrong with Volley !");
                error.printStackTrace();

            }
        });
        // Add the request to the queue
        Volley.newRequestQueue(this).add(jsObjRequest);



    }

    private void use_volley(String movieUrl) {
       Toast.makeText(MovieDetailActivity.this, "Using Volley", Toast.LENGTH_LONG).show();

        if(isNetworkAvailable()) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            // Request a string response
            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, movieUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e(TAG,response);
                                movie = getDetails(response);
                                Toast.makeText(MovieDetailActivity.this,response.substring(0,20),Toast.LENGTH_LONG).show();
                                updateDisplay();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Error handling
                    System.out.println("Something went wrong with Volley !");
                    error.printStackTrace();

                }
            });
            // Add the request to the queue
            Volley.newRequestQueue(this).add(stringRequest);
        }
        else{
            Log.v(TAG, "Sorry network is down");
        }
    }


    private void check_network(String movieUrl) {
        Toast.makeText(MovieDetailActivity.this,"Using OkHttp",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        if(isNetworkAvailable()){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(movieUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    System.out.println("Something went wrong with OKHttp");
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String data = response.body().string();
                            movie = getDetails(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        }

                        else {
                            Log.v(TAG, "bad response");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught ", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Exception caught ", e);
                    }
                }
            });
            Log.v(TAG, "Yup network is available");
        }
        else{
            Log.v(TAG, "Sorry network is down");
        }


    }

    private void updateDisplay() {
        Picasso.with(MovieDetailActivity.this).load(movie.getPoster()).into(poster);
        title.setText(movie.getTitle());

        if (movie.getPlot().length() >= 375) {
            plot.setText("Plot: " + movie.getPlot().substring(0,375)+"...");
        }
        else {
            plot.setText("Plot: " + movie.getPlot());
        }
        Cast.setText("Cast: " + movie.getActors());
        director.setText("Director: " + movie.getDirector());
        release_date.setText("Released: " + movie.getReleased());
        Genre.setText("Genre: " + movie.getGenre());
        ratingBar.setRating(movie.getImdbRating() / 2);
        ContentType.setText(movie.getRated());
        pDialog.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            mDataSource.open();
            Log.v(TAG,"---------- Database succesfully created -------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Flick getDetails(String data) throws JSONException {
        JSONObject omdb = new JSONObject(data);
        Flick flick = new Flick();
        flick.setTitle(omdb.getString("Title"));
        flick.setImdbRating(omdb.getString("imdbRating"));
        flick.setReleased(omdb.getString("Released"));
        flick.setRated(omdb.getString("Rated"));
        flick.setGenre(omdb.getString("Genre"));
        flick.setYear(omdb.getString("Year"));
        flick.setPlot(omdb.getString("Plot"));
        flick.setPoster(omdb.getString("Poster"));
        flick.setResponse(omdb.getString("Response"));
        flick.setImdbId(omdb.getString("imdbID"));
        flick.setType(omdb.getString("Type"));
        flick.setLanguage(omdb.getString("Language"));
        flick.setCountry(omdb.getString("Country"));
        flick.setAwards(omdb.getString("Awards"));
        flick.setActors(omdb.getString("Actors"));
        flick.setDirector(omdb.getString("Director"));
        flick.setWriter(omdb.getString("Writer"));
        flick.setMetascore(omdb.getString("Metascore"));
        movie_list.add(flick);
        return flick;
    }

    private void loadMovieData() {
        Log.v(TAG,"-------Trying----------");
        mDataSource.insertMovie(movie);
        Log.v(TAG, "-------Trying----------");
    }

    @Override
    protected void onPause(){
        super.onPause();
        mDataSource.close();
    }
}
