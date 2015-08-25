package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.support.annotation.NonNull;

public class RestfitResponse extends RestfitParcelable {

    final RestfitRequest request;

    final int status;

    final RestfitHttpHeaders headers;

    final RestfitResponseBody body;

    public RestfitResponse(Builder builder) {
        request = builder.request;
        status = builder.status;
        headers = builder.headers;
        body = builder.body;
    }

    public static class Builder {

        RestfitRequest request;

        int status;

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitResponseBody body;

        public Builder request(@NonNull RestfitRequest request) {
            this.request = request;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder header(@NonNull String key, @NonNull String value) {
            headers.put(key, value);
            return this;
        }

        @NonNull
        public RestfitResponse build() {
            return new RestfitResponse(this);
        }
    }


    public static final Creator<RestfitHttpHeaders> CREATOR = new EasyCreator<>(RestfitHttpHeaders.class);
}
