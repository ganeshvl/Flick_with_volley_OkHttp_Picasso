package mitul.flickster.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mitul on 28/09/15.
 */
public class MovieParty {
    private float current_rating;
    private String date;
    private String party_title;
    private String time;
    private String location;
    private Flick flick;
    private ArrayList<String> names_email = new ArrayList<String>();
    private ArrayList<String> names = new ArrayList<String>();
    private String movie_name;


    public MovieParty(Flick flick) {
        this.flick = flick;
    }

    public MovieParty() {

    }
    public void setMovieName(String movie_name){this.movie_name = movie_name;}
    public String getMovie_name(){return this.movie_name;}
    public String getLocation(){return this.location;}
    public void setLocation(String location){ this.location = location;}
    public void setCurrent_rating(){
        this.current_rating = flick.getImdbRating();
    }
    public float getCurrent_rating(){
        return this.flick.getImdbRating();
    }
    public String getParty_title(){return party_title;}
    public void setParty_title(String title) {this.party_title = title;}
    public void setDate(String date){
        this.date= date;
    }
    public void setTime(String time){
        this.time= time;
    }
    public String getTime(){return this.time;}
    public String getDate(){return this.date;}


    public String getMovieName(){
        return this.flick.getTitle();
    }
    public String getMovieID(){ return this.flick.getImdbId();}
    public ArrayList<String> getContactsEmail(){
        return names_email;
    }
    public void setContactEmail(String s){
        names_email.add(s);
    }
    public void setNames(String s){ names.add(s);}
    public String getNamesList(){
        String nameList = null;
        Iterator<String> itr = names.iterator();
        while(itr.hasNext()){
            if(nameList == null){
                nameList = itr.next();
            }
            else {
                nameList = nameList+","+itr.next();
            }

        }

        return nameList;
    }
    public String getEmailList(){
        String emailList = null;
        Iterator<String> itr = names_email.iterator();
        while(itr.hasNext()){
            if(emailList == null){
                emailList = itr.next();
            }
            else {
                emailList = emailList+","+itr.next();
            }

        }
        return emailList;
    }

    public ArrayList<String> getNames(){ return names;}
    public void  delete(){names_email.remove(names_email.size() - 1);}
}

