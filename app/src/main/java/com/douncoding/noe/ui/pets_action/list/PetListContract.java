package com.douncoding.noe.ui.pets_action.list;

import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseContract;

import java.util.List;

public interface PetListContract {
    interface View extends BaseContract.View {
        // 반려동물 목록 로딩 다이얼로그 현시
        void showItemLoadingDialog();
        // 다이얼로그 제거
        void dismissItemLoadingDialog();

        // 추적중인 Pet 의 개수 현시
        void renderSelectedPetCount(int count);

        // Pet 목록 현시
        void addPetList(String key, Pet item);

        void updateItem(String key, boolean isSelected);
    }

    interface Presenter extends BaseContract.Presenter {
        void onItemLoad();

        void onInsertTarget(String key, Pet pet);

        void onRemoveTarget(String key, Pet pet);
    }
}
