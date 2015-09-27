package mitul.flickster.wrapper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mitul on 26/09/15.
 */
public class DataWrapper implements Serializable {

    private ArrayList<String> data;

    public DataWrapper(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return this.data;
    }

}