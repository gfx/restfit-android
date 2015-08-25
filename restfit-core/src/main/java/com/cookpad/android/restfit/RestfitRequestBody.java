package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

public class RestfitRequestBody extends RestfitParcelable {

    public RestfitRequestBody() {

    }

    public static final Creator<RestfitRequestBody> CREATOR = new EasyCreator<>(RestfitRequestBody.class);
}
