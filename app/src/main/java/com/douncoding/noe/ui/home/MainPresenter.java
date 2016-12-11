package com.douncoding.noe.ui.home;

import com.douncoding.noe.ui.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by douncoding on 2016. 12. 2..
 */

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
    public void onLogout() {
        // 로그아웃 처리 임시동작
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }
}
