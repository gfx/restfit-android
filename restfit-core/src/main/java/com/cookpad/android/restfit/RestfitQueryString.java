package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestfitQueryString {

    final Map<String, String> params = new LinkedHashMap<>();

    public RestfitQueryString() {
    }

    @NonNull
    private String encode(@NonNull String key) {
        return RestfitUtils.encodeUrlComponent(key);
    }

    public RestfitQueryString put(@NonNull String key, @NonNull String value) {
        putEncodedValue(key, RestfitUtils.encodeUrlComponent(value));
        return this;
    }

    public RestfitQueryString putIfNotEmpty(@NonNull String key, @Nullable String value) {
        if (!TextUtils.isEmpty(value)) {
            put(key, value);
        }
        return this;
    }

    public RestfitQueryString putEncodedValue(@NonNull String key, @NonNull String encodedValue) {
        params.put(encode(key), encodedValue);
        return this;
    }

    public RestfitQueryString putEncodedPair(@NonNull String encodedKey, @NonNull String encodedValue) {
        params.put(encodedKey, encodedValue);
        return this;
    }

    public RestfitQueryString put(@NonNull String key, long value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryString put(@NonNull String key, double value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryString put(@NonNull String key, boolean value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryString put(@NonNull String key, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            params.put(encode(key) + "[" + encode(entry.getKey()) + "]", encode(entry.getValue()));
        }
        return this;
    }

    public RestfitQueryString putAll(@NonNull RestfitQueryString queryString) {
        params.putAll(queryString.getMap());
        return this;
    }

    public Map<String, String> getMap() {
        return params;
    }

    @NonNull
    public String buildQueryString() {
        if (params.size() == 0) {
            return "";
        }

        List<String> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey())) {
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return TextUtils.join("&", list);
    }

    @Override
    public String toString() {
        return buildQueryString();
    }
}
