package com.douncoding.noe.ui.pets_action.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.douncoding.noe.R;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseFragment;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PetListFragment extends BaseFragment implements PetListContract.View {
    public static final String TAG = PetListFragment.class.getSimpleName();
    public static PetListFragment newInstance() {
        Bundle args = new Bundle();
        PetListFragment fragment = new PetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_list) HorizontalInfiniteCycleViewPager recyclerView;
    @BindView(R.id.item_empty_view) ViewGroup mEmptyView;
    @BindView(R.id.bg_picture) ImageView mBackgroundPicture;
    @BindView(R.id.select_count) TextView mSelectedCountText;

    private PetListPagerAdapter adapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private PetListPresenter presenter;

    private OnFragmentListener onFragmentListener;
    public interface OnFragmentListener {
        void onNewPetClick();
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
        return R.layout.fragment_pet_list;
    }

    @Override
    public PetListPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
        presenter = new PetListPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        setupRecyclerView();
        presenter.onItemLoad();
    }

    /**
     * 수평형 타입의 리스트 뷰이며 양쪽 사이드 아이템이 같이 보이는 형태
     */
    private void setupRecyclerView() {
        adapter = new PetListPagerAdapter(new PetListPagerAdapter.OnSetupViewListener() {
            @Override
            public void onSetupView(PetViewHolder holder, Pet pet, int position, String key) {
                setupPet(holder, pet, position, key);
            }
        });
        recyclerView.setAdapter(adapter);

        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // getRealItem() 은제 활성화되는것인가... 처음 쓰는 라이브러리는 삽질이 항상 심하네.
                Pet pet = adapter.getData(position);
                if (pet != null) {
                    Glide.with(getContext())
                            .load(pet.getPictureUrl())
                            .thumbnail(0.1f)
                            .bitmapTransform(new BlurTransformation(getContext()))
                            .into(mBackgroundPicture);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        recyclerView.addOnPageChangeListener(onPageChangeListener);
    }

    @OnClick(R.id.new_pet) public void onNewPetClick() {
        onFragmentListener.onNewPetClick();
    }

    @Override
    public void showItemLoadingDialog() {
        super.showProgressDialog("친구 목록 내려받는중...");
    }

    @Override
    public void dismissItemLoadingDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void addPetList(String key, Pet item) {
        mEmptyView.setVisibility(View.GONE);
        adapter.add(key, item);
    }

    /**
     * Model-Presenter-View 구조의 View 에 관한 처리를 Adapter 에 종속시키지 않고, View 로직으로 분리하여 처리한다.
     * @param holder ViewHolder 아이템 뷰
     * @param pet 반려동물 정보
     * @param position 아이템 위치
     * @param key 반려동물 키
     */
    private void setupPet(PetViewHolder holder, Pet pet, int position, String key) {
        final boolean tracking = presenter.isTrackingTarget(key);

        Glide.with(getContext())
                .load(pet.getPictureUrl())
                .thumbnail(0.1f)
                .into(holder.faceview);

        holder.petname.setText(pet.getName());
        holder.nickname.setText(pet.getNickname());
        holder.birthday.setText(pet.getBirthday());
        holder.beacon.setText(pet.getBeacon().getMajor() + ":" + pet.getBeacon().getMinor());

        if (tracking) {
            holder.trackstate.setVisibility(View.VISIBLE);
            holder.startAction.setTextColor(Color.parseColor("#20FFFFFF"));
            holder.stopAction.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            holder.trackstate.setVisibility(View.GONE);
            holder.startAction.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            holder.stopAction.setTextColor(Color.parseColor("#20FFFFFF"));
        }

        holder.setOnListener(new PetViewHolder.OnListener() {
            @Override
            public void onItemClicked() {
            }

            @Override
            public void onStartActionClicked() {
                if (!tracking) {
                    new MaterialDialog.Builder(getContext())
                            .content("감시를 시작하시겠습니까?")
                            .onPositive((dialog, which) -> presenter.onInsertTarget(key, pet))
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .positiveText(android.R.string.ok)
                            .negativeText(android.R.string.cancel)
                            .show();
                }
            }

            @Override
            public void onStopActionClicked() {
                if (tracking) {
                    new MaterialDialog.Builder(getContext())
                            .content("감시를 중지하시겠습니까?")
                            .onPositive((dialog, which) -> presenter.onRemoveTarget(key, pet))
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .positiveText(android.R.string.ok)
                            .negativeText(android.R.string.cancel)
                            .show();
                }
            }
        });

        // 첫 페이지 배경
        if (position == 0) {
            recyclerView.post(() -> onPageChangeListener.onPageSelected(0));
        }
    }

    @Override
    public void renderSelectedPetCount(int count) {
        String message = "";
        switch (count) {
            case 0:
                message = "선 택 없음(" + count + ")";
                break;
            case 1:
                message = "한 개 선택(" + count + ")";
                break;
            case 2:
                message = "두 개 선택(" + count + ")";
                break;
            case 3:
                message = "세 개 선택(" + count + ")";
                break;
            default:
                message = "다 수 선택(" + count + ")";
                break;
        }
        mSelectedCountText.setText(message);
    }

    @Override
    public void updateItem(String key, boolean isSelected) {
        adapter.notifyDataSetChanged();
        showToastMessage("변경을 완료하였습니다.");
    }
}
