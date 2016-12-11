package com.douncoding.noe.ui.car;

import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BaseContract;

import java.util.List;

public interface CarEventContract {
    interface View extends BaseContract.View {
        void renderPetEventList(List<TrackEvent> items);
    }

    interface Presenter extends BaseContract.Presenter {
        void onLoadEvent();

        void onManagementAction();
    }
}
