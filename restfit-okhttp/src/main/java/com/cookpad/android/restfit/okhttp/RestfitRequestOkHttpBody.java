package com.cookpad.android.restfit.okhttp;

import com.cookpad.android.restfit.RestfitHttpHeaders;
import com.cookpad.android.restfit.RestfitRequestBody;
import com.squareup.okhttp.RequestBody;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

import okio.BufferedSink;
import okio.Okio;

public class RestfitRequestOkHttpBody implements RestfitRequestBody {

    final RequestBody requestBody;

    public RestfitRequestOkHttpBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @NonNull
    @Override
    public RestfitHttpHeaders headers() throws IOException {
        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        headers.put(HTTP_CONTENT_TYPE, requestBody.contentType().toString());

        long contentLength = requestBody.contentLength();
        if (contentLength != -1) {
            headers.put(HTTP_CONTENT_LENGTH, String.valueOf(contentLength));
        }

        return headers;
    }

    @Override
    public void writeTo(@NonNull OutputStream out) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(out));
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }
}
