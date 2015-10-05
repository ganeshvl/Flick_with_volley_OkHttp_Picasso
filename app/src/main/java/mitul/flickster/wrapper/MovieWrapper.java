package mitul.flickster.wrapper;

/**
 * Created by mitul on 03/10/15.
 */

import java.io.Serializable;
import java.util.ArrayList;

import mitul.flickster.model.Flick;

/**
 * Created by mitul on 26/09/15.
 */
public class MovieWrapper implements Serializable {


    private ArrayList<Flick> flick;

    public MovieWrapper(ArrayList<Flick> data) {
        this.flick = data;
    }


    public ArrayList<Flick> getData() {
        return this.flick;
    }

}
