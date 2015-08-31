package com.cookpad.android.restfit.okhttp;

import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;

import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitOkHttpStack implements RestfitHttpStack {

    @NonNull
    @Override
    public Single<RestfitResponse> perform(@NonNull RestfitRequest request) {
        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(SingleSubscriber<? super RestfitResponse> subscriber) {
                // TODO
            }
        });
    }
}
