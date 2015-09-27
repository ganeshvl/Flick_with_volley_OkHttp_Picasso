package mitul.flickster;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import mitul.flickster.db.MovieDataSource;
import mitul.flickster.db.MovieHelper;


public class MovieListActivity extends Activity {
    protected MovieDataSource mMovieDataSource;
    protected ArrayList<String> mTitles;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        mTitles = new ArrayList<String>();
        mListView = (ListView)findViewById(R.id.list1);
        mMovieDataSource = new MovieDataSource(MovieListActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mMovieDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = mMovieDataSource.selectAllMovies();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MovieListActivity.this,
                android.R.layout.simple_list_item_1,
                mTitles);

        mListView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMovieDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
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
