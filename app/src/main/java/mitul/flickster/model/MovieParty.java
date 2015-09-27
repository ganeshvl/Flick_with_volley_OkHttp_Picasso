package mitul.flickster.model;

import java.util.ArrayList;

/**
 * Created by mitul on 28/09/15.
 */
public class MovieParty {
    private float current_rating;
    private String date;
    private String time;
    private Flick flick;
    private ArrayList<String> names = new ArrayList<String>();


    public MovieParty(Flick flick) {
        this.flick = flick;
    }

    public void setCurrent_rating(){
        this.current_rating = flick.getImdbRating();
    }
    public float getCurrent_rating(){
        return this.flick.getImdbRating();
    }
    public void setDate(String date){
        this.date= date;
    }
    public void setTime(String time){
        this.time= time;
    }

    public String getMovieName(){
        return this.flick.getTitle();
    }
    public ArrayList<String> getContacts(){
        return names;
    }
    public void setContactName(String s){
        names.add(s);
    }
    public void  delete(){names.remove(names.size() - 1);}
}

