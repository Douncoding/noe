package com.douncoding.noe.service;

import com.douncoding.noe.model.EventStatus;
import com.orhanobut.logger.Logger;

import org.ccbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 안전, 주의, 경고, 끈김 4가지의 상태를 가지도록 구성
 * 안정적으로 비콘이 감지되는 것에 중점이 있다 따라서, 가중치를 준다.
 */
public class EnterBeaconSet {
    private static final int DEBOUNCE_COUNT = 10;
    private Map<Beacon, Integer> dataSet;

    public EnterBeaconSet() {
        this.dataSet = new HashMap<>();
    }

    public void debounce(List<Beacon> items) {
        // 감지된 비콘이 없는 경우도 처리하기 위함
        if (items.size() <= 0)
            items = new ArrayList<>();

        // 감지된 모든 항목 처리: 디바운스 값 초기화
        for (Beacon beacon : items) {
            if (!dataSet.containsKey(beacon)) {
                onCallback.onConnect(beacon);
            }
            dataSet.put(beacon, DEBOUNCE_COUNT);
        }

        // 감지되지 않은 항목처리: 디바운싱
        for (Beacon key : dataSet.keySet()) {
            int count = dataSet.get(key);

            Logger.v("디바운싱 확인 비콘:%s 디바운스:%d",key.getId1().toString(), count);
            // 만약, 보유하고 있는 비콘이 감지되지 않는 경우 안정화(디바운스) 처리 수행
            if (!items.contains(key)) {
                if (count < 0) {
                    dataSet.remove(key);
                    onCallback.onDisconnect(key);
                } else {
                    dataSet.put(key, --count);
                }
            }
        }


    }

    boolean contain(String uuid) {
        boolean isContain = false;
        for (Beacon key : dataSet.keySet()) {
            if (key.getId1().toString().equals(uuid))
                isContain = true;
        }
        return isContain;
    }

    private EventStatus convertRssiToStatus(int rssi) {
        if (rssi > -80) {
            return EventStatus.NORMAL;
        } else if (rssi > -81 && rssi < -90) {
            return EventStatus.NOTICE;
        } else {
            return EventStatus.WARNNING;
        }
    }


    public List<Beacon> getEnterBeaconList() {
        return new ArrayList<Beacon>(dataSet.keySet());
    }

    private OnCallback onCallback;
    interface OnCallback {
        void onConnect(Beacon beacon);
        void onDisconnect(Beacon beacon);
    }

    void setOnCallback(OnCallback callback) {
        this.onCallback = callback;
    }
}
