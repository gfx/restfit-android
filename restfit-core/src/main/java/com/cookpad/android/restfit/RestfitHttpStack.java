package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import rx.Single;

/**
 * Low-level Http handler interface
 */
public interface RestfitHttpStack {

    @NonNull
    Single<RestfitResponse> perform(@NonNull RestfitRequest request);

}
