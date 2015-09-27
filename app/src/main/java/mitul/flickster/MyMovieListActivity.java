package mitul.flickster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import mitul.flickster.adapter.MovieAdapter;
import mitul.flickster.db.MovieDataSource;
import mitul.flickster.db.MovieHelper;
import mitul.flickster.model.Flick;
import mitul.flickster.services.MySingleton;


public class MyMovieListActivity extends Activity {
    ListView myMovieListView;
    JSONObject json_data;
    Flick movie;
    public static final String TAG = MyMovieListActivity.class.getSimpleName();
    ArrayList<String> Movielist = new ArrayList<>();
    private MovieDataSource mDataSource;
    protected ArrayList<String> mTitles;
    private ArrayList<Flick> movieDatabase;
    public static final String MOVIE_OBJECT = "MOVIE_OBJECT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie_list);
        myMovieListView = (ListView) findViewById(R.id.myMovieListView);
        mDataSource = new MovieDataSource(MyMovieListActivity.this);
        movieDatabase = new ArrayList<Flick>();
        mTitles = new ArrayList<String>();
        String movie_name = getIntent().getStringExtra("my_movie");
        Log.v("tag", movie_name);
        use_json_object("http://www.omdbapi.com/?t=" + movie_name.trim().replace(" ", "+") + "&y=&plot=full&r=json");
        //check_network("http://www.omdbapi.com/?t=" + movie_name.trim().replace(" ", "+") + "&y=&plot=full&r=json");
       // Log.v("tag", movie.getGenre());
        //updateUI();
        setListenerOnListView();
    }
    private void setListenerOnListView() {
        myMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final Flick item = (Flick) parent.getItemAtPosition(position);
                Intent myIntent = new Intent(view.getContext(), MovieDetailActivity.class);
                //myIntent.putExtra(MOVIE_OBJECT,sample_movies.get(position));
                //myIntent.putExtra("my_movie", (String) parent.getItemAtPosition(position));
                Flick flick = (Flick)parent.getItemAtPosition(position);
                //myIntent.putExtra("Imdb",flick.getImdbId());
                myIntent.putExtra(MOVIE_OBJECT,flick);
                //Log.v(TAG, position + " ---------------------------------------");
                //Toast.makeText(getApplicationContext(),"-----------------",Toast.LENGTH_LONG);
                startActivity(myIntent);
            }
        });
    }

    private void updateUI() {
        Cursor cursor = mDataSource.selectAllMovies();
        updateList(cursor);
    }
    private void updateList(Cursor cursor) {
        mTitles.clear();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int i = cursor.getColumnIndex(MovieHelper.COLUMN_RATING);
            mTitles.add(cursor.getString(i));
            //Toast.makeText(MovieListActivity.this,
            // String.valueOf(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_RATING))),
            // Toast.LENGTH_LONG).show();
            cursor.moveToNext();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyMovieListActivity.this,
                android.R.layout.simple_list_item_1,
                mTitles);

        myMovieListView.setAdapter(adapter);
    }

    private void use_json_object(String movieUrl) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, movieUrl,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            Log.v("json",response.getString("Director"));
                            movie = getDetails(response);
                            loadMovieData();
                            Movielist.add(movie.getDirector());
                            Cursor cursor = mDataSource.selectAllMovies();
                            mTitles.clear();
                            cursor.moveToFirst();
                            while(!cursor.isAfterLast()){
                                Flick flick = new Flick();
                                //int i = cursor.getColumnIndex(MovieHelper.COLUMN_TITLE);
                                //mTitles.add(cursor.getString(i));
                                flick.setTitle(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_TITLE)));
                                flick.setRated(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_CONTENT)));
                                flick.setReleased(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_RELEASE)));
                                flick.setGenre(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_GENRE)));
                                flick.setDirector(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_DIRECTOR)));
                                flick.setActors(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_CAST)));
                                flick.setPlot(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_PLOT)));
                                flick.setPoster(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_IMAGE)));
                                flick.setImdbRating(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_RATING)));
                                flick.setImdbId(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_IMDB)));
                                flick.setShort_plot();
                                movieDatabase.add(flick);
                                cursor.moveToNext();
                            }
                            //MovieAdapter movie_adapter = new MovieAdapter(MyMovieListActivity.this,movieDatabase);
                            //myMovieListView.setAdapter(movie_adapter);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // updateDisplay();
                                    MovieAdapter movie_adapter = new MovieAdapter(MyMovieListActivity.this,movieDatabase);
                                    myMovieListView.setAdapter(movie_adapter);

                                }
                            });
                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyMovieListActivity.this,
                              //      android.R.layout.simple_list_item_1,
                                //    mTitles);
                            //myMovieListView.setAdapter(adapter);

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
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void check_network(String movieUrl) {


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
                            Log.v(TAG,response.body().string());
                            movie = getDetails(data);
                            Movielist.add(movie.getDirector());
                            //Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // updateDisplay();
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyMovieListActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            Movielist);
                                    myMovieListView.setAdapter(adapter);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_movie_list, menu);
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
        return flick;
    }
    private Flick getDetails(JSONObject omdb) throws JSONException {
        //JSONObject omdb = new JSONObject(data);
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
        return flick;
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
        //check_network(movieUrl);
        super.onResume();
        //get_movie_details(movie_list);
        try {
            mDataSource.open();
            Log.v(TAG, "---------- Database succesfully created -------------");
            //loadMovieData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void loadMovieData() {
        Log.v(TAG,"-------Trying----------");
        mDataSource.insertMovie(movie);
        Log.v(TAG, "-------Trying----------");
    }

    @Override
    protected void onPause(){
        super.onPause();
        //loadMovieData();
        //mDataSource.updateRating(ratingBar.getRating() + "", movie.getImdbId());
        mDataSource.close();
    }
}
