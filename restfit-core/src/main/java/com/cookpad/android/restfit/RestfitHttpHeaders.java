package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitBaseModel;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class RestfitHttpHeaders extends RestfitBaseModel implements Iterable<Map.Entry<String, String>> {

    public static String KEY_USER_AGENT = "User-Agent";

    final Map<String, String> rawHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public RestfitHttpHeaders() {

    }

    public void put(@NonNull String key, @NonNull String value) {
        rawHeaders.put(key, value);
    }

    public void putAll(@NonNull Map<String, String> headers) {
        this.rawHeaders.putAll(headers);
    }

    public void putAll(@NonNull RestfitHttpHeaders headers) {
        headers.putAll(headers.rawHeaders);
    }

    public String get(@NonNull String key) {
        return rawHeaders.get(key);
    }

    public boolean contains(@NonNull String key) {
        return rawHeaders.containsKey(key);
    }

    public Map<String, String> getRawHeaders() {
        return rawHeaders;
    }

    public static final Creator<RestfitHttpHeaders> CREATOR = new EasyCreator<>(RestfitHttpHeaders.class);

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return rawHeaders.entrySet().iterator();
    }
}
