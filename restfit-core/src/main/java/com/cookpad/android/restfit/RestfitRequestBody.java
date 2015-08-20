package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.os.Parcel;

public class RestfitRequestBody extends RestfitParcelable {

    public RestfitRequestBody() {

    }

    public static final Creator<RestfitRequestBody> CREATOR = new Creator<RestfitRequestBody>() {

        @Override
        public RestfitRequestBody createFromParcel(Parcel in) {
            return RestfitRequestBody.readFromParcel(in, null);
        }

        @Override
        public RestfitRequestBody[] newArray(int size) {
            return new RestfitRequestBody[size];
        }
    };
}
