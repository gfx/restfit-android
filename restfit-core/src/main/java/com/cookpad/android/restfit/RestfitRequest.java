package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.support.annotation.NonNull;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class RestfitRequest extends RestfitParcelable {

    final String method;

    final String url;

    final RestfitHttpHeaders headers;

    final RestfitRequestBody body;

    final int connectTimeoutMillis;

    final int readTimeoutMillis;

    RestfitRequest(Builder builder) {
        method = builder.method;
        url = builder.url;
        headers = builder.headers;
        body = builder.body;
        connectTimeoutMillis = builder.connectTimeoutMillis;
        readTimeoutMillis = builder.readTimeoutMillis;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public RestfitHttpHeaders getHeaders() {
        return headers;
    }

    public RestfitRequestBody getBody() {
        return body;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public static class Builder {

        String method;

        String url;

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitRequestBody body;

        int connectTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(10);

        int readTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(30);


        public Builder method(@NonNull String method) {
            this.method = method;
            return this;
        }

        public Builder url(@NonNull URL url) {
            this.url = url.toString();
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

        public Builder connectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder setReadTimeoutMillis(int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        @NonNull
        public RestfitRequest build() {
            return new RestfitRequest(this);
        }
    }


    public static final Creator<RestfitRequest> CREATOR = new EasyCreator<>(RestfitRequest.class);
}
