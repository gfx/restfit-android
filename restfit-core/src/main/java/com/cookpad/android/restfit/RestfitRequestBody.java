package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitBaseModel;

public class RestfitRequestBody extends RestfitBaseModel {

    public RestfitRequestBody() {

    }

    public static final Creator<RestfitRequestBody> CREATOR = new EasyCreator<>(RestfitRequestBody.class);
}
