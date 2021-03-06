package com.douncoding.noe.ui.pay_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Pay;
import com.douncoding.noe.ui.BaseContract;

import java.util.List;

public interface PayListContract {

    interface View extends BaseContract.View {
        // 로딩 다이얼로그 현시
        void showItemLoadingDialog();
        // 로딩 다이얼로그 제거
        void dismissItemLoadingDialog();

        // 결제수단 아이템 렌더링
        void renderPaymentList(List<Pay> items);
    }

    interface Presenter extends BaseContract.Presenter {
        // 아이템 목록 요청
        void onItemLoad();
        // 아이템 삭제 요청
        void onRemove(Pay pay);
    }
}
