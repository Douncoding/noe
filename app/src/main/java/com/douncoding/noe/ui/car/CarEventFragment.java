package com.douncoding.noe.ui.car;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.babys_action.BabyMgmtActivity;
import com.douncoding.noe.ui.car_action.CarMgmtActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarEventFragment extends BaseFragment implements CarEventContract.View {
    public static final String TAG = CarEventFragment.class.getSimpleName();
    public static CarEventFragment newInstance() {
        Bundle args = new Bundle();
        CarEventFragment fragment = new CarEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_empty_view) ViewGroup emptyView;
    @BindView(R.id.event_list) RecyclerView eventList;

    private CarEventAdapter adapter;
    private CarEventPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_car_event;
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
        presenter = new CarEventPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        setupRecyclerView();
        presenter.onLoadEvent();
    }

    @OnClick(R.id.management_btn) public void onManagementButtonClick() {
        CarMgmtActivity.startActivity(super.getContext());
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        eventList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void renderPetEventList(List<TrackEvent> items) {
        if (items.size() <= 0) {
            emptyView.setVisibility(View.VISIBLE);
            eventList.setVisibility(View.GONE);
        } else {
            adapter = new CarEventAdapter(items);
            eventList.setAdapter(adapter);
            eventList.scrollToPosition(items.size());

            emptyView.setVisibility(View.GONE);
            eventList.setVisibility(View.VISIBLE);
        }
    }
}
