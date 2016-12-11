package com.douncoding.noe.ui.car_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Car;
import com.douncoding.noe.model.TrackType;
import com.douncoding.noe.repository.TTargetRepository;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.babys_action.list.BabyListContract;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class CarListPresenter extends BasePresenter<CarListContract.View> implements  CarListContract.Presenter {

    @Override
    public void onResumeFromContext() {
        this.onItemLoad();
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

        FirebaseUtil.getCurrentUserHasCarRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUtil.getCarsRef().child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getKey() != null && dataSnapshot.getValue() != null)
                                mView.addCar(dataSnapshot.getKey(), dataSnapshot.getValue(Car.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            databaseError.toException().printStackTrace();
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
}