package com.douncoding.noe.model;

public class Pay {
    private Beacon beacon;
    private String uid;

    private String bankname;
    private String account;
    private String password;

    public Pay() {
    }

    public Pay(Beacon beacon, String bankname, String account, String password) {
        this.beacon = beacon;
        this.bankname = bankname;
        this.account = account;
        this.password = password;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
