package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.os.Parcel;

public class RestfitRequest extends RestfitParcelable {

    final String method;

    final String url;

    final RestfitRequestHeaders headers;

    final RestfitRequestBody body;

    RestfitRequest(Builder builder) {
        method = builder.method;
        url = builder.url;
        headers = builder.headers;
        body = builder.body;
    }

    public static class Builder {

        String method;

        String url;

        RestfitRequestHeaders headers = new RestfitRequestHeaders();

        RestfitRequestBody body;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        Builder header(String key, String value) {
            return this;
        }

        public RestfitRequest build() {
            return new RestfitRequest(this);
        }
    }

    // parcebale

    public static final Creator<RestfitRequest> CREATOR = new Creator<RestfitRequest>() {

        @Override
        public RestfitRequest createFromParcel(Parcel source) {
            return RestfitRequest.readFromParcel(source, null);
        }

        @Override
        public RestfitRequest[] newArray(int size) {
            return new RestfitRequest[size];
        }
    };
}
