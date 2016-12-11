package com.douncoding.noe.ui.splash;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.douncoding.noe.ui.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.Logger;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.douncoding.noe.Constants.RC_INDISPENSABLE_PERMISSION_GROUP;

public class SplashPresenter extends BasePresenter<SplashContract.View> implements
        EasyPermissions.PermissionCallbacks, SplashContract.Presenter {


    @Override
    public void onResumeFromContext() {
        checkRequirePermissionWithLogin();
    }

    @Override
    public void onPauseFromContext() {

    }

    @Override
    public void onDestroyFromContext() {

    }

    @AfterPermissionGranted(RC_INDISPENSABLE_PERMISSION_GROUP)
    private void checkRequirePermissionWithLogin() {
        String[] permissions = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), permissions)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Logger.i("로그인 상태: Email:%s UID:%s", user.getEmail(), user.getUid());
                mView.showMainContentScreen();
            } else {
                Logger.i("비로그인 상태: 회원가입 요청");
                mView.showLoginScreen();
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, RC_INDISPENSABLE_PERMISSION_GROUP);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        mView.showPermissionDenyMessage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
