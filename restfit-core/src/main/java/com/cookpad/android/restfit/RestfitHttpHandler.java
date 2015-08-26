package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import rx.Single;

public interface RestfitHttpHandler {

    @NonNull
    Single<RestfitResponse> perform(@NonNull RestfitRequest request);

}
