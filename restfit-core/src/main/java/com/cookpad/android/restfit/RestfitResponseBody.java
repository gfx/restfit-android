package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

public class RestfitResponseBody extends RestfitParcelable {

    public RestfitResponseBody() {
    }

    public static Creator<RestfitResponseBody> CREATOR = new EasyCreator<>(RestfitResponseBody.class);
}
