package com.cookpad.android.restfit.okhttp;

import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.restfit.RestfitResponseBody;
import com.cookpad.android.restfit.exception.RestfitRequestException;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okio.BufferedSink;
import rx.Single;
import rx.SingleSubscriber;

public class RestfitOkHttpStack implements RestfitHttpStack {

    final OkHttpClient okHttpClient;

    public RestfitOkHttpStack(@NonNull OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @NonNull
    @Override
    public Single<RestfitResponse> perform(@NonNull final RestfitRequest request) {
        return Single.create(new Single.OnSubscribe<RestfitResponse>() {
            @Override
            public void call(SingleSubscriber<? super RestfitResponse> subscriber) {

                Request.Builder builder = new Request.Builder();

                for (Map.Entry<String, String> header : request.getHeaders()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }

                builder.url(request.getUrl().toString());

                builder.method(request.getMethod(), createRequestBody(request));

                try {
                    okHttpClient.setConnectTimeout(request.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS);
                    okHttpClient.setReadTimeout(request.getReadTimeoutMillis(), TimeUnit.MILLISECONDS);

                    Response response = okHttpClient.newCall(builder.build()).execute();
                    subscriber.onSuccess(new RestfitResponse.Builder()
                            .status(response.code(), response.message())
                            .headers(parseHeaders(response.headers()))
                            .body(parseBody(request, response.body()))
                            .build());
                } catch (IOException e) {
                    subscriber.onError(new RestfitRequestException(request, e));
                }
            }
        });
    }

    @Nullable
    static RequestBody createRequestBody(final RestfitRequest request) {
        if (request.hasBody()) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return MediaType.parse(request.getHeaders().get("content-type"));
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    request.getBody().writeTo(sink);
                }
            };
        } else {
            return null;
        }
    }

    static Map<String, String> parseHeaders(Headers okHttpHeaders) {
        Map<String, String> headers = new TreeMap<>();
        for (String name : okHttpHeaders.names()) {
            headers.put(name, okHttpHeaders.get(name));
        }
        return headers;
    }

    static RestfitResponseBody parseBody(RestfitRequest request, ResponseBody okHttpResponseBody) throws IOException {
        return new RestfitResponseBody(request, okHttpResponseBody.byteStream());
    }
}
