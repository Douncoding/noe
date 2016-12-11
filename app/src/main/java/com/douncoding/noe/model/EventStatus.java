package com.douncoding.noe.model;

public enum  EventStatus {
    UNKNOWN(0), NORMAL(1), NOTICE(2), WARNNING(3), BREAKAWAY(4);

    private int value;

    EventStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
