package com.douncoding.noe.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.douncoding.noe.R;
import com.orhanobut.logger.Logger;

/**
 * 프라그먼트는 데이터를 처리하는 역할을 수행하지 않으며, View 의 대한 로직만
 * 구현되야 한다.
 * onAttach() > onCreate() > onViewCreated() > onActivityCreated() > onStart() > onResume()
 */
public abstract class BaseFragment extends Fragment implements BaseContract.View {
    public abstract int getContentViewId();
    public abstract BasePresenter getPresenter();

    /**
     * View 를 초기화하는 과정에서 Activity 의 Context 를 사용하는 로직을 구현하는 것에 주의해야 한다.
     * {@link BaseFragment#initData()} 호출 지점에서 Context 의 의존적인 초기화로직을 수행하길 바란다.
     */
    protected abstract void initArguments(Bundle bundle);
    protected abstract void initContentView(View view);
    // Activity Context 를 필요로 하는 처리가 있는 경우 재정의 하여 사용
    protected abstract void initData();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initArguments(getArguments());
        return inflater.inflate(getContentViewId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initContentView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter() != null) {
            getPresenter().onResumeFromContext();
            getPresenter().attachView(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getPresenter() != null)
            getPresenter().onPauseFromContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroyFromContext();
            getPresenter().detachView();
        }
    }

    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private MaterialDialog mProgressDialog;
    protected void showProgressDialog(String message) {
        mProgressDialog = new MaterialDialog.Builder(getContext())
                .content(message)
                .progress(true, 0)
                .show();
    }

    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public void showSystemErrorMessage(String message) {
        showToastMessage(message + ":" + getString(R.string.announce_system_error_message));
    }
}
