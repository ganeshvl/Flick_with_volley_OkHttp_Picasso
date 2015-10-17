package mitul.flickster.handler;

import android.content.Context;
import android.database.Cursor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import mitul.flickster.db.MovieDataSource;
import mitul.flickster.db.MovieHelper;
import mitul.flickster.model.Flick;

/**
 * Created by mitul on 29/09/15.
 */
public class DatabaseHandler extends RequestHandler {
    private MovieDataSource mMovieDataSource = new MovieDataSource();
    private HashMap<String, Flick> movieDatabase = new HashMap<String, Flick>();
    private ArrayList<Flick> movie_list = new ArrayList<Flick>();


    public DatabaseHandler(Context context) {
        this.mMovieDataSource.setContext(context);
    }

    @Override
    public int handleRequest(String request) {

        try {
            mMovieDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = mMovieDataSource.selectAllMovies();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Flick flick = new Flick();
            //int i = cursor.getColumnIndex(MovieHelper.COLUMN_TITLE);
            //mTitles.add(cursor.getString(i));
            flick.setTitle(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_TITLE)));
            System.out.println(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_TITLE)));
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
            movieDatabase.put(cursor.getString(cursor.getColumnIndex(MovieHelper.COLUMN_TITLE)), flick);
            movie_list.add(flick);
            cursor.moveToNext();
        }
        mMovieDataSource.close();/*
        boolean found = false;
        Flick movie = new Flick();
        for(int i =0; i< movie_list.size();i++){
            Flick f = movie_list.get(i);
            if(f.getTitle().equalsIgnoreCase(request)){
                movie_list.remove(i);
                movie = f;
                found = true;
                break;
            }
        }
        if (found){
            movie_list.add(movie);
            Collections.reverse(movie_list);
            return 1;
        }
        */
        if (movieDatabase.containsKey(request)) {
          System.out.println("Movie found");
            /*
            boolean found = false;
            Flick movie = new Flick();
            for(int i =0; i< movie_list.size();i++){
                Flick f = movie_list.get(i);
                if(f.getTitle().equalsIgnoreCase(request)){
                    movie_list.remove(i);
                    movie = f;
                    found = true;
                    break;
                }
                if(found){
                    movie_list.add(movie);
                    Collections.reverse(movie_list);
                }
            }
            */
            return 1;
        }
        else {
            System.out.println("Movie Not found");
            ApiCallHandler api = new ApiCallHandler();
            api.handleRequest(request);
            return 0;
        }

    }

    public Flick fetch_movie(String request) {
        return this.movieDatabase.get(request);
    }
    public  ArrayList<Flick> getMovie_list(String request){
            Flick movie = new Flick();
            for(int i =0; i< movie_list.size();i++){
                Flick f = movie_list.get(i);
                if(f.getTitle().equalsIgnoreCase(request)){
                    movie_list.remove(i);
                    movie = f;
                    movie_list.add(movie);
                    Collections.reverse(movie_list);
                    break;
                }

            }

        return movie_list;
    }
}
