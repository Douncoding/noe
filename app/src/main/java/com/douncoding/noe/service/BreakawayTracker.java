package com.douncoding.noe.service;

import android.os.Handler;

import com.orhanobut.logger.Logger;

/**
 * 강의장 이탈(비콘 잃음) 시 처리
 */
public class BreakawayTracker {
    private Handler mHandler;
    private int remaining; // 이탈유효시간

    private boolean isRunning;
    private TrackingTask trackingTask;

    private OnCallback onCallback;
    public interface OnCallback {
        void onExpire();
    }

    /**
     * @param validate 시간(초)
     */
    public BreakawayTracker(int validate, OnCallback onCallback) {
        this.remaining = validate;
        this.mHandler = new Handler();
        this.trackingTask = new TrackingTask();
        this.onCallback = onCallback;
    }

    public void start() {
        Logger.i("지역제한: 이탈감지 테스크 시작: 남은시간:" + remaining);
        isRunning = true;
        mHandler.post(trackingTask);
    }

    public void stop() {
        Logger.i("지역제한: 이탈감지 테스크 정지: 남은시간:" + remaining);
        isRunning = false;
        mHandler.removeCallbacks(trackingTask);
    }

    public int getRemaining() {
        return remaining;
    }

    private class TrackingTask implements Runnable {
        @Override
        public void run() {
            if (!isRunning) return;

            remaining -= 1;

            if (remaining <= 0) {
                if (onCallback != null) onCallback.onExpire();
            } else {
                // 1초단위 재귀
                mHandler.postDelayed(this, 1000);
            }
        }
    }
}
