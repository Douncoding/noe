package com.douncoding.noe.ui.pet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.pets_action.PetMgmtActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PetFragment extends BaseFragment implements PetContract.View {
    public static final String TAG = PetFragment.class.getSimpleName();
    public static PetFragment newInstance() {
        Bundle args = new Bundle();
        PetFragment fragment = new PetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_empty_view) ViewGroup emptyView;
    @BindView(R.id.event_list) RecyclerView petsEventList;
    private PetPresenter presenter;

    private PetEventAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_pet;
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
        presenter = new PetPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        this.setupRecyclerView();

        presenter.onLoad();
    }

    @OnClick(R.id.management_btn) public void onManagementButtonClick() {
        PetMgmtActivity.startActivity(super.getContext());
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        petsEventList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void renderPetEventList(List<TrackEvent> items) {
        if (items.size() <= 0) {
            emptyView.setVisibility(View.VISIBLE);
            petsEventList.setVisibility(View.GONE);
        } else {
            adapter = new PetEventAdapter(items);
            petsEventList.setAdapter(adapter);
            petsEventList.scrollToPosition(items.size());

            emptyView.setVisibility(View.GONE);
            petsEventList.setVisibility(View.VISIBLE);
        }
    }
}
