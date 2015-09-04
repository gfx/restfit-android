package com.cookpad.android.restfit.okhttp;

import com.cookpad.android.restfit.RestfitHttpHeaders;
import com.cookpad.android.restfit.RestfitRequestBody;
import com.squareup.okhttp.RequestBody;

import android.support.annotation.NonNull;

import java.io.IOException;

import okio.BufferedSink;

public class RestfitRequestOkHttpBody implements RestfitRequestBody {

    final RequestBody requestBody;

    public RestfitRequestOkHttpBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @NonNull
    @Override
    public RestfitHttpHeaders headers() {
        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        headers.put(HTTP_CONTENT_TYPE, requestBody.contentType().toString());

        try {
            long contentLength = requestBody.contentLength();
            if (contentLength != -1) {
                headers.put(HTTP_CONTENT_LENGTH, String.valueOf(contentLength));
            }
        } catch (IOException e) {
            // ignore errors
        }

        return headers;
    }

    @Override
    public void writeTo(@NonNull BufferedSink out) throws IOException {
        requestBody.writeTo(out);
    }
}
