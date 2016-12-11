package com.douncoding.noe.model;

/**
 * Created by douncoding on 2016. 12. 7..
 */

public enum  TrackType {
    PET("PET"), BABY("BABY");

    private String type;

    TrackType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
