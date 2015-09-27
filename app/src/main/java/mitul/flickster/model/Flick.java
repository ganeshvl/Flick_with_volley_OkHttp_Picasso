package mitul.flickster.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mitul on 13/09/15.
 */
public class Flick implements Parcelable{

    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String language;
    private String awards;
    private String poster;
    private String metascore;
    private Float imdbRating;
    private String imdbVotes;
    private String type;
    private String response;
    private String country;
    private String imdbId;
    private String short_plot;

    public Flick() {

    }

    public void setShort_plot(){
        if (getPlot().length() > 50){
            this.short_plot = getPlot().substring(0,50)+"...";
        }
        else{
            this.short_plot = getPlot();
        }
    }
    public String getShort_plot(){
        return this.short_plot;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }


    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public Float getImdbRating() {return imdbRating;}

    public void setImdbRating(String imdbRating) {this.imdbRating = Float.parseFloat(imdbRating);}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imdbId);
        dest.writeString(rated);
        dest.writeString(plot);
        dest.writeString(short_plot);
        dest.writeString(genre);
        dest.writeString(director);
        dest.writeString(actors);
        dest.writeString(poster);
        dest.writeFloat(imdbRating);
        dest.writeString(released);

    }
    public Flick(Parcel in){
        title = in.readString();
        imdbId = in.readString();
        rated = in.readString();
        plot = in.readString();
        short_plot = in.readString();
        genre = in.readString();
        director= in.readString();
        actors = in.readString();
        poster = in.readString();
        imdbRating = in.readFloat();
        released = in.readString();
    }

    public static final Creator<Flick> CREATOR = new Creator<Flick>() {
        @Override
        public Flick createFromParcel(Parcel source) {
            return new Flick(source);
        }

        @Override
        public Flick[] newArray(int size) {
            return new Flick[size];
        }
    };
}
