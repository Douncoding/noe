package com.douncoding.noe.ui.babys_action.list;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseContract;

public interface BabyListContract {
    interface View extends BaseContract.View {
        // 로딩 다이얼로그 현시
        void showItemLoadingDialog();
        // 로딩 다이얼로그 제거
        void dismissItemLoadingDialog();

        // 추적중인 아이템 현시
        void renderSelectedCount(int count);

        // 목록 현시
        void addBabyList(String key, Baby item);

        void updateItem(String key, boolean isSelected);
    }

    interface Presenter extends BaseContract.Presenter {
        void onItemLoad();

        void onInsertTarget(String key, Baby baby);

        void onRemoveTarget(String key, Baby baby);
    }
}
