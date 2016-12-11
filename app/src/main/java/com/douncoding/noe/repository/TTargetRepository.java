package com.douncoding.noe.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.TrackType;
import com.douncoding.noe.model.data.TrackingTarget;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Full-Name: Tracking Target Repository
 */

public class TTargetRepository {
    public static TTargetRepository instance = null;

    /**
     * Application Context 에서 한번 공급해주자
     */
    public static TTargetRepository getInstance() {
        if (instance == null)
            instance = new TTargetRepository();
        return instance;
    }

    private TTargetRepository() {
    }

    /**
     * 타켓이 되는 개체는 반려동물, 차량, 자녀 세가지가 될수 있다. 하지만, 본 레포지터리에서 기억하는 것은 비콘의 식별정보
     * 뿐이기 때문에 프론트앤드 서비스와 통신하기 위해서는 타켓을 식별가능한 키값이 필요하다. 따라서, 키값은 파이어베이스에서
     * 생성한 키값을 사용한다.
     *
     * @param key Generated Key from firebase.
     */
    public void add(String key, TrackType type, Object data) {
        Logger.i("타켓 정보 저장: ");

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        TrackingTarget trackingTarget = realm.createObject(TrackingTarget.class);
        trackingTarget.setKey(key);
        trackingTarget.setType(type.name());
        trackingTarget.setJson(new Gson().toJson(data));
        realm.commitTransaction();
    }

    public void remove(String key) {
        Logger.i("타켓 정보 삭제:");
        Realm.getDefaultInstance().executeTransaction(realm ->
                realm.where(TrackingTarget.class).equalTo("key", key).findAll().deleteAllFromRealm());
    }

    public interface OnCallback {
        void onResult(String key, String json, String type);
    }

    /**
     * Data 타입의 클래스는 외부에 감추기 위함.. 결국에는 Data Layer 가 공존하게 됬다... 경력의 부족함이 이럴때
     * 나오는것 같다
     */
    public void getList(OnCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TrackingTarget> results = realm.where(TrackingTarget.class).findAll();

        Logger.i("타켓 전체조회: 개수:" + results.size());

        for (TrackingTarget item : results) {
            callback.onResult(item.getKey(), item.getJson(), item.getType());
        }
    }

    public TrackingTarget get(String key) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(TrackingTarget.class)
                .equalTo("key", key).findAll().first();
    }
}
