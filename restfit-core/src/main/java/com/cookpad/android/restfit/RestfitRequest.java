package com.cookpad.android.restfit;

import com.cookpad.android.restfit.exception.RestfitRuntimeException;
import com.cookpad.android.restfit.internal.RestfitBaseModel;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

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

        if (body != null) {
            headers.putAll(body.headers());
        }
    }

    static Uri buildUrl(Uri url, RestfitQueryStringBuilder queryString) {
        RestfitQueryStringBuilder q = new RestfitQueryStringBuilder();
        q.putQueryString(url.getQuery());
        q.putAll(queryString);
        return url.buildUpon()
                .encodedQuery(q.build())
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

    public boolean hasBody() {
        return body != null;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void writeBodyTo(OutputStream out) throws IOException {
        body.writeTo(out);
    }

    public static class Builder {

        String method;

        Uri url;

        RestfitQueryStringBuilder queryString = new RestfitQueryStringBuilder();

        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        RestfitRequestBody body;

        int connectTimeoutMillis;

        int readTimeoutMillis;

        /**
         * Creates a builder via {@link RestfitClient#requestBuilder()}.
         */
        /* package */ Builder() {

        }


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

        public Builder path(@NonNull String path) {
            this.url = url.buildUpon()
                    .path(path)
                    .build();
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

        public Builder headers(@NonNull RestfitHttpHeaders headers) {
            headers.putAll(headers);
            return this;
        }

        public Builder body(@NonNull RestfitRequestBody body) {
            if (this.body != null) {
                throw new RestfitRuntimeException("A request body is already set");
            }
            this.body = body;
            return this;
        }

        public Builder connectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder readTimeoutMillis(int readTimeoutMillis) {
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
