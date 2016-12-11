package com.douncoding.noe.ui.pay_action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.BaseContract;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.pay_action.list.PayListFragment;
import com.douncoding.noe.ui.pay_action.list.PayListPresenter;
import com.douncoding.noe.ui.pay_action.register.PayRegisterFragment;

import butterknife.ButterKnife;

public class PayMgmtActivity extends BaseActivity implements PayListFragment.OnFragmentListener {
    public static void startActivity(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, PayMgmtActivity.class));
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_management;
    }

    @Override
    public void initState(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        this.setupToolbar();
        addFragment(R.id.fragment_container, PayListFragment.newInstance(), PayListFragment.TAG);
    }

    @Override
    public void initInjector() {

    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public <P extends BasePresenter<BaseContract.View>> P getPresenter() {
        return null;
    }

    @Override
    public void onNewItemClick() {
        addFragment(R.id.fragment_container, PayRegisterFragment.newInstance(), PayRegisterFragment.TAG);
    }
}
