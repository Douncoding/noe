package com.douncoding.noe.model;

/**
 * Created by douncoding on 2016. 12. 7..
 */

public class Baby {
    private Beacon beacon;

    private String name;
    private String birthday;
    private String nickname;
    private int gender;
    private String pictureUrl;

    public Baby() {
    }

    public Baby(String name, String birthday, String nickname, int gender, String pictureUrl, Beacon beacon) {
        this.name = name;
        this.birthday = birthday;
        this.nickname = nickname;
        this.gender = gender;
        this.pictureUrl = pictureUrl;
        this.beacon = beacon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String picture_url) {
        this.pictureUrl = picture_url;
    }
}
