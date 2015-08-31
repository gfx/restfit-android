package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import rx.Single;

public interface RestfitHttpStack {

    @NonNull
    Single<RestfitResponse> perform(@NonNull RestfitRequest request);

}
