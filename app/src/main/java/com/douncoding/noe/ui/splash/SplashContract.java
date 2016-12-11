package com.douncoding.noe.ui.splash;


import com.douncoding.noe.ui.BaseContract;

public interface SplashContract {
    interface View extends BaseContract.View {
        // 앱실행을 위한 기본권한 획득 실패
        void showPermissionDenyMessage();

        // 로그인 화면 출력
        void showLoginScreen();

        // 메인 화면 출력
        void showMainContentScreen();
    }

    interface Presenter extends BaseContract.Presenter {

    }
}
