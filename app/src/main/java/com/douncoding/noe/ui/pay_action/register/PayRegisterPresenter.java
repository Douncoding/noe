package com.douncoding.noe.ui.pay_action.register;

import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.service.BeaconService;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (pay.getBeacon().isEmpty()) {
            mView.showRequestInputField(2);
            return ;
        }

        // 중복확인
        FirebaseUtil.getBeaconPayRef().child(pay.getBeacon()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 이미 등록된 비콘 목록 - 이렇게 하면 결국 타입을 전부 찾아야 한다는 단점이 존재한다.
                // 따라서, 처음 부터 타입을 나누어서 저장하는 것이 이득이 크다.
                if (dataSnapshot.exists()) {
                    mView.showRegisterFailure();
                } else {
                    submitToFirebase(pay);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void submitToFirebase(final Pay pay) {
        mView.showProgressDialog();

        DatabaseReference paysRef = FirebaseUtil.getPaysRef();
        DatabaseReference beaconRef = FirebaseUtil.getBeaconPayRef();
        DatabaseReference peopleRef = FirebaseUtil.getCurrentUserHasPayRef();

        Map<String, Object> updateUserData = new HashMap<>();
        final String payKey = paysRef.push().getKey();

        updateUserData.put(FirebaseUtil.getPath(paysRef) + "/" + payKey, pay);
        updateUserData.put(FirebaseUtil.getPath(beaconRef) + "/" + pay.getBeacon(), payKey);
        updateUserData.put(FirebaseUtil.getPath(peopleRef) + "/" + payKey, true);

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
