package com.watermark.main.DPWA;

import java.io.Serializable;
import java.util.*;
import java.util.Set;

public class Dataset  implements Serializable {
    private HashMap<Integer,ArrayList<String>> datatable;

    public HashMap<Integer, ArrayList<String>> getDatatable() {
        return datatable;
    }

    public void setDatatable(HashMap<Integer, ArrayList<String>> datatable) {
        this.datatable = datatable;
    }

    public Dataset(){
        this.datatable =new HashMap<Integer, ArrayList<String>>();

    }
    public Dataset(Dataset dataset) {
        this.datatable = new HashMap<>(dataset.getDatatable());
    }
    public Dataset(HashMap<Integer,ArrayList<String>> datatable){this.datatable = datatable;}

    public void insert(Integer key,ArrayList<String> data){
        datatable.put(key,data);
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "datatable=" + datatable +
                '}';
    }

    public int getSize() {
        return datatable.size();
    }
}
