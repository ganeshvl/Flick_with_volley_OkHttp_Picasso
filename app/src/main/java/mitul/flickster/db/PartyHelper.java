package mitul.flickster.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mitul on 12/10/15.
 */
public class PartyHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "parties1.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_PARTIES = "PARTIES1";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_IMDB = "IMDB";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_VENUE = "LOCATION";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ADDRESS = "ADDRESS";
    public static final String COLUMN_PARTYTITLE = "PARTYTITLE";

    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_PARTIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMDB + " TEXT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_VENUE + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PARTYTITLE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT)";


    public PartyHelper(Context context) {
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
