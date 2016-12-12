package com.douncoding.noe.fcm;

import android.util.Log;

import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Refreshed token: " + refreshedToken);
    }
}
