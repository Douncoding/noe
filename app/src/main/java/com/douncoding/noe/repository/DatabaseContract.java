package com.douncoding.noe.repository;

import android.provider.BaseColumns;

/**
 * 데이터베이스 스키마 정의 클래스
 */

public class DatabaseContract {
    public static final int DATABASE_VERSION             = 3;
    public static final String DATABASE_NAME             = "NOE-Database";

    private static final String TEXT_TYPE                = "TEXT";
    private static final String PRIMARY_KEY_TYPE         = "INTEGER PRIMARY KEY";
    private static final String TEXT_PRIMARY_KEY_TYPE    = "TEXT PRIMARY KEY";
    private static final String AUTO_PRIMARY_KEY_TYPE    = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String INTEGER_TYPE             = "INTEGER";

    /**
     * 추적대상 기록
     * KEY: Firebase Item Key
     * FIELD1: Instance to JSON
     * FIELD2: Tracking Target Type (Pet, Baby)
     * - 차는 비콘이 차에 실장되는 형태기 때문에 수신기로 부터 알람을 받는 형태로 제공된다.
     */
    public static class TrackingTarget implements BaseColumns {
        public static final String TABLE_NAME = "TABLE_TRACKING_TARGET";
        public static final String KEY_ID = "KEY";
        public static final String FIELD1 = "DATA";
        public static final String FIELD2 = "TYPE";

        public static final String CREATE_TABLE_SQL = "CREATE TABLE " +
                TABLE_NAME + " " +
                "(" +
                KEY_ID + " " + TEXT_PRIMARY_KEY_TYPE + ", " +
                FIELD1 + " " + TEXT_TYPE + ", " +
                FIELD2 + " " + TEXT_TYPE +
                ")";
    }
}
