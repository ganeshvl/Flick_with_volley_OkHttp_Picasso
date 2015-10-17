package mitul.flickster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import mitul.flickster.model.MovieParty;

/**
 * Created by mitul on 12/10/15.
 */
public class PartyDataSource {
    private SQLiteDatabase mDatabase;
    private PartyHelper mPartyHelper;
    private Context mContext;

    public PartyDataSource(Context context){
        this.mContext = context;
        mPartyHelper = new PartyHelper(mContext);
    }
    public void open() throws SQLException {
        mDatabase = mPartyHelper.getWritableDatabase();
    }

    public void close() {
        mDatabase.close();
    }

    public void insertParty(MovieParty party){
        mDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(PartyHelper.COLUMN_IMDB,party.getMovieID());
            values.put(PartyHelper.COLUMN_TITLE,party.getMovieName());
            values.put(PartyHelper.COLUMN_TIME,party.getTime());
            values.put(PartyHelper.COLUMN_DATE,party.getDate());
            values.put(PartyHelper.COLUMN_VENUE,party.getLocation());
            values.put(PartyHelper.COLUMN_NAME,party.getNamesList());
            values.put(PartyHelper.COLUMN_ADDRESS,party.getEmailList());
            values.put(PartyHelper.COLUMN_PARTYTITLE,party.getParty_title());

            mDatabase.insert(PartyHelper.TABLE_PARTIES, null, values);
            mDatabase.setTransactionSuccessful();
        }
        finally {
            mDatabase.endTransaction();
        }
    }
    public Cursor selectAllParties(){
        Cursor cursor = mDatabase.query(
                PartyHelper.TABLE_PARTIES,
                null, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        ) ;
        return cursor;
    }
    public void deleteAll() {
        mDatabase.delete(
                PartyHelper.TABLE_PARTIES, // table
                null, // where clause
                null  // where params
        );
    }

}
