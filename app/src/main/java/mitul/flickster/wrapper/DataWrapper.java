package mitul.flickster.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

import mitul.flickster.model.Flick;

/**
 * Created by mitul on 26/09/15.
 */
public class DataWrapper implements Serializable {

    private ArrayList<String> data;
    private ArrayList<Flick> flick;

    public DataWrapper(ArrayList<String> data) {
        this.data = data;
    }
    //public DataWrapper(ArrayList<Flick> flick) {
        //this.flick = flick;
    //}

    public ArrayList<String> getData() {
        return this.data;
    }



}