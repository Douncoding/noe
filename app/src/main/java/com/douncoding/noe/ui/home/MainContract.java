package com.douncoding.noe.ui.home;

import com.douncoding.noe.ui.BaseContract;

/**
 * Created by douncoding on 2016. 12. 2..
 */

public interface MainContract {
    interface View extends BaseContract.View {

    }

    interface Presenter extends BaseContract.Presenter {
        void onLogout();

        void sendToFCM();

        void startBeaconService();
    }
}
