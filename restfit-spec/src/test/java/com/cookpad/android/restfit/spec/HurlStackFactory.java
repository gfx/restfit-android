package com.cookpad.android.restfit.spec;

import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.RestfitHurlStack;

public class HurlStackFactory implements RestfitHttpStackSpec.HttpStackFactory {

    @Override
    public RestfitHttpStack createInstance() {
        return new RestfitHurlStack();
    }
}
