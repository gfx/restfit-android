package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class RestfitQueryStringBuilder {

    static final Pattern SEP_QUERY_STRING = Pattern.compile("[&;]");

    static final Pattern SEP_PAIR = Pattern.compile("=");

    final Map<String, String> params = new LinkedHashMap<>();

    public RestfitQueryStringBuilder(@NonNull String queryString) {
        for (String pair : SEP_QUERY_STRING.split(queryString)) {
            String[] kv = SEP_PAIR.split(pair, 2);
            // to allow non-encoded query strings like "value=こんにちは"
            put(decode(kv[0]), decode(kv[1]));
        }
    }

    public RestfitQueryStringBuilder() {
    }

    @NonNull
    private static String encode(@NonNull String key) {
        return RestfitUtils.encodeUrlComponent(key);
    }

    @NonNull
    private static String decode(@NonNull String key) {
        return RestfitUtils.decodeUrlComponent(key);
    }

    public RestfitQueryStringBuilder putQueryString(@Nullable String queryString) {
        if (!TextUtils.isEmpty(queryString)) {
            this.putAll(new RestfitQueryStringBuilder(queryString));
        }
        return this;
    }

    public RestfitQueryStringBuilder put(@NonNull String key, @NonNull String value) {
        putEncodedValue(key, RestfitUtils.encodeUrlComponent(value));
        return this;
    }

    public RestfitQueryStringBuilder putIfNotEmpty(@NonNull String key, @Nullable String value) {
        if (!TextUtils.isEmpty(value)) {
            put(key, value);
        }
        return this;
    }

    public RestfitQueryStringBuilder putEncodedValue(@NonNull String key, @NonNull String encodedValue) {
        params.put(encode(key), encodedValue);
        return this;
    }

    public RestfitQueryStringBuilder putEncodedPair(@NonNull String encodedKey, @NonNull String encodedValue) {
        params.put(encodedKey, encodedValue);
        return this;
    }

    public RestfitQueryStringBuilder put(@NonNull String key, long value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryStringBuilder put(@NonNull String key, double value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryStringBuilder put(@NonNull String key, boolean value) {
        putEncodedValue(key, String.valueOf(value));
        return this;
    }

    public RestfitQueryStringBuilder put(@NonNull String key, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            params.put(encode(key) + "[" + encode(entry.getKey()) + "]", encode(entry.getValue()));
        }
        return this;
    }

    public RestfitQueryStringBuilder putAll(@NonNull RestfitQueryStringBuilder queryString) {
        params.putAll(queryString.params);
        return this;
    }

    @NonNull
    public Set<String> keySet() {
        return params.keySet();
    }

    @Nullable
    public String get(String key) {
        return params.get(key);
    }

    @NonNull
    public String build() {
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

    @NonNull
    @Override
    public String toString() {
        return build();
    }
}
