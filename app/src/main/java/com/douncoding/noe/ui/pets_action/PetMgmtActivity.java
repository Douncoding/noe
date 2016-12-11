package com.douncoding.noe.ui.pets_action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.douncoding.noe.R;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.BaseContract;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.pets_action.list.PetListFragment;
import com.douncoding.noe.ui.pets_action.register.PetRegisterFragment;

import butterknife.ButterKnife;

import static com.douncoding.noe.Constants.TC_PICK_IMAGE;

public class PetMgmtActivity extends BaseActivity implements PetListFragment.OnFragmentListener {
    public static void startActivity(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, PetMgmtActivity.class));
        }
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_pet_management;
    }

    @Override
    public void initState(Bundle savedInstanceState) {

    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        this.setupToolbar();

        addFragment(R.id.fragment_container, PetListFragment.newInstance(), PetListFragment.TAG);
    }

    @Override
    public void initInjector() {

    }

    @Override
    public <P extends BasePresenter<BaseContract.View>> P getPresenter() {
        return null;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewPetClick() {
        addFragment(R.id.fragment_container, PetRegisterFragment.newInstance(), PetRegisterFragment.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (TC_PICK_IMAGE == requestCode) {
                getFragmentByTag(PetRegisterFragment.TAG).onActivityResult(requestCode, resultCode, data);

            }
        }
    }
}
