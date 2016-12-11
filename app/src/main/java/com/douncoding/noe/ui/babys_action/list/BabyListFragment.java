package com.douncoding.noe.ui.babys_action.list;

import android.content.Context;
import android.graphics.Color;
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
import com.douncoding.noe.model.Baby;
import com.douncoding.noe.ui.BaseFragment;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class BabyListFragment extends BaseFragment implements BabyListContract.View {
    public static final String TAG = BabyListFragment.class.getSimpleName();
    public static BabyListFragment newInstance() {
        Bundle args = new Bundle();
        BabyListFragment fragment = new BabyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.baby_list) HorizontalInfiniteCycleViewPager recyclerView;
    @BindView(R.id.item_empty_view) ViewGroup mEmptyView;
    @BindView(R.id.bg_picture) ImageView mBackgroundPicture;
    @BindView(R.id.select_count) TextView mSelectedCountText;

    private BabyListPagerAdapter adapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private BabyListPresenter presenter;

    private OnFragmentListener onFragmentListener;
    public interface OnFragmentListener {
        void onNewBabyClick();
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

    @OnClick(R.id.new_baby) public void onNewBabyClicked() {
        onFragmentListener.onNewBabyClick();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_baby_list;
    }

    @Override
    public BabyListPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
        presenter = new BabyListPresenter();
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
        adapter = new BabyListPagerAdapter(new BabyListPagerAdapter.OnSetupViewListener() {
            @Override
            public void onSetupView(BabyViewHolder holder, Baby baby, int position, String key) {
                setupBaby(holder, baby, position, key);
            }
        });
        recyclerView.setAdapter(adapter);

        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // getRealItem() 은제 활성화되는것인가... 처음 쓰는 라이브러리는 삽질이 항상 심하네.
                Baby baby = adapter.getData(position);
                if (baby != null) {
                    Glide.with(getContext())
                            .load(baby.getPictureUrl())
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

    @Override
    public void showItemLoadingDialog() {
        super.showProgressDialog("자녀 목록 내려받는중...");
    }

    @Override
    public void dismissItemLoadingDialog() {
        super.dismissProgressDialog();
    }

    @Override
    public void addBabyList(String key, Baby item) {
        mEmptyView.setVisibility(View.GONE);
        adapter.add(key, item);
    }

    private void setupBaby(BabyViewHolder holder, Baby baby, int position, String key) {
        final boolean tracking = presenter.isTrackingTarget(key);

        Glide.with(getContext())
                .load(baby.getPictureUrl())
                .thumbnail(0.1f)
                .into(holder.faceview);

        holder.petname.setText(baby.getName());
        holder.nickname.setText(baby.getNickname());
        holder.birthday.setText(baby.getBirthday());
        holder.beacon.setText(baby.getBeacon().getMajor() + ":" + baby.getBeacon().getMinor());

        if (tracking) {
            holder.trackstate.setVisibility(View.VISIBLE);
            holder.startAction.setTextColor(Color.parseColor("#20FFFFFF"));
            holder.stopAction.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        } else {
            holder.trackstate.setVisibility(View.GONE);
            holder.startAction.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            holder.stopAction.setTextColor(Color.parseColor("#20FFFFFF"));
        }

        holder.setOnListener(new BabyViewHolder.OnListener() {
            @Override
            public void onItemClicked() {

            }

            @Override
            public void onStartActionClicked() {
                if (!tracking) {
                    new MaterialDialog.Builder(getContext())
                            .content("감시를 시작하시겠습니까?")
                            .onPositive((dialog, which) -> presenter.onInsertTarget(key, baby))
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
                            .onPositive((dialog, which) -> presenter.onRemoveTarget(key, baby))
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
    public void renderSelectedCount(int count) {
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
