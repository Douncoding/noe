package com.douncoding.noe.ui.pay_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

public class PayListPresenter extends BasePresenter<PayListContract.View> implements PayListContract.Presenter {

    @Override
    public void onResumeFromContext() {

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

        FirebaseUtil.getCurrentUserHasPayRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUtil.getPaysRef().child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null)
                                mView.addPayment(dataSnapshot.getKey(), dataSnapshot.getValue(Pay.class));
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

    @Override
    public void onRemove(Pay pay) {

    }
}
