package com.douncoding.noe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.EventStatus;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.model.TrackType;
import com.douncoding.noe.model.data.TrackingTarget;
import com.douncoding.noe.repository.TTargetRepository;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.ccbeacon.beacon.Beacon;
import org.ccbeacon.beacon.BeaconConsumer;
import org.ccbeacon.beacon.BeaconManager;
import org.ccbeacon.beacon.BeaconParser;
import org.ccbeacon.beacon.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.douncoding.noe.Constants.ACTION_BEACON_SERVICE_START;
import static com.douncoding.noe.Constants.ACTION_BEACON_SERVICE_STOP;

public class BeaconService extends Service implements BeaconConsumer {
    /**
     * 바인드를 사용하거나 브로드캐스트를 이용하여 접근해야 한정적이지만, 귀차니즘으로 인해 전역으로 접근한다.
     * 따라서, 본 서비스가 정상적으로 컨텍스트 상에서 운용중인 경우 필히 instance 가 가리키도록 유지해야 한다. 이는
     * 액티비티가 강제종료될 가능을 가진다.
     */
    public static BeaconService instance = null;

    private BeaconManager beaconManager;
    private EventGenerator eventGenerator; // "안정화" 된 진입비콘 목록
    private Map<String, Object> trackerMap; // 추적 중인 목록

    /**
     *
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupBeacon();
        setupBroadcastReceiver();

        eventGenerator = new EventGenerator();
        eventGenerator.setOnCallback(onEventGeneratorCallback);

        trackerMap = new HashMap<>();
        TTargetRepository.getInstance().getList(new TTargetRepository.OnCallback() {
            @Override
            public void onResult(String key, String json, String type) {
                if (type.equals(TrackType.PET.name())) {
                    Pet pet = new Gson().fromJson(json, Pet.class);
                    trackerMap.put(key, pet);
                } else if (type.equals(TrackType.BABY.name())) {
                    Baby baby = new Gson().fromJson(json, Baby.class);
                    trackerMap.put(key, baby);
                }
            }
        });

        instance = this;
        Logger.i("======================== 비콘 서비스 시작 ========================");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("======================== 비콘 서비스 종료 ========================");
    }

    /**
     * 비콘 설정
     * CC-Beacon Layout (m:2-3=0215) 설정
     * 제조사 마다 고유의 레이아웃이 있으며, BeaconManager 에 설정된 비콘만 감지한다. 따라서 다른 제조사의 비콘을
     * 같이 사용하는 경우 필히 추가해야 한다.
     */
    private void setupBeacon() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        beaconManager.setForegroundBetweenScanPeriod(250);
        beaconManager.setForegroundScanPeriod(1000);
    }

    /**
     * 감시 시작과 종료의 제어는 브로드캐스트를 통해 액티비티로 부터 제어된다.
     */
    private void setupBroadcastReceiver() {
        LocalBroadcastReceiver receiver = new LocalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BEACON_SERVICE_START);
        intentFilter.addAction(ACTION_BEACON_SERVICE_STOP);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(((collection, region) -> {
            eventGenerator.debounce((List<Beacon>)collection);
        }));

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 비콘의 상태변화 감지
     */
    private EventGenerator.OnCallback onEventGeneratorCallback = new EventGenerator.OnCallback() {
        @Override
        public void onChangedEventStatus(Beacon beacon, EventStatus eventStatus) {
            Logger.i("상태변화 이벤트수신:" + eventStatus.name());
            // 이벤트 감지 - 데이터베이스 저장 - 어떤 형태로?
            // 여기서 기록할 수 있는데이터를 신경써야되냐? 아니면, 출력하는 관점에서 필요한 데이터를 신경 써야 하냐

            // 비콘에 해당하는 객체가 무엇인지를 구하는 로직필요
            // 여기서 파이어페이스 Key 를 사용할 필요가 있는가? 당현히 없다. 왜? 접근할 가치가 없으니간

            // 이탈 - 상태변경 시 현재위치의 값을 추출하는 부분 필요
            // 그러면, TrackEvent 객체를 생성할 수 있다.
            // 해당 객체를 데이터베이스에 기록하고 리스너를 등록하면 원하는 처리과정의 끝이다.
        }
    };

    class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_BEACON_SERVICE_START)) {

            } else if (intent.getAction().equals(ACTION_BEACON_SERVICE_STOP)) {

            }
        }
    }

    /**
     * @return 현재 진입상태인 비콘목록
     */
    public List<Beacon> getCurrentBeaconList() {
        return eventGenerator.getEnterBeaconList();
    }

    /**
     * 감시할 타켓을 추가
     *
     * @param key 비콘을 식별하기 위한 식별자 (UUID)
     */
    public void addTarget(String key, TrackType type, Pet pet) {
        Logger.i("추적 서비스: 새로운 대상 추가: 식별:%s 타입:%s 대상:%s", key, type.name(), new Gson().toJson(pet));
    }

    /**
     *
     * @param key 비콘을 식별하기 위한 식별자 (UUID)
     */
    public void removeTarget(String key) {
        Logger.i("추적 서비스: 대상 제거: 식별:%s", key);

//        TrackingTarget trackingTarget = TTargetRepository.getInstance().get(key);
//        if (trackingTarget.getType().equals(TrackType.PET.name())) {
//            Pet pet = new Gson().fromJson(trackingTarget.getJson(), Pet.class);
//            trackerMap.remove(pet.getBeacon().getUuid());
//        } else if (trackingTarget.getType().equals(TrackType.BABY.name())) {
//            Baby baby = new Gson().fromJson(trackingTarget.getJson(), Baby.class);
////            trackerMap.remove(pet.getBeacon().getUuid());
//        }

    }
}
