package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitUtils;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitClient {


    static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(2);

    static final int DEFAULT_READ_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(10);


    final boolean debug;

    final Uri endpoint;

    final RestfitHttpHeaders headers;

    final RestfitHttpStack httpStack;

    final ExecutorService executorService;

    final int connectTimeoutMillis;

    final int readTimeoutMillis;

    RestfitClient(Builder builder) {
        debug = builder.debug;
        endpoint = builder.endpoint;
        headers = builder.requestHeaders;
        httpStack = builder.httpStack;
        executorService = builder.executorService;
        connectTimeoutMillis = builder.connectTimeoutMillis;
        readTimeoutMillis = builder.readTimeoutMillis;
    }

    @NonNull
    public String getUserAgent() {
        return headers.get(RestfitHttpHeaders.KEY_USER_AGENT);
    }

    @NonNull
    public Single<RestfitResponse> call(@NonNull final RestfitRequest request) {

        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(final SingleSubscriber<? super RestfitResponse> subscriber) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        httpStack.perform(request).subscribe(subscriber);
                    }
                });
            }
        });
    }

    @NonNull
    public RestfitRequest.Builder requestBuilder() {
        RestfitRequest.Builder b = new RestfitRequest.Builder(this)
                .connectTimeoutMillis(connectTimeoutMillis)
                .readTimeoutMillis(readTimeoutMillis)
                .headers(headers);

        if (endpoint != null) {
            b.url(endpoint);
        }

        return b;
    }

    public static class Builder {

        boolean debug = false;

        Uri endpoint;

        RestfitHttpHeaders requestHeaders = new RestfitHttpHeaders();

        RestfitHttpStack httpStack;

        ExecutorService executorService;

        int connectTimeoutMillis = DEFAULT_CONNECT_TIMEOUT_MILLIS;

        int readTimeoutMillis = DEFAULT_READ_TIMEOUT_MILLIS;

        public Builder endpoint(@NonNull String endpoint) {
            return endpoint(Uri.parse(endpoint));
        }

        public Builder endpoint(@NonNull URL endpoint) {
            return endpoint(Uri.parse(endpoint.toString()));
        }

        public Builder endpoint(@NonNull Uri endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder userAgent(@NonNull String userAgent) {
            this.requestHeaders.put(RestfitHttpHeaders.KEY_USER_AGENT, userAgent);
            return this;
        }

        public Builder httpStack(@NonNull RestfitHttpStack httpStack) {
            this.httpStack = httpStack;
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setConnectTimeout(long time, TimeUnit unit) {
            assert time >= 0;
            long millis = unit.toMillis(time);
            assert millis < Integer.MAX_VALUE;

            this.connectTimeoutMillis = (int) millis;

            return this;
        }

        public Builder setReadTimeout(long time, TimeUnit unit) {
            assert time >= 0;
            long millis = unit.toMillis(time);
            assert millis < Integer.MAX_VALUE;

            this.readTimeoutMillis = (int) millis;

            return this;
        }

        @NonNull
        public RestfitClient build() {
            validate();

            return new RestfitClient(this);
        }

        void validate() {
            if (!requestHeaders.contains(RestfitHttpHeaders.KEY_USER_AGENT)) {
                throw new IllegalArgumentException("No userAgent specified");
            }
            if (httpStack == null) {
                throw new IllegalArgumentException("No httpStack specified");
            }

            if (executorService == null) {
                executorService = RestfitUtils.createDefaultThreadPoolExecutor();
            }
        }
    }
}
