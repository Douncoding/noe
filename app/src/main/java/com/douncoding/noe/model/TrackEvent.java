package com.douncoding.noe.model;

/**
 * Created by douncoding on 2016. 12. 6..
 */

public class TrackEvent {

    private Type type;

    private String title;
    private String description;
    private String facePictureUrl;
    private long timestamp;

    private long latitude;
    private long longitude;

    public TrackEvent() {
    }

    public TrackEvent(Type type, String title, String description, String facePictureUrl, long latitude, long longitude, long timestamp) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.facePictureUrl = facePictureUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacePictureUrl() {
        return facePictureUrl;
    }

    public void setFacePictureUrl(String facePictureUrl) {
        this.facePictureUrl = facePictureUrl;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public enum Type {
        NORMAL, NOTICE, WARNNING, BREAKAWAY;
    }
}
