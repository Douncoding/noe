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
import com.orhanobut.logger.Logger;

import java.util.List;

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

    }

    @OnClick(R.id.new_item) public void onNewItemClicked() {
        onFragmentListener.onNewItemClick();
    }

    @Override
    public void showItemLoadingDialog() {
        super.showProgressDialog("목록 내려받는중...");
    }

    @Override
    public void dismissItemLoadingDialog() {
        super.dismissProgressDialog();
    }

    /**
     * 실시간으로 추가될 필요성이 없기 때문에 아이템을 추가하는 형태가 아닌 한번에 랜더링하는 방식으로 변경한다.
     * 새로운 항목이 등록되는 경우에도 Firebase Database의 모든항목에 접근해야 하기때문에 해당 방법이 적합하다고 판단한다.
     */
    @Override
    public void renderPaymentList(List<Pay> items) {
        if (items.size() < 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new PayListAdapter(items);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
