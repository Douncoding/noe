package com.douncoding.noe.ui;

import android.app.Activity;
import android.content.Context;

/**
 * Created by douncoding on 2016. 12. 2..
 */

public interface BaseContract {
    interface View {
        Activity activity();
        void showSystemErrorMessage(String message);
    }

    interface Presenter {
        void attachView(BaseContract.View view);
        void detachView();
        Context getContext();
        Activity getActivity();
    }
}
