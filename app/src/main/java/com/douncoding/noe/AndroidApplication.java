package com.douncoding.noe;

import android.app.Application;
import android.content.Intent;

import com.douncoding.noe.repository.TTargetRepository;
import com.douncoding.noe.service.BeaconService;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initRealmConfiguration();

        // 앱내 로그 처리 초기화
        Logger.init(getString(R.string.app_name));
    }


    private void initRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
