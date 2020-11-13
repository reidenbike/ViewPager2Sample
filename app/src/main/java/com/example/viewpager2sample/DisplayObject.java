package com.example.viewpager2sample;

import java.util.ArrayList;

public class DisplayObject {
    private ArrayList<Integer> dataFieldOptions;
    private int numberDisplays;

    DisplayObject(ArrayList<Integer> dataFieldOptions, int numberDisplays){
        this.dataFieldOptions = dataFieldOptions;
        this.numberDisplays = numberDisplays;
    }

    Integer getMetric(int position) {
        return dataFieldOptions.get(position);
    }
    public void setMetric(int position, int metric) {
        dataFieldOptions.set(position, metric);
    }

    Integer getNumberDisplays() {
        return numberDisplays;
    }
    void setNumberDisplays(int numberDisplays) {
        this.numberDisplays = numberDisplays;
    }
}
