package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitClient {

    final boolean debug;

    final RestfitRequestHeaders headers = new RestfitRequestHeaders();

    final RestfitHttpHandler httpHandler;

    final ExecutorService executorService;

    RestfitClient(Builder builder) {
        headers.setUserAgent(builder.userAgent);
        httpHandler = builder.httpHandler;
        executorService = builder.executorService;
        debug = builder.debug;
    }

    @NonNull
    public String getUserAgent() {
        return headers.getUserAgent();
    }

    @NonNull
    public Single<RestfitResponse> call(@NonNull final RestfitRequest request) {

        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(final SingleSubscriber<? super RestfitResponse> subscriber) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        httpHandler.execute(request).subscribe(subscriber);
                    }
                });
            }
        });
    }

    public static class Builder {

        boolean debug = false;

        RestfitHttpHandler httpHandler;

        String userAgent;

        ExecutorService executorService;

        public Builder userAgent(@NonNull String userAgent) {
            this.userAgent = userAgent;
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
            if (userAgent == null) {
                throw new IllegalArgumentException("No userAgent specified");
            }
            if (httpHandler == null) {
                throw new IllegalArgumentException("No httpHandler specified");
            }

            if (executorService == null) {
                executorService = RestfitExecutors.createDefaultThreadPoolExecutor();
            }
        }
    }
}
