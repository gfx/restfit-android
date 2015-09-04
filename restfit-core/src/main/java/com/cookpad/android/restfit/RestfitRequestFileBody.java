package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okio.BufferedSink;

public class RestfitRequestFileBody implements RestfitRequestBody {

    @NonNull
    @Override
    public RestfitHttpHeaders headers() {
        return new RestfitHttpHeaders();
    }

    @Override
    public void writeTo(@NonNull BufferedSink out) throws IOException {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
