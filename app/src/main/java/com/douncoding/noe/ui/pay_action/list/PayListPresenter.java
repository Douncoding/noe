package com.douncoding.noe.ui.pay_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class PayListPresenter extends BasePresenter<PayListContract.View> implements PayListContract.Presenter {

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

        FirebaseUtil.getCurrentUserHasPayRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Pay> collection = new ArrayList<>();
                int total = (int)dataSnapshot.getChildrenCount();

                // 등록된 아이템이 없는 경우
                if (total == 0) {
                    Logger.i("등록되 결제 정보가 없습니다.");
                    mView.renderPaymentList(collection);
                    mView.dismissItemLoadingDialog();
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUtil.getPaysRef().child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                collection.add(dataSnapshot.getValue(Pay.class));
                                // 소유한 비콘 목록이 모두 검색된 경우 출력 한다.
                                if (collection.size() == total) {
                                    mView.renderPaymentList(collection);
                                    mView.dismissItemLoadingDialog();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            databaseError.toException().printStackTrace();
                        }
                    });
                }
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
