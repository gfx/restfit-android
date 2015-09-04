package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okio.BufferedSink;

public interface RestfitRequestBody {

    String HTTP_CONTENT_TYPE = "Content-Type";

    String HTTP_CONTENT_LENGTH = "Content-Length";

    /**
     * Returns HTTP headers that includes <code>Content-Type</code>, <code>Content-Length</code>.
     */
    @NonNull
    RestfitHttpHeaders headers();

    /**
     * Writes the content body to {@param out}.
     */
    void writeTo(@NonNull BufferedSink out) throws IOException;
}
