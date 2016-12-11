package com.douncoding.noe.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.BaseContract;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.home.MainActivity;
import com.douncoding.noe.ui.login.LoginActivity;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initState(Bundle savedInstanceState) {

    }

    @Override
    public void initInjector() {
        presenter = new SplashPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SplashPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showPermissionDenyMessage() {
        Snackbar.make(rootView, "앱 실행의 위한 최소권한을 충족하지 못했습니다.", Snackbar.LENGTH_SHORT);
    }

    @Override
    public void showLoginScreen() {
        new Handler().postDelayed(() -> {
            LoginActivity.startActivity(this);
            finish();
        }, 2000);
    }

    @Override
    public void showMainContentScreen() {
        new Handler().postDelayed(() -> {
            MainActivity.startActivity(this);
            finish();
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
