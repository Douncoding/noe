package com.douncoding.noe.ui.pet;

import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BaseContract;

import java.util.List;

/**
 * Created by douncoding on 2016. 12. 3..
 */

public interface PetContract {
    interface View extends BaseContract.View {
        void renderPetEventList(List<TrackEvent> items);
    }

    interface Presenter extends BaseContract.Presenter {
        void onLoad();

        // 반려동물 관리 버튼 클릭
        void onManagementAction();
    }
}
