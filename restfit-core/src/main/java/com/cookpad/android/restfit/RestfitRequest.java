package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitBaseModel;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class RestfitRequest extends RestfitBaseModel {

    final String method;

    final Uri url;

    final RestfitHttpHeaders headers;

    final RestfitRequestBody body;

    final int connectTimeoutMillis;

    final int readTimeoutMillis;

    RestfitRequest(Builder builder) {
        method = builder.method;
        url = buildUrl(builder.url, builder.queryString);
        headers = builder.headers;
        body = builder.body;
        connectTimeoutMillis = builder.connectTimeoutMillis;
        readTimeoutMillis = builder.readTimeoutMillis;
    }

    static Uri buildUrl(Uri url, RestfitQueryStringBuilder queryString) {
        for (String key : url.getQueryParameterNames()) {
            queryString.put(key, url.getQueryParameter(key));
        }
        return url.buildUpon()
                .encodedQuery(queryString.build())
                .build();
    }

    public String getMethod() {
        return method;
    }

    public Uri getUrl() {
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

        Uri url;

        RestfitQueryStringBuilder queryString = new RestfitQueryStringBuilder();

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitRequestBody body;

        int connectTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(10);

        int readTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(30);


        public Builder method(@NonNull String method) {
            this.method = method;
            return this;
        }

        public Builder url(@NonNull URL url) {
            this.url = Uri.parse(url.toString());
            return this;
        }

        public Builder url(@NonNull String url) {
            this.url = Uri.parse(url);
            return this;
        }

        public Builder url(@NonNull Uri url) {
            this.url = url;
            return this;
        }

        public Builder queryString(@NonNull RestfitQueryStringBuilder queryString) {
            this.queryString = queryString;
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
