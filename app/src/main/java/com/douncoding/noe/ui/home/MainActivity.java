package com.douncoding.noe.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.baby.BabyFragment;
import com.douncoding.noe.ui.car.CarEventFragment;
import com.douncoding.noe.ui.pay_action.PayMgmtActivity;
import com.douncoding.noe.ui.pet.PetFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
    MenuFragment.OnFragmentListener {
    public static void startActivity(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    private MainPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initState(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);

        this.setupToolbar();
        this.setupDrawerNavigation();

        // 시작화면은 항상 선택메뉴 화면으로 고정된다.
        addFragment(R.id.fragment_container, MenuFragment.newInstance(), MenuFragment.TAG);
    }

    @Override
    public void initInjector() {
        presenter = new MainPresenter();
        presenter.attachView(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MainPresenter getPresenter() {
        return presenter;
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupDrawerNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                presenter.onLogout();
                break;
        }
        return false;
    }

    @Override
    public void onBabyClick() {
        addFragment(R.id.fragment_container, BabyFragment.newInstance(), BabyFragment.TAG);
    }

    @Override
    public void onPetClick() {
        addFragment(R.id.fragment_container, PetFragment.newInstance(), PetFragment.TAG);
    }

    @Override
    public void onCarClick() {
        addFragment(R.id.fragment_container, CarEventFragment.newInstance(), CarEventFragment.TAG);
    }

    @Override
    public void onPayClick() {
        // 결제 시스템은 결제내역을 제공하지 않음.
        PayMgmtActivity.startActivity(this);
    }
}
