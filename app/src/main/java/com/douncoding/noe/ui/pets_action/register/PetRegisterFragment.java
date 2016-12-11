package com.douncoding.noe.ui.pets_action.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.douncoding.noe.R;
import com.douncoding.noe.model.Beacon;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseActivity;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.component.BeaconItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PetRegisterFragment extends BaseFragment implements PetRegisterContract.View {
    public static final String TAG = PetRegisterFragment.class.getSimpleName();
    public static PetRegisterFragment newInstance() {
        Bundle args = new Bundle();
        PetRegisterFragment fragment = new PetRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.gender_male) ViewGroup mGenderMaleView;
    @BindView(R.id.gender_female) ViewGroup mGenderFemaleView;
    @BindView(R.id.add_picture) ViewGroup mAddPictureView;
    @BindView(R.id.face_picture) CircleImageView mPictureImage;
    @BindView(R.id.name) EditText mNameEdit;
    @BindView(R.id.birthday) EditText mBirthdayEdit;
    @BindView(R.id.nickname) EditText mNicknameEdit;
    @BindView(R.id.beacon_uuid) EditText mBeaconUuidEdit;
    @BindView(R.id.beacon_major) EditText mBeaconMajorEdit;
    @BindView(R.id.beacon_minor) EditText mBeaconMinorEdit;

    private PetRegisterPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_pet_register;
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
        presenter = new PetRegisterPresenter();
        presenter.attachView(this);

        setHasOptionsMenu(true);

        setupGenderRadioGroup();
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
        icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.petPrimaryColor), PorterDuff.Mode.SRC_IN);
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

    /**
     * 성별 선택 설정
     */
    private int mGenderSelectedPosition = -1;
    private void setupGenderRadioGroup() {
        final int disableColor = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        final int focusColor = ContextCompat.getColor(getContext(), R.color.petPrimaryColor);

        mGenderMaleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGenderMaleView.setBackgroundColor(focusColor);
                mGenderFemaleView.setBackgroundColor(disableColor);
                mGenderSelectedPosition = 0;
            }
        });

        mGenderFemaleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGenderMaleView.setBackgroundColor(disableColor);
                mGenderFemaleView.setBackgroundColor(focusColor);
                mGenderSelectedPosition = 1;
            }
        });
    }

    /**
     * 비콘 자동검색 기능 수행
     * 등록된 리스너는 검색실패 시 필히 제거하여 사용자가 직접 입력할 수 있도록 한다.
     */
    private void setupBeaconEditListener() {
        mBeaconMajorEdit.setOnFocusChangeListener((view, b) -> {
            if (b) presenter.searchAroundBeacon();
        });

        mBeaconMinorEdit.setOnFocusChangeListener((view, b) -> {
            if (b) presenter.searchAroundBeacon();
        });
    }

    @OnClick(R.id.add_picture) public void onAddPictureClick() {
        presenter.onShowImagePicker();
    }

    private void onSubmitClicked() {
        Beacon beacon = new Beacon(mBeaconUuidEdit.getText().toString(),
                mBeaconMajorEdit.getText().toString(), mBeaconMinorEdit.getText().toString());

        Pet pet = new Pet(
                mNameEdit.getText().toString(),
                mBirthdayEdit.getText().toString(),
                mNicknameEdit.getText().toString(),
                mGenderSelectedPosition,
                null, beacon);
        presenter.onSubmit(pet);
    }

    /**
     * {@link com.douncoding.noe.ui.pets_action.PetMgmtActivity} ->
     * {@link PetRegisterFragment} ->
     * {@link PetRegisterPresenter} -> 로 전달 하는과정...
     *
     * 안드로이드 자원자체를 이용하는 경우 이런 불편한점들이 발생한다. EventBus 와 같은 라이브리를 사용해야 할가? 고민이다.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderFacePicture(Bitmap bitmap) {
        mPictureImage.setImageBitmap(bitmap);
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
        showToastMessage("주변의 비콘을 검색할 수 없습니다. 직접으로 입력하세요.");
    }

    @Override
    public void showProgressDialog(String message) {
        super.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void showRegisterSuccess() {
        showToastMessage("친구 등록을 완료하였습니다.");
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showRegisterFailure() {
        showToastMessage("친구 등록을 실패하였습니다. 다시 시도하세요.");
    }

    @Override
    public void showRequestInputField(int loc) {
        showToastMessage("모든 정보를 정확하게 입력하세요.");
    }
}
