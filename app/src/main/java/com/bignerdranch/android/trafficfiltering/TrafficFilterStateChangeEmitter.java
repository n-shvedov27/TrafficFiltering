package com.bignerdranch.android.trafficfiltering;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class TrafficFilterStateChangeEmitter implements ObservableOnSubscribe<Integer> {
    private static int VPN_UNABLE = 0;
    private static int VPN_DISABLED = 1;
    private static int NO_INTERNET = 0;

    public int getCurrentStatus() {
        return currentStatus;
    }

    private int currentStatus = VPN_DISABLED;

    private ObservableEmitter<Integer> emitter;
    private static TrafficFilterStateChangeEmitter instance;

    synchronized public void putStatus(Integer newStatus) {
        if (currentStatus != newStatus) {
            currentStatus = newStatus;
            emitter.onNext(currentStatus);
        }
    }

    private TrafficFilterStateChangeEmitter() {

    }

    public static TrafficFilterStateChangeEmitter getInstance() {
        if (instance == null) {
            instance = new TrafficFilterStateChangeEmitter();
        }

        return instance;
    }

    @Override
    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
        this.emitter = emitter;
    }
}
