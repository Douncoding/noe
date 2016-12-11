package com.douncoding.noe.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class TrackingTarget extends RealmObject {
    @PrimaryKey
    private String key;
    private String json;
    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
