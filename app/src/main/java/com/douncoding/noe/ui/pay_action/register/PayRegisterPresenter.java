package com.douncoding.noe.ui.pay_action.register;

import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.service.BeaconService;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by douncoding on 2016. 12. 9..
 */

public class PayRegisterPresenter extends BasePresenter<PayRegisterContract.View> implements PayRegisterContract.Presenter {
    @Override
    public void onResumeFromContext() {

    }

    @Override
    public void onPauseFromContext() {

    }

    @Override
    public void onDestroyFromContext() {

    }

    @Override
    public void onSubmit(Pay pay) {
        // 기본정보확인
        if (pay.getBankname().isEmpty() || pay.getAccount().isEmpty() || pay.getPassword().isEmpty()) {
            mView.showRequestInputField(1);
            return ;
        }

        // 비콘정보 확인
        Beacon beacon = pay.getBeacon();
        if (beacon.getUuid().isEmpty() || beacon.getMajor().isEmpty() || beacon.getMinor().isEmpty()) {
            mView.showRequestInputField(3);
            return ;
        }

        mView.showProgressDialog();

        DatabaseReference paysRef = FirebaseUtil.getPaysRef();
        DatabaseReference beaconRef = FirebaseUtil.getBeaconRef();
        DatabaseReference peopleRef = FirebaseUtil.getCurrentUserHasPayRef();

        Map<String, Object> updateUserData = new HashMap<>();
        final String babyKey = paysRef.push().getKey();
        updateUserData.put(FirebaseUtil.getPath(paysRef) + "/" + babyKey, pay);
        updateUserData.put(FirebaseUtil.getPath(beaconRef) + "/" + beaconRef.push().getKey(), beacon);
        updateUserData.put(FirebaseUtil.getPath(peopleRef) + "/" + babyKey, true);

        // 데이터베이스 기록
        FirebaseUtil.getBaseRef().updateChildren(updateUserData, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                FirebaseCrash.report(databaseError.toException());
                mView.showRegisterFailure();
            } else {
                mView.showRegisterSuccess();
            }
            mView.hideProgressDialog();
        });
    }

    @Override
    public void searchAroundBeacon() {
        List<org.ccbeacon.beacon.Beacon> beacons = BeaconService.instance.getCurrentBeaconList();
        if (beacons.size() <= 0) {
            Logger.i("비콘 감지 실패: 수동입력 필요");
            mView.showSearchBeaconFailure();
        } else if (beacons.size() == 1){
            Logger.i("비콘 한개 감지: 자동입력");
            org.ccbeacon.beacon.Beacon item = beacons.get(0);
            Beacon result = new Beacon(item.getId1().toString(),
                    item.getId2().toString(), item.getId3().toString());
            mView.renderBeaconData(result);
        } else {
            Logger.i("비콘 %s개 감지: 선택필요", beacons.size());
            List<Beacon> result = new ArrayList<>();
            for (org.ccbeacon.beacon.Beacon item : beacons) {
                result.add(new Beacon(item.getId1().toString(), item.getId2().toString(),
                        item.getId3().toString()));
            }

            mView.renderChooserBeaconDialog(result);
        }
    }
}
