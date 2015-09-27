package mitul.flickster;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mitul.flickster.model.Flick;


public class IndexActivity extends Activity {
    private static final  String MOVIE_TITLE = "MOVIE_TITLE" ;
    @InjectView(R.id.movie_name) EditText movie_title;
    @InjectView(R.id.searchButton) Button searchButton;
    public static final String TAG = IndexActivity.class.getSimpleName();
    public ArrayList<String> movie_list;
    public static final String SEARCH_TERM = "SEARCH_TERM";
    ProgressDialog pDialog ;
    TextView myText;
    private Flick myObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        movie_list = new ArrayList<String>();
        myText = (TextView)findViewById(R.id.myText);
        ButterKnife.inject(this);
        setListenerOnSearchButton();
        //String con = getIntent().getStringExtra("Imdb");
        myObject = (Flick) getIntent().getParcelableExtra(MyMovieListActivity.MOVIE_OBJECT);
        //
        if(myObject!=null)
        {myText.setText((CharSequence) myObject.getTitle());}

    }


    private void setListenerOnSearchButton() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_term = movie_title.getText().toString();
                //String movieUrl = "http://www.omdbapi.com/?s=" + search_term.trim().replace(" ","+")+ getString(R.string.omdb_format);
                Intent myIntent;
                myIntent = new Intent(v.getContext(),SuggestionActivity.class);
                myIntent.putExtra(SEARCH_TERM,search_term);
                startActivity(myIntent);


                //myIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                //myIntent.putExtra(MOVIE_TITLE,search_term);
                //startActivity(myIntent);
            }
        };
        searchButton.setOnClickListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
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



}
