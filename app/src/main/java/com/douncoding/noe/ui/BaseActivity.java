package com.douncoding.noe.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.component.ProgressDialogFragment;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {
    protected View rootView;

    // Content Layout Resource
    public abstract int getContentViewId();
    public abstract void initState(Bundle savedInstanceState);
    public abstract void initUiAndListener();

    // UI 바인드 시점이전에 처리해야할 항목이 있다면 재정의 해서 사용
    public abstract void initInjector();
    // 액티비티에서 기능을 직접 처리하는 경우는 재정의 해서 사용
    @SuppressWarnings("unchecked")
    public abstract <P extends BasePresenter<BaseContract.View>> P getPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
//        this.setSystemBarTintable();

        rootView = (ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);

        this.initState(savedInstanceState);
        this.initInjector();
        this.initUiAndListener();


    }

    @Override
    protected void onStart() {
        super.onStart();
        onBackStackChangedListener = getFragmentListener();
        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (onBackStackChangedListener != null)
            getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
    }

    /**
     * 풀스크린 형태의 액티비티가 필요한 경우 오버라이드하여 반환값을 'true' 로 설정한다.
     */
    protected boolean isFullscreen() {
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFullscreen()) {
            setFullscreen();
        }
    }

    /**
     * https://developer.android.com/training/system-ui/immersive.html#sticky
     * Sticky immersive mode
     */
    private void setFullscreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onResume() {
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

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public void showSystemErrorMessage(String message) {
        showToastMessage(message + ":" + getString(R.string.announce_system_error_message));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener;
    private FragmentManager.OnBackStackChangedListener getFragmentListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                int count = manager.getBackStackEntryCount();
                if (count == 0) {
                    finish();
                } else {
                    Fragment fragment = manager.getFragments().get(count - 1);
                    fragment.onResume();
                }
            }
        };
    }

    public <T extends BaseFragment> void addFragment(@IdRes int containerViewId, T fragment, @Nullable String tag) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        if (getCurrentFragment() != null) {
            transaction.hide(getCurrentFragment());
        }

        Fragment instance = getSupportFragmentManager().findFragmentByTag(tag);
//        transaction.setCustomAnimations(android.R.anim.fade_in, 0);
        transaction.addToBackStack(null);

        if (instance == null) {
            transaction.add(containerViewId, fragment, tag);
            transaction.commit();
        } else {
            transaction.show(instance);
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    public <T extends BaseFragment> void replaceFragment(@IdRes int containerViewId, T fragment, @Nullable String tag) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, 0);
        transaction.replace(containerViewId, fragment, tag);
        transaction.commit();

        // 완료까지 지연
        getSupportFragmentManager().executePendingTransactions();
    }

    public BaseFragment getFragmentByTag(String tag) {
        return (BaseFragment)this.getSupportFragmentManager().findFragmentByTag(tag);
    }

    public BaseFragment getCurrentFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return (BaseFragment)fragment;
            }
        }

        return null;
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 예외처리 하지 않은 항목에러 발생시 처리를 공통으로 정의한다.
     */
    public void showNotCacheSystemError () {
        showToastMessage(getString(R.string.announce_failure_system));
        finish();
    }

    private static final String TAG_DIALOG_FRAGMENT = "tagDialogFragment";
    protected void showProgressDialog(String message) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getExistingDialogFragment();
        if (prev == null) {
            ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(message);
            fragment.show(ft, TAG_DIALOG_FRAGMENT);
        }
    }

    protected void dismissProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getExistingDialogFragment();
        if (prev != null) {
            ft.remove(prev).commit();
        }
    }

    private Fragment getExistingDialogFragment() {
        return getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_FRAGMENT);
    }
}
