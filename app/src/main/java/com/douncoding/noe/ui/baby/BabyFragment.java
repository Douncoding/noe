package com.douncoding.noe.ui.baby;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BabyFragment extends BaseFragment implements BabyContract.View {
    public static final String TAG = BabyFragment.class.getSimpleName();
    public static BabyFragment newInstance() {
        Bundle args = new Bundle();
        BabyFragment fragment = new BabyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_empty_view) ViewGroup emptyView;
    @BindView(R.id.event_list) RecyclerView eventList;
    private BabyPresenter presenter;
    private BabyEventAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_baby;
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
        presenter = new BabyPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        this.setupRecyclerView();
        presenter.onLoad();
    }

    @OnClick(R.id.management_btn) public void onManagementButtonClick() {
        BabyMgmtActivity.startActivity(super.getContext());
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
            adapter = new BabyEventAdapter(items);
            eventList.setAdapter(adapter);
            eventList.scrollToPosition(items.size());

            emptyView.setVisibility(View.GONE);
            eventList.setVisibility(View.VISIBLE);
        }
    }
}
