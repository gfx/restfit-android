package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;

public class RestfitHttpHeaders extends RestfitParcelable {

    public static String KEY_USER_AGENT = "User-Agent";

    final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public RestfitHttpHeaders() {

    }

    public void put(@NonNull String key, @NonNull String value) {
        headers.put(key, value);
    }

    public String get(@NonNull String key) {
        return headers.get(key);
    }

    public boolean contains(@NonNull String key) {
        return headers.containsKey(key);
    }

    public void setUserAgent(@NonNull String userAgent) {
        headers.put(KEY_USER_AGENT, userAgent);
    }

    public String getUserAgent() {
        return headers.get(KEY_USER_AGENT);
    }

    public Map<String, String> getRawHeaders() {
        return headers;
    }

    public static final Creator<RestfitHttpHeaders> CREATOR = new EasyCreator<>(RestfitHttpHeaders.class);
}
