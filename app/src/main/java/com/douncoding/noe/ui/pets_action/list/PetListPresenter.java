package com.douncoding.noe.ui.pets_action.list;

import com.douncoding.noe.model.Pet;
import com.douncoding.noe.model.TrackType;
import com.douncoding.noe.repository.TTargetRepository;
import com.douncoding.noe.service.BeaconService;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetListPresenter extends BasePresenter<PetListContract.View> implements  PetListContract.Presenter {

    private Map<String, Pet> mTargetMap; // 현재 트래킹 중인 목록

    @Override
    public void onResumeFromContext() {
        mTargetMap = new HashMap<>();
        TTargetRepository.getInstance().getList((key, json, type) -> {
            if (type.equals(TrackType.PET.name())) {
                mTargetMap.put(key, new Gson().fromJson(json, Pet.class));
            }
        });

        mView.renderSelectedPetCount(mTargetMap.size());
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

        FirebaseUtil.getCurrentUserHasPetRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUtil.getPetsRef().child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mView.addPetList(dataSnapshot.getKey(), dataSnapshot.getValue(Pet.class));
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
    public void onInsertTarget(String key, Pet pet) {
        if (!mTargetMap.containsKey(key)) {
            BeaconService.instance.addTarget(key, TrackType.PET, pet);
            mTargetMap.put(key, pet);
            mView.updateItem(key, true);
            mView.renderSelectedPetCount(mTargetMap.size());
        } else {
            Logger.w("추적목록 추가 실패: 이미 포함된 키:" + key);
        }
    }

    @Override
    public void onRemoveTarget(String key, Pet pet) {
        if (mTargetMap.containsKey(key)) {
            BeaconService.instance.removeTarget(key);
            mTargetMap.remove(key);
            mView.updateItem(key, false);
            mView.renderSelectedPetCount(mTargetMap.size());
        } else {
            Logger.w("추적목록 삭제 실패: 포함되지 않은 키:" + key);
        }
    }

    public boolean isTrackingTarget(String key) {
        return mTargetMap.containsKey(key);
    }

}