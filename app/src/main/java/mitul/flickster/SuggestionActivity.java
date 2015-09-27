package mitul.flickster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mitul.flickster.services.MySingleton;


public class SuggestionActivity extends Activity {
    private ListView mListView;
    public static final String TAG = SuggestionActivity.class.getSimpleName();
    ArrayList<String> Movielist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        mListView = (ListView) findViewById(R.id.listView);
        String search_term = getIntent().getStringExtra(IndexActivity.SEARCH_TERM);
        String movieUrl = "http://www.omdbapi.com/?s=" + search_term.trim().replace(" ","+")+ getString(R.string.omdb_format);
        use_json_object(movieUrl);
        setListenerOnListView();
    }

    private void setListenerOnListView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final Flick item = (Flick) parent.getItemAtPosition(position);
                Intent myIntent = new Intent(view.getContext(), MyMovieListActivity.class);
                //myIntent.putExtra(MOVIE_OBJECT,flick_list[position]);
                //myIntent.putExtra(MOVIE_OBJECT,sample_movies.get(position));
                myIntent.putExtra("my_movie",(String)parent.getItemAtPosition(position));
                //String title =(String)parent.getItemAtPosition(position);
                //Log.v(TAG,title+" ---------------------------------------");
                startActivity(myIntent);
            }
        });
    }

    private void use_json_object(String movieUrl) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, movieUrl,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jArray = response.getJSONArray("Search");
                            //final ArrayList<String> Movielist = new ArrayList<>();
                            Movielist.clear();
                            //System.out.println("*****JARRAY*****" + jArray.length());
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json_data = jArray.getJSONObject(i);
                                //Log.v(TAG, "Title: " + json_data.getString("Title"));
                                Movielist.add(json_data.getString("Title"));
                                Log.v(TAG, "Title: " + json_data.getString("Title"));

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SuggestionActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    Movielist);
                                    mListView.setAdapter(adapter);
                                }
                            });
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



}
