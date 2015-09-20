package mitul.flickster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import mitul.flickster.model.Flick;

/**
 * Created by mitul on 20/09/15.
 */
public class MovieDataSource {

    private SQLiteDatabase mDatabase;
    private MovieHelper mMovieHelper;
    private Context mContext;

    public MovieDataSource(Context context){
        mContext = context;
        mMovieHelper = new MovieHelper(mContext);
    }

    //open
    public void open() throws SQLException {
        mDatabase = mMovieHelper.getWritableDatabase();
    }

    public void close() {
        mDatabase.close();
    }

    public void inserForecast(Flick flick){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MovieHelper.COLUMN_IMAGE,flick.getPoster());
            values.put(MovieHelper.COLUMN_TITLE,flick.getTitle());
            values.put(MovieHelper.COLUMN_CONTENT,flick.getRated());
            values.put(MovieHelper.COLUMN_PLOT,flick.getPlot());
            values.put(MovieHelper.COLUMN_GENRE,flick.getGenre());
            values.put(MovieHelper.COLUMN_RELEASE,flick.getReleased());
            values.put(MovieHelper.COLUMN_RATING,flick.getImdbRating());
            values.put(MovieHelper.COLUMN_CAST,flick.getActors());
            values.put(MovieHelper.COLUMN_DIRECTOR,flick.getDirector());
            mDatabase.insert(MovieHelper.TABLE_MOVIES, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }

    public Cursor selectAllTemperatures(){
        Cursor cursor = mDatabase.query(
                MovieHelper.TABLE_MOVIES, // table
                new String[] { MovieHelper.COLUMN_IMAGE,MovieHelper.COLUMN_TITLE,MovieHelper.COLUMN_CONTENT,
                        MovieHelper.COLUMN_PLOT,MovieHelper.COLUMN_GENRE,MovieHelper.COLUMN_RELEASE,MovieHelper.COLUMN_RATING
                ,MovieHelper.COLUMN_CAST,MovieHelper.COLUMN_DIRECTOR}, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        );
        return cursor;
    }

}