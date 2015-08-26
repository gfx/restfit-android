package com.cookpad.android.restfit;

import com.cookpad.android.restfit.exception.RestfitRequestException;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Single;
import rx.SingleSubscriber;

/**
 * A {@link RestfitHttpHandler} implementation taht uses {@link java.net.HttpURLConnection}.
 */
public class RestfitHurlHandler implements RestfitHttpHandler {

    @NonNull
    @Override
    public Single<RestfitResponse> perform(@NonNull final RestfitRequest request) {
        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(SingleSubscriber<? super RestfitResponse> subscriber) {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(request.url).openConnection();

                    // create a request

                    conn.setRequestMethod(request.getMethod());
                    for (Map.Entry<String, String> header : request.getHeaders()) {
                        conn.setRequestProperty(header.getKey(), header.getValue());
                    }
                    conn.setConnectTimeout(request.getConnectTimeoutMillis());
                    conn.setReadTimeout(request.getReadTimeoutMillis());

                    // wait for a response

                    int statusCode = conn.getResponseCode();
                    if (statusCode == -1) {
                        subscriber.onError(new RestfitRequestException(request,
                                new IOException("Could not receive response code from HttpUrlConnection")));
                    }
                    String statusMessage = conn.getResponseMessage();

                    RestfitResponse response = new RestfitResponse.Builder()
                            .status(statusCode, statusMessage)
                            .headers(extractLastItems(conn.getHeaderFields()))
                            .body(new RestfitResponseBody(request, conn.getInputStream()))
                            .build();
                    subscriber.onSuccess(response);

                } catch (IOException e) {
                    subscriber.onError(new RestfitRequestException(request, e));
                }
            }
        });
    }

    Map<String, String> extractLastItems(Map<String, List<String>> raw) {
        Map<String, String> extracted = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : raw.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            extracted.put(key, values.get(values.size() - 1));
        }

        return extracted;
    }
}
