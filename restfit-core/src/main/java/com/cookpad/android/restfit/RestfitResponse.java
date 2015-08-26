package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.support.annotation.NonNull;

import java.util.Map;

public class RestfitResponse extends RestfitParcelable {

    final RestfitRequest request;

    final int statusCode;

    final String statusMessage;

    final RestfitHttpHeaders headers;

    final RestfitResponseBody body;

    public RestfitResponse(Builder builder) {
        request = builder.request;
        statusCode = builder.statusCode;
        statusMessage = builder.statusMessage;
        headers = builder.headers;
        body = builder.body;
    }

    public static class Builder {

        RestfitRequest request;

        int statusCode;

        String statusMessage;

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitResponseBody body;

        public Builder request(@NonNull RestfitRequest request) {
            this.request = request;
            return this;
        }

        public Builder status(int statusCode, String statusMessage) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
            return this;
        }

        public Builder header(@NonNull String key, @NonNull String value) {
            headers.put(key, value);
            return this;
        }

        public Builder headers(@NonNull Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder body(@NonNull RestfitResponseBody body) {
            this.body = body;
            return this;
        }

        @NonNull
        public RestfitResponse build() {
            return new RestfitResponse(this);
        }
    }


    public static final Creator<RestfitHttpHeaders> CREATOR = new EasyCreator<>(RestfitHttpHeaders.class);
}
