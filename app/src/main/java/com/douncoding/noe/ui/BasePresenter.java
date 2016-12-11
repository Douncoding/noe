package com.douncoding.noe.ui;

import android.app.Activity;
import android.content.Context;

/**
 * Created by douncoding on 2016. 12. 2..
 */

public abstract class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    protected T mView;

    @SuppressWarnings("unchecked")
    @Override
    public void attachView(BaseContract.View view) {
        mView = (T)view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    // Presenter 생존주기를 Context 의 생존주기에 대응
    public abstract void onResumeFromContext();
    public abstract void onPauseFromContext();
    public abstract void onDestroyFromContext();

    @Override
    public Context getContext() {
        if (mView.activity() == null) {
            throw new NullPointerException("이미 반환된 액티비티의 Presenter 이거나 액티비티가 바인드 되지 않은 상태");
        } else {
            return mView.activity().getBaseContext();
        }
    }

    @Override
    public Activity getActivity() {
        if (mView.activity() == null) {
            throw new NullPointerException("이미 반환된 액티비티의 Presenter 이거나 액티비티가 바인드 되지 않은 상태");
        } else {
            return mView.activity();
        }
    }
}