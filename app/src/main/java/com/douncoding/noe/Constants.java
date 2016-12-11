package com.douncoding.noe;

import android.*;

/**
 * Created by douncoding on 2016. 12. 2..
 */

public final class Constants {

    /**
     * Permission Group
     */
    public static final int RC_INDISPENSABLE_PERMISSION_GROUP = 101;
    public static final int RC_CAMERA_PERMISSIONS             = 102;
    public static final String[] CARMERA_PARAMS = new String[] {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int TC_PICK_IMAGE = 202;

    // 서버에 등록되는 이미지의 크기 (FULL SIZE)
    public static final int FULL_SIZE_MAX_DIMENSION = 1280;

    /**
     * {@link com.douncoding.noe.service.BeaconService}의 브로드캐스트
     */
    public static final String ACTION_BEACON_SERVICE_START = "action.service.beacon.start";
    public static final String ACTION_BEACON_SERVICE_STOP = "action.service.beacon.stop";


    public static final String DAUM_MAP_KEY = "a4ee99c749995f3f6e57cb1e24687d09";
}
