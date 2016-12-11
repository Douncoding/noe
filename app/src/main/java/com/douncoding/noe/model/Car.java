package com.douncoding.noe.model;

public class Car {
    private Beacon beacon;
    private String uid;

    private String name;
    private String licenseplate;
    private String birthday;
    private String pictureUrl;

    public Car() {
    }

    /**
     *
     * @param beacon 비콘
     * @param name 차종 또는 닉네임
     * @param licenseplate 차량번호
     * @param birthday 구매일자
     * @param pictureUrl 사진경로
     */
    public Car(Beacon beacon, String name, String licenseplate, String birthday, String pictureUrl) {
        this.beacon = beacon;
        this.name = name;
        this.licenseplate = licenseplate;
        this.birthday = birthday;
        this.pictureUrl = pictureUrl;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
