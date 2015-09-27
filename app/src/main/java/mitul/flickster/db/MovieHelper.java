package mitul.flickster.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mitul on 20/09/15.
 */
public class MovieHelper extends SQLiteOpenHelper {

    //private static final String DB_NAME = "movies.db";
    private static final String DB_NAME = "movies1.db";
    public static final String TABLE_MOVIES = "MOVIES";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_IMDB = "IMDB";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_IMAGE = "IMAGE";
    public static final String COLUMN_CONTENT = "CONTENT";
    public static final String COLUMN_PLOT = "PLOT";
    public static final String COLUMN_GENRE = "GENRE";
    public static final String COLUMN_RELEASE = "RELEASE";
    public static final String COLUMN_RATING = "RATING";
    public static final String COLUMN_CAST = "CAST";
    public static final String COLUMN_DIRECTOR = "DIRECTOR";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_MOVIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_PLOT + " TEXT, " +
                    COLUMN_GENRE + " TEXT, " +
                    COLUMN_RELEASE + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    COLUMN_CAST + " TEXT, " +
                    COLUMN_IMDB + " TEXT, " +
                    COLUMN_DIRECTOR + " TEXT)";

    public MovieHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
