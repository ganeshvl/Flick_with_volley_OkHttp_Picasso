package mitul.flickster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitul.flickster.db.MovieDataSource;
import mitul.flickster.model.Flick;
import mitul.flickster.services.MySingleton;


public class MovieDetailActivity extends Activity {
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
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
    @InjectView (R.id.imageView)
    NetworkImageView poster;
    private MovieDataSource mDataSource;
    private float current_rating ;
    private Flick myObject;
    public static final String MOVIE_PARTY= "MOVIE_PARTY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.inject(this);
        mDataSource = new MovieDataSource(MovieDetailActivity.this);
        myObject = (Flick) getIntent().getParcelableExtra(MyMovieListActivity.MOVIE_OBJECT);
        if(myObject!=null)
        {title.setText((CharSequence) myObject.getTitle());}
        updateDisplay(myObject);
        ListenerOnPartyButton();
        setListenerOnRatingBar();
    }

    private void ListenerOnPartyButton() {
        PartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailActivity.this, PartyActivity.class);
                i.putExtra(MOVIE_PARTY,myObject);
                startActivity(i);

            }
        });
    }
    private void setListenerOnRatingBar() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,ratingBar.getRating()+"");
                //mDataSource.updateRating(current_rating + "", myObject.getImdbId());
            }
        };
        ratingBar.setOnClickListener(listener);
    }

/*
    private void use_json_object(String movieUrl) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, movieUrl,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jArray = response.getJSONArray("Search");
                            //System.out.println("*****JARRAY*****" + jArray.length());
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json_data = jArray.getJSONObject(i);
                                //Log.v(TAG, "Title: " + json_data.getString("Title"));
                                movie_list.add(json_data.getString("Title"));
                                Log.v(TAG, "Title: " + movie_list.get(i));
                            }
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
        //Volley.newRequestQueue(this).add(jsObjRequest);
        //Volley.newRequestQueue(this).stop();
        //get_movie_details(movie_list);
    }

    private void get_movie_details(ArrayList<String> movie_list) {
        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        Log.v(TAG, "**************************************************************");
        Iterator<String> itr = movie_list.iterator();
        JsonObjectRequest detailed_req;
        while(itr.hasNext()){
            String url = "http://www.omdbapi.com/?t="+itr.next().trim().replace(" ","+")+"&y=&plot=full&r=json";
            detailed_req = new JsonObjectRequest(com.android.volley.Request.Method.GET, url,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.v(TAG, "TITLE:" + response.getString("Title"));
                                Toast.makeText(MovieDetailActivity.this,response.toString(),Toast.LENGTH_LONG);
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

            mRequestQueue.add(detailed_req);
            //System.out.println(itr.next());
        }
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
                                Toast.makeText(MovieDetailActivity.this,response,Toast.LENGTH_LONG).show();
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
*/
    private void updateDisplay(Flick movie) {
        ImageLoader imL = MySingleton.getInstance(this).getImageLoader();
        poster.setImageUrl(movie.getPoster(), imL);
        //Picasso.with(MovieDetailActivity.this).load(movie.getPoster()).into(poster);
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
        ratingBar.setRating(movie.getImdbRating());
        ContentType.setText(movie.getRated());
        title.setText(movie.getTitle());

    }

    @Override
    protected void onResume(){
        //check_network(movieUrl);
        super.onResume();
        //get_movie_details(movie_list);
        //ratingBar.setRating(myObject.getImdbRating());
        try {
            mDataSource.open();
            Log.v(TAG, "---------- Database succesfully created -------------");
            //loadMovieData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause(){
        super.onPause();
        mDataSource.close();
    }
}
