package com.douncoding.noe.model;

public class Pay {
    private String uid;
    private String beacon;

    private String bankname;
    private String account;
    private String password;

    public Pay() {
    }

    public Pay(String uid, String beacon, String bankname, String account, String password) {
        this.uid = uid;
        this.bankname = bankname;
        this.account = account;
        this.password = password;
        this.beacon = beacon;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }
}
