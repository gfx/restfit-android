package com.cookpad.android.restfit.internal;

import java.nio.charset.Charset;

public class RestfitUtils {

    public static final String DEFAULT_ENCODING_NAME = "UTF-8";

    public static final Charset DEFAULT_ENCODING = Charset.forName(DEFAULT_ENCODING_NAME);

    @SuppressWarnings("unchecked")
    public static <R, V> R uncheckedCast(V value) {
        return (R) value;
    }
}
