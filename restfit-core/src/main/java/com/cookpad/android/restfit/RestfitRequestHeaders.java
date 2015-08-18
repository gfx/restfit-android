package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;

public class RestfitRequestHeaders {

    public static String KEY_USER_AGENT = "User-Agent";

    final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public void setUserAgent(@NonNull String userAgent) {
        headers.put(KEY_USER_AGENT, userAgent);
    }

    public String getUserAgent() {
        return headers.get(KEY_USER_AGENT);
    }

    public Map<String, String> getRawHeaders() {
        return headers;
    }
}
