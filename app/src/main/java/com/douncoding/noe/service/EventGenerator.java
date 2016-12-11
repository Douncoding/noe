package com.douncoding.noe.service;

import com.douncoding.noe.model.EventStatus;
import com.orhanobut.logger.Logger;

import org.ccbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by douncoding on 2016. 12. 8..
 */

public class EventGenerator {
    private static final int DEBOUNCE_COUNT = 10;
    private Map<Beacon, DistanceLevel> dataSet;

    public EventGenerator() {
        this.dataSet = new HashMap<>();
    }

    public void debounce(List<Beacon> beacons) {
        if (beacons.size() <= 0)
            beacons = Collections.emptyList();

        /**
         * 감지된 항목 처리: 안전, 주의, 경고 상태를 감지하기 위한목적을 가진다.
         */
        for (Beacon beacon : beacons) {
            // 기존에 가지고 있던 상태
            if (dataSet.containsKey(beacon))
            {
                DistanceLevel level = dataSet.get(beacon);
                EventStatus currentStatus = convertRssiToStatus(beacon.getRssi());

                Logger.v("상태 확인:%s => %s", level.currentEventStatus.name(), currentStatus.name());

                if (level.currentEventStatus != currentStatus && !currentStatus.equals(EventStatus.UNKNOWN)) {
                    Logger.v("상태가 변경되기 이전에 새로운 상태가 갱신됨: %s => %s", level.currentEventStatus.name(), currentStatus.name());
                    level.currentEventStatus = currentStatus;
                    level.stayCount = 0;
                } else {
                    // 이전상태와 현재상태가 다른 경우에만 이벤트를 발생시키기 위함
                    if (level.prevEventStatus != level.currentEventStatus) {
                        if (level.stayCount > DEBOUNCE_COUNT) {
                            Logger.v("상태변경 이벤트 발생:%s => %s", level.prevEventStatus.name(), level.currentEventStatus.name());

                            if (onCallback != null) {
                                onCallback.onChangedEventStatus(beacon, level.currentEventStatus);
                            }

                            level.prevEventStatus = level.currentEventStatus;
                            level.stayCount = DEBOUNCE_COUNT;
                        } else {
                            level.stayCount++;
                            Logger.v("상태 카운드: %s = %d", level.currentEventStatus, level.stayCount);
                        }
                    } else {
                        // 안전화를 위해 반복되는 상태는 지속적인 디바운스 임계치를 가지도록 설정한다.
                        // 해당부분이 존재하지 않는다면 '이탈' 상태감지가 누적 개수로 동작하게 된다.
                        level.stayCount = DEBOUNCE_COUNT;
                    }
                }
            }
            // 새로운 연결 - 새로운 연결 상태는 즉시 반영된다.
            else
            {
                Logger.v("새로운 연결");
                DistanceLevel level = new DistanceLevel();
                level.prevEventStatus = EventStatus.UNKNOWN;
                // 모든 새로운연결은 항상 이벤트를 발생시키게 된다
                level.currentEventStatus = EventStatus.UNKNOWN;
                // 연결되자마자 빠지는 경우을 대비하기 위해 초기값을 임계치로 설정
                level.stayCount = DEBOUNCE_COUNT;
                dataSet.put(beacon, level);
            }
        }


        /**
         * 감지되지 않은 항목 처리: {@link EventStatus#BREAKAWAY} 상태
         */
        for (Beacon key : dataSet.keySet()) {
            // 감지된 비콘목록 중에 기존에 저장된 비콘이 없는 경우 (신호를 감지 못하는 경우)
            if (!beacons.contains(key)) {
                DistanceLevel level = dataSet.get(key);
                if (level.stayCount < 0) {
                    Logger.v("이탈상태 변경 이벤트 발생:%s => %s", level.prevEventStatus.name(), EventStatus.BREAKAWAY.name());

                    if (onCallback != null) {
                        onCallback.onChangedEventStatus(key, EventStatus.BREAKAWAY);
                    }

                    dataSet.remove(key);
                } else {
                    Logger.v("이탈상태 기록: " + level.stayCount);
                    level.stayCount--;
                }
            }
        }
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
        void onChangedEventStatus(Beacon beacon, EventStatus eventStatus);
    }

    void setOnCallback(OnCallback callback) {
        this.onCallback = callback;
    }

    private class DistanceLevel {
        EventStatus prevEventStatus;
        EventStatus currentEventStatus;
        int stayCount;
    }

}
