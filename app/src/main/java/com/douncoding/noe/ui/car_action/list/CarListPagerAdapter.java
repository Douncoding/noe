package com.douncoding.noe.ui.car_action.list;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Car;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.pets_action.list.PetViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CarListPagerAdapter extends PagerAdapter {
    private List<String> keySet;
    private List<Car> dataSet;
    private OnSetupViewListener onSetupViewListener;

    public CarListPagerAdapter(OnSetupViewListener onSetupViewListener) {
        this.keySet = new ArrayList<>();
        this.dataSet = new ArrayList<>();
        this.onSetupViewListener = onSetupViewListener;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * instantiateItem 메소드에서 생성한 객체를 이용할 것인지 여부를 반환 한다.
     * Default 는 false 이며, false 인 경우 커스텀으로 생성한 View 를 사용할 수 없다.
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        final View view = inflater.inflate(R.layout.list_item_car, container, false);
        final CarViewHolder holder = new CarViewHolder(view);

        onSetupViewListener.onSetupView(holder, dataSet.get(position), position, keySet.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    /**
     * 디자인 때문에 페이지어댑터를 사용했는데...  페이지 어덥터는 하나의 아이템(프라그먼트)만 갱신하는 것이 블가능? 하다는 사실
     * 을 처음알았다. 이는 사용자가 액션을 수행한이후 자신이 액션을 수행한 아이템이 항상 최상단에 있기때문에 따로 보완하지 않고
     * 현재의 이슈를 가지고 간다. 만약 변경하고 싶다면, 프라그먼트의 태그를 이용하거나 인스턴스를 가지고와서 하드코딩하는 과정을
     * 수행하면 된다.
     */
    public void add(String key, Car car) {
        keySet.add(key);
        dataSet.add(car);
        notifyDataSetChanged();
    }

    public Car getData(int position) {
        return dataSet.get(position);
    }

    public interface OnSetupViewListener {
        void onSetupView(CarViewHolder holder, Car car, int position, String key);
    }
}
