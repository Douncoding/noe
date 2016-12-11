package com.douncoding.noe.ui.babys_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.model.TrackType;
import com.douncoding.noe.repository.TTargetRepository;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class BabyListPresenter extends BasePresenter<BabyListContract.View> implements  BabyListContract.Presenter {

    private Map<String, Baby> mTargetMap; // 현재 트래킹 중인 목록

    @Override
    public void onResumeFromContext() {
        mTargetMap = new HashMap<>();
        TTargetRepository.getInstance().getList((key, json, type) -> {
            if (type.equals(TrackType.PET.name())) {
                mTargetMap.put(key, new Gson().fromJson(json, Baby.class));
            }
        });

        mView.renderSelectedCount(mTargetMap.size());
    }

    @Override
    public void onPauseFromContext() {

    }

    @Override
    public void onDestroyFromContext() {

    }

    @Override
    public void onItemLoad() {
        mView.showItemLoadingDialog();

        FirebaseUtil.getCurrentUserHasBabyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUtil.getBabysRef().child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mView.addBabyList(dataSnapshot.getKey(), dataSnapshot.getValue(Baby.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                mView.dismissItemLoadingDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onInsertTarget(String key, Baby baby) {
        if (!mTargetMap.containsKey(key)) {
//            BeaconService.instance.addTarget(key, TrackType.PET, pe);
//            mTargetMap.put(key, pet);
            mView.updateItem(key, true);
            mView.renderSelectedCount(mTargetMap.size());
        } else {
            Logger.w("추적목록 추가 실패: 이미 포함된 키:" + key);
        }
    }

    @Override
    public void onRemoveTarget(String key, Baby baby) {
        if (mTargetMap.containsKey(key)) {
//            BeaconService.instance.removeTarget(key);
//            mTargetMap.remove(key);
            mView.updateItem(key, false);
            mView.renderSelectedCount(mTargetMap.size());
        } else {
            Logger.w("추적목록 삭제 실패: 포함되지 않은 키:" + key);
        }
    }

    public boolean isTrackingTarget(String key) {
        return mTargetMap.containsKey(key);
    }

}