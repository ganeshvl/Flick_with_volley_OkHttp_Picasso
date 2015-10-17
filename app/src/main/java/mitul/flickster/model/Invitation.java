package mitul.flickster.model;

/**
 * Created by mitul on 13/10/15.
 */
public class Invitation {

    private String party_title;
    private String movie_id;
    private String movie_name;
    private String party_people;
    private String party_contacts;
    private String date;
    private String time;
    private String venue;

    public Invitation(String party_title, String movie_id, String movie_name, String party_people, String party_contacts, String date, String time, String venue) {
        this.party_title = party_title;
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.party_people = party_people;
        this.party_contacts = party_contacts;
        this.date = date;
        this.time = time;
        this.venue = venue;
    }

    public String getParty_title() {
        return party_title;
    }

    public void setParty_title(String party_title) {
        this.party_title = party_title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParty_contacts() {
        return party_contacts;
    }

    public void setParty_contacts(String party_contacts) {
        this.party_contacts = party_contacts;
    }

    public String getParty_people() {
        return party_people;
    }

    public void setParty_people(String party_people) {
        this.party_people = party_people;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }



}
