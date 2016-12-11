package com.douncoding.noe.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by douncoding on 2016. 12. 3..
 */

public class MenuFragment extends BaseFragment {
    public static final String TAG = MenuFragment.class.getSimpleName();
    public static MenuFragment newInstance() {
        Bundle args = new Bundle();
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnFragmentListener onFragmentListener;
    public interface OnFragmentListener {
        void onBabyClick();
        void onPetClick();
        void onCarClick();
        void onPayClick();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            this.onFragmentListener = (OnFragmentListener)context;
        } else {
            throw new IllegalArgumentException("must be implements OnFragmentListener.");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_menu;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.baby_container) public void onBabyClick() {
        onFragmentListener.onBabyClick();
    }

    @OnClick(R.id.pet_container) public void onPetClick() {
        onFragmentListener.onPetClick();
    }

    @OnClick(R.id.car_container) public void onCarClick() {
        onFragmentListener.onCarClick();
    }

    @OnClick(R.id.pay_container) public void onPayClick() {
        onFragmentListener.onPayClick();
    }
}
