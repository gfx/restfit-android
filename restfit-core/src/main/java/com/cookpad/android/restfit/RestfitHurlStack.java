package com.cookpad.android.restfit;

import com.cookpad.android.restfit.exception.RestfitRequestException;
import com.cookpad.android.restfit.exception.RestfitRuntimeException;

import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okio.BufferedSink;
import okio.Okio;
import rx.Single;
import rx.SingleSubscriber;

/**
 * A {@link RestfitHttpStack} implementation that uses {@link java.net.HttpURLConnection}.
 */
public class RestfitHurlStack implements RestfitHttpStack {

    @NonNull
    @Override
    public Single<RestfitResponse> perform(@NonNull final RestfitRequest request) {
        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(SingleSubscriber<? super RestfitResponse> subscriber) {
                HttpURLConnection conn;
                try {
                    conn = connect(request);

                    // setup a connection
                    conn.setRequestMethod(request.getMethod());
                    for (Map.Entry<String, String> header : request.getHeaders()) {
                        conn.setRequestProperty(header.getKey(), header.getValue());
                    }
                    conn.setConnectTimeout(request.getConnectTimeoutMillis());
                    conn.setReadTimeout(request.getReadTimeoutMillis());

                    if (request.hasBody()) {
                        conn.setDoOutput(true);
                        OutputStream out = conn.getOutputStream();
                        BufferedSink sink = Okio.buffer(Okio.sink(out));
                        request.writeBodyTo(sink);
                        out.close();
                    }

                    // wait for a response
                    int statusCode;
                    InputStream in;
                    try {
                        statusCode = conn.getResponseCode();
                        in = statusCode < 400 ? conn.getInputStream() : conn.getErrorStream();
                    } catch (FileNotFoundException e) {
                        statusCode = 404;
                        in = conn.getErrorStream();
                    }
                    if (statusCode == -1) {
                        subscriber.onError(new RestfitRequestException(request,
                                new IOException("Could not receive response code from HttpUrlConnection")));
                    }
                    String statusMessage = conn.getResponseMessage();

                    RestfitResponse response = new RestfitResponse.Builder()
                            .status(statusCode, statusMessage)
                            .headers(extractLastItems(conn.getHeaderFields()))
                            .body(new RestfitResponseBody(request, in))
                            .build();
                    subscriber.onSuccess(response);
                } catch (IOException e) {
                    subscriber.onError(new RestfitRequestException(request, e));
                }
            }
        });
    }

    HttpURLConnection connect(RestfitRequest request) throws IOException {
        return (HttpURLConnection) new URL(request.url.toString()).openConnection();
    }

    Map<String, String> extractLastItems(Map<String, List<String>> raw) {
        if (raw == null) {
            throw new RestfitRuntimeException("Headers must not be null");
        }
        Map<String, String> extracted = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : raw.entrySet()) {
            String key = entry.getKey();
            if (key == null) {
                // key is null for the status line. e.g. ["HTTP/1.1 200 OK"]
                continue;
            }
            List<String> values = entry.getValue();
            extracted.put(key, values.get(values.size() - 1));
        }

        return extracted;
    }
}
