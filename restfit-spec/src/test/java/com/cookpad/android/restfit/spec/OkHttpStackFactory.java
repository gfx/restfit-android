package com.cookpad.android.restfit.spec;

import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.okhttp.RestfitOkHttpStack;
import com.squareup.okhttp.OkHttpClient;

public class OkHttpStackFactory implements RestfitHttpStackSpec.HttpStackFactory {

    @Override
    public RestfitHttpStack createInstance() {
        return new RestfitOkHttpStack(new OkHttpClient());
    }
}
