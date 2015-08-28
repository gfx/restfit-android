package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitClient {

    final boolean debug;

    final RestfitHttpHeaders headers;

    final RestfitHttpHandler httpHandler;

    final ExecutorService executorService;

    RestfitClient(Builder builder) {
        headers = builder.requestHeaders;
        httpHandler = builder.httpHandler;
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
                        httpHandler.perform(request).subscribe(subscriber);
                    }
                });
            }
        });
    }

    public static class Builder {

        boolean debug = false;

        RestfitHttpHeaders requestHeaders = new RestfitHttpHeaders();

        RestfitHttpHandler httpHandler;

        ExecutorService executorService;

        public Builder userAgent(@NonNull String userAgent) {
            this.requestHeaders.put(RestfitHttpHeaders.KEY_USER_AGENT, userAgent);
            return this;
        }

        public Builder httpHandler(@NonNull RestfitHttpHandler httpHandler) {
            this.httpHandler = httpHandler;
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
            if (httpHandler == null) {
                throw new IllegalArgumentException("No httpHandler specified");
            }

            if (executorService == null) {
                executorService = RestfitUtils.createDefaultThreadPoolExecutor();
            }
        }
    }
}
