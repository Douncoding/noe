package com.douncoding.noe.ui.pay_action.register;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.douncoding.noe.R;
import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.component.BeaconItemAdapter;
import com.douncoding.noe.util.FirebaseUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayRegisterFragment extends BaseFragment implements PayRegisterContract.View {
    public static final String TAG = PayRegisterFragment.class.getSimpleName();
    public static PayRegisterFragment newInstance() {
        Bundle args = new Bundle();
        PayRegisterFragment fragment = new PayRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.basic_info_1) EditText mBankName;
    @BindView(R.id.basic_info_2) EditText mAccountNumber;
    @BindView(R.id.basic_info_3) EditText mPassword;

    @BindView(R.id.beacon_uuid) EditText mBeaconUuidEdit;
    @BindView(R.id.beacon_major) EditText mBeaconMajorEdit;
    @BindView(R.id.beacon_minor) EditText mBeaconMinorEdit;

    private PayRegisterPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_pay_register;
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
        presenter = new PayRegisterPresenter();
        presenter.attachView(this);

        setHasOptionsMenu(true);
        setupBeaconEditListener();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_register, menu);
        // DONE 버튼 색상 변경
        Drawable icon = menu.getItem(0).getIcon();
        icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.payPrimaryColor), PorterDuff.Mode.SRC_IN);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                onSubmitClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBeaconEditListener() {
        mBeaconMajorEdit.setOnFocusChangeListener((view, b) -> {
            if (b) presenter.searchAroundBeacon();
        });

        mBeaconMinorEdit.setOnFocusChangeListener((view, b) -> {
            if (b) presenter.searchAroundBeacon();
        });
    }

    private void onSubmitClicked() {
        String builder =
                 mBeaconUuidEdit.getText().toString() +
                 mBeaconMajorEdit.getText().toString() +
                 mBeaconMinorEdit.getText().toString();

        Pay pay = new Pay(
                FirebaseUtil.getCurrentUserId(),
                builder,
                mBankName.getText().toString(),
                mAccountNumber.getText().toString(),
                mPassword.getText().toString());
        presenter.onSubmit(pay);
    }

    @Override
    public void renderBeaconData(Beacon beacon) {
        showToastMessage("자동으로 찾았습니다. 비콘 뒷면의 번호를 확인해주세요.");

        mBeaconUuidEdit.setText(beacon.getUuid());
        mBeaconMajorEdit.setText(beacon.getMajor());
        mBeaconMinorEdit.setText(beacon.getMinor());
    }

    @Override
    public void renderChooserBeaconDialog(List<Beacon> beaconList) {
        BeaconItemAdapter.OnCallback callback = beacon -> {
            mBeaconUuidEdit.setText(beacon.getUuid());
            mBeaconMajorEdit.setText(beacon.getMajor());
            mBeaconMinorEdit.setText(beacon.getMinor());
        };

        new MaterialDialog.Builder(getContext())
                .title("비콘을 선택하세요.")
                .adapter(new BeaconItemAdapter(beaconList, callback), null)
                .show();
    }

    @Override
    public void showSearchBeaconFailure() {
        showToastMessage("주변의 비콘을 검색할 수 없습니다. 비콘의 전원을 확인 한 이후에 다시 시도해주세요.");
    }

    @Override
    public void showProgressDialog() {
        super.showProgressDialog("결제수단을 등록 중 입니다...");
    }

    @Override
    public void hideProgressDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void showRegisterSuccess() {
        showToastMessage("결제수단 등록을 완료하였습니다.");
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showRegisterFailure() {
        showToastMessage("등록에 실패하였습니다. 다시 시도하세요.");
    }

    @Override
    public void showRequestInputField(int loc) {
        showToastMessage("모든 정보를 정확하게 입력하세요.");
    }

    @Override
    public void showRegisterFailureWithAlreadyBeacon() {
        showToastMessage("이미 등록된 비콘 입니다. 다른 비콘을 이용해 시도해주세요.");
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
