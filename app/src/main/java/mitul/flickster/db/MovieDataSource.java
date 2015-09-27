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

    public void insertMovie(Flick flick){
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
            values.put(MovieHelper.COLUMN_IMDB,flick.getImdbId());
            mDatabase.insert(MovieHelper.TABLE_MOVIES, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }


    /*
     * UPDATE
     */
    public int updateRating(String rating,String id) {
        String whereClause = MovieHelper.COLUMN_IMDB + " = ? ";
        ContentValues values = new ContentValues();
        values.put(MovieHelper.COLUMN_RATING, rating);
        return mDatabase.update(MovieHelper.TABLE_MOVIES, values, whereClause ,new String[]{ id });
    }



    public Cursor selectAllMovies(){
        Cursor cursor = mDatabase.query(
                MovieHelper.TABLE_MOVIES,
                null, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        ) ;
        return cursor;
    }

}
