package com.douncoding.noe.ui.car;

import com.douncoding.noe.model.TrackEvent;
import com.douncoding.noe.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by douncoding on 2016. 12. 10..
 */

public class CarEventPresenter extends BasePresenter<CarEventContract.View> implements CarEventContract.Presenter {

    @Override
    public void onResumeFromContext() {

    }

    @Override
    public void onPauseFromContext() {

    }

    @Override
    public void onDestroyFromContext() {

    }

    /**
     * 자녀, 반려동물과 다르게 차량이벤트는 별도의 수신기로 부터 전송되는 이벤트이기 때문에 서버에서 조회한다.
     * 따라서 Firebase Database 에 리스너를 등록한다는 것과 Realm Database 에서 리스너를 등록하던 다른 클래스와 차이점이
     * 있다.
     */
    @Override
    public void onLoadEvent() {
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
