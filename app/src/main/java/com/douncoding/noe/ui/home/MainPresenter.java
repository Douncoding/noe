package com.douncoding.noe.ui.home;

import android.content.Intent;

import com.douncoding.noe.service.BeaconService;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

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
    public void sendToFCM() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            FirebaseUtil.getBaseRef().child("fcm").child(FirebaseUtil.getCurrentUserId()).setValue(token);
        } else {
            Logger.e("FCM 토큰 등록 실패:");
        }
    }

    @Override
    public void startBeaconService() {
        getContext().startService(new Intent(getContext(), BeaconService.class));
    }

    @Override
    public void onLogout() {
        // 로그아웃 처리 임시동작
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }
}
