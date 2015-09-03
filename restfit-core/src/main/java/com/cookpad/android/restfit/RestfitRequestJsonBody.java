package com.cookpad.android.restfit;

import com.google.gson.JsonElement;

import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class RestfitRequestJsonBody implements RestfitRequestBody {

    static String CONTENT_TYPE_VALUE = "application/json";

    byte[] content;

    public RestfitRequestJsonBody(JsonElement json) {
        content = json.toString().getBytes(RestfitUtils.DEFAULT_ENCODING);
    }

    @NonNull
    @Override
    public RestfitHttpHeaders headers() {
        RestfitHttpHeaders headers = new RestfitHttpHeaders();

        headers.put(HTTP_CONTENT_TYPE, CONTENT_TYPE_VALUE);
        headers.put(HTTP_CONTENT_LENGTH, String.valueOf(content.length));

        return headers;
    }

    @Override
    public void writeTo(@NonNull OutputStream out) throws IOException {
        out.write(content);
    }
}
