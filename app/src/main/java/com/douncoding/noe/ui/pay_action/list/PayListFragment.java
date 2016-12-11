package com.douncoding.noe.ui.pay_action.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.babys_action.list.BabyListFragment;
import com.douncoding.noe.ui.pets_action.list.PetListPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by douncoding on 2016. 12. 9..
 */

public class PayListFragment extends BaseFragment implements PayListContract.View{
    public static final String TAG = PayListFragment.class.getSimpleName();
    public static PayListFragment newInstance() {
        Bundle args = new Bundle();
        PayListFragment fragment = new PayListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_empty_view) ViewGroup mEmptyView;
    @BindView(R.id.item_list)RecyclerView mRecyclerView;

    private PayListAdapter adapter;
    private PayListPresenter presenter;

    private OnFragmentListener onFragmentListener;
    public interface OnFragmentListener {
        void onNewItemClick();
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
        return R.layout.fragment_pay_list;
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
        presenter = new PayListPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        setupRecyclerView();
        presenter.onItemLoad();
    }

    @OnClick(R.id.new_item) public void onNewItemClicked() {
        onFragmentListener.onNewItemClick();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayListAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showItemLoadingDialog() {
        super.showProgressDialog("목록 내려받는중...");
    }

    @Override
    public void dismissItemLoadingDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void addPayment(String key, Pay item) {
        mEmptyView.setVisibility(View.GONE);
        adapter.add(item);
    }
}
