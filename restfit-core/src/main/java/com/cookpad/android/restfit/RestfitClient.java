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

    final boolean debug;

    final Uri endpoint;

    final RestfitHttpHeaders headers;

    final RestfitHttpStack httpStack;

    final ExecutorService executorService;

    final int defaultConnectTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(2);

    final int defaultReadTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(10);

    RestfitClient(Builder builder) {
        endpoint = builder.endpoint;
        headers = builder.requestHeaders;
        httpStack = builder.httpStack;
        executorService = builder.executorService;
        debug = builder.debug;
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
                .connectTimeoutMillis(defaultConnectTimeoutMillis)
                .readTimeoutMillis(defaultReadTimeoutMillis)
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
