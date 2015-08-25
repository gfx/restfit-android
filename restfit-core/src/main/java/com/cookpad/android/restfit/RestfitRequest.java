package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.support.annotation.NonNull;

public class RestfitRequest extends RestfitParcelable {

    final String method;

    final String url;

    final RestfitHttpHeaders headers;

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

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitRequestBody body;

        public Builder method(@NonNull String method) {
            this.method = method;
            return this;
        }

        public Builder url(@NonNull String url) {
            this.url = url;
            return this;
        }

        public Builder header(@NonNull String key, @NonNull String value) {
            headers.put(key, value);
            return this;
        }

        @NonNull
        public RestfitRequest build() {
            return new RestfitRequest(this);
        }
    }


    public static final Creator<RestfitRequest> CREATOR = new EasyCreator<>(RestfitRequest.class);
}
