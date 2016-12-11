package com.douncoding.noe.ui.baby;

import com.douncoding.noe.model.Baby;
import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BasePresenter;
import com.douncoding.noe.ui.pet.PetContract;

import java.util.ArrayList;
import java.util.List;


public class BabyPresenter extends BasePresenter<BabyContract.View> implements BabyContract.Presenter {

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
    public void onLoad() {
        List<TrackEvent> results = new ArrayList<>();

        TrackEvent event = new TrackEvent();
        event.setType(TrackEvent.Type.NORMAL);
        event.setTitle("TITLE#1");
        event.setDescription("DESCRIPTION#1");
        event.setFacePictureUrl(null);
        event.setLatitude(0);
        event.setLongitude(0);
        event.setTimestamp(System.currentTimeMillis());
        results.add(event);

        TrackEvent event2 = new TrackEvent();
        event2.setType(TrackEvent.Type.NOTICE);
        event2.setTitle("TITLE#2");
        event2.setDescription("DESCRIPTION#2");
        event2.setFacePictureUrl(null);
        event2.setLatitude(0);
        event2.setLongitude(0);
        event2.setTimestamp(System.currentTimeMillis());
        results.add(event2);

        TrackEvent event4 = new TrackEvent();
        event4.setType(TrackEvent.Type.WARNNING);
        event4.setTitle("TITLE#2");
        event4.setDescription("DESCRIPTION#2");
        event4.setFacePictureUrl(null);
        event4.setLatitude(0);
        event4.setLongitude(0);
        event4.setTimestamp(System.currentTimeMillis());
        results.add(event4);

        TrackEvent event3 = new TrackEvent();
        event3.setType(TrackEvent.Type.BREAKAWAY);
        event3.setTitle("TITLE#3");
        event3.setDescription("DESCRIPTION#3");
        event3.setFacePictureUrl(null);
        event3.setLatitude(0);
        event3.setLongitude(0);
        event3.setTimestamp(System.currentTimeMillis());
        results.add(event3);

        mView.renderPetEventList(results);
    }

    @Override
    public void onManagementAction() {

    }
}
