package com.douncoding.noe.ui.pets_action.list;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.douncoding.noe.R;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Firebase 경로를 아이템으로 받으며, 경로를 기준으로 아이템을 직접 검색하여 View 를 생성한다.
 *
 * 왜?
 * {@link com.firebase.ui.database.FirebaseRecyclerAdapter} 를 사용하는 경우는 단일 쿼리로 원하는 데이터를 구할 수
 * 있는 경우에만 가능하다. 하지만, 로그인한 사용자가 소유한 Pet의 세부정보에 접근하는 것은 최소 2번의 쿼리가 수행되어야 하기때문에
 * 직접 Adapter 를 구현하여 사용해야한다. 또한, 파이어베이스는 비동기형태로만 조회가 가능하기 때문에 "패스" 아이템을 통해
 * 처리하는 로직이 추가로 구현되어야 한다.
 *
 * PS. 아이템의 리스터를 처리하는 부분은 초기화 이후에 변경되는 아이템에 대한 대응을 할 수 있다는 점과 View 로직에 관한 처리를
 * 외부에서 처리한다는 방식에 주의를 기울어야 한다.
 */

public class PetListPagerAdapter extends PagerAdapter {
    private List<String> keySet;
    private List<Pet> dataSet;
    private OnSetupViewListener onSetupViewListener;

    public PetListPagerAdapter(OnSetupViewListener onSetupViewListener) {
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
        final View view = inflater.inflate(R.layout.list_item_pet, container, false);
        final PetViewHolder holder = new PetViewHolder(view);

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
    public void add(String key, Pet pet) {
        keySet.add(key);
        dataSet.add(pet);
        notifyDataSetChanged();
    }

    public Pet getData(int position) {
        return dataSet.get(position);
    }

    public interface OnSetupViewListener {
        void onSetupView(PetViewHolder holder, Pet pet, int position, String key);
    }
}
