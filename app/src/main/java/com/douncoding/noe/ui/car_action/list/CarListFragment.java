package com.douncoding.noe.ui.car_action.list;

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
import com.douncoding.noe.model.Car;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseFragment;
import com.douncoding.noe.ui.pets_action.list.PetListContract;
import com.douncoding.noe.ui.pets_action.list.PetListPagerAdapter;
import com.douncoding.noe.ui.pets_action.list.PetListPresenter;
import com.douncoding.noe.ui.pets_action.list.PetViewHolder;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class CarListFragment extends BaseFragment implements CarListContract.View {
    public static final String TAG = CarListFragment.class.getSimpleName();
    public static CarListFragment newInstance() {
        Bundle args = new Bundle();
        CarListFragment fragment = new CarListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.item_list) HorizontalInfiniteCycleViewPager recyclerView;
    @BindView(R.id.item_empty_view) ViewGroup mEmptyView;
    @BindView(R.id.bg_picture) ImageView mBackgroundPicture;

    private CarListPagerAdapter adapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private CarListPresenter presenter;

    private OnFragmentListener onFragmentListener;
    public interface OnFragmentListener {
        void onNewClick();
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
        return R.layout.fragment_car_list;
    }

    @Override
    public CarListPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    protected void initContentView(View view) {
        ButterKnife.bind(this, view);
        presenter = new CarListPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initData() {
        setupRecyclerView();
        // presenter.onItemLoad(); // 갱신시 자동 호출 되도록 변경}
    }

    /**
     * 수평형 타입의 리스트 뷰이며 양쪽 사이드 아이템이 같이 보이는 형태
     */
    private void setupRecyclerView() {
        adapter = new CarListPagerAdapter(new CarListPagerAdapter.OnSetupViewListener() {
            @Override
            public void onSetupView(CarViewHolder holder, Car car, int position, String key) {
                setupItem(holder, car, position, key);
            }
        });
        recyclerView.setAdapter(adapter);

        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                // getRealItem() 은제 활성화되는것인가... 처음 쓰는 라이브러리는 삽질이 항상 심하네.
                Car car = adapter.getData(position);
                if (car != null) {
                    Glide.with(getContext())
                            .load(car.getPictureUrl())
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

    @OnClick(R.id.new_item) public void onNewClicked() {
        onFragmentListener.onNewClick();
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
    public void addCar(String key, Car item) {
        mEmptyView.setVisibility(View.GONE);
        adapter.add(key, item);
    }

    private void setupItem(CarViewHolder holder, Car car, int position, String key) {
        Glide.with(getContext())
                .load(car.getPictureUrl())
                .thumbnail(0.1f)
                .into(holder.faceview);

        holder.carname.setText(car.getName());
        holder.licenseplate.setText(car.getLicenseplate());
        holder.birthday.setText(car.getBirthday());
        holder.beacon.setText(car.getBeacon().getMajor() + ":" + car.getBeacon().getMinor());

        holder.setOnListener(new CarViewHolder.OnListener() {
            @Override
            public void onItemClicked() {
            }
        });

        // 첫 페이지 배경
        if (position == 0) {
            recyclerView.post(() -> onPageChangeListener.onPageSelected(0));
        }
    }
}
