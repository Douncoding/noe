package com.douncoding.noe.ui.car_action.list;

import com.douncoding.noe.model.Car;
import com.douncoding.noe.model.Pet;
import com.douncoding.noe.ui.BaseContract;

public interface CarListContract {
    interface View extends BaseContract.View {
        // 반려동물 목록 로딩 다이얼로그 현시
        void showItemLoadingDialog();
        // 다이얼로그 제거
        void dismissItemLoadingDialog();
        // 목록 추가
        void addCar(String key, Car item);
    }

    interface Presenter extends BaseContract.Presenter {
        void onItemLoad();
    }
}
