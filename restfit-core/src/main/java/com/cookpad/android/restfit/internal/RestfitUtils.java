package com.cookpad.android.restfit.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestfitUtils {

    public static final int BUFFER_SIZE = 4096;

    public static final String DEFAULT_ENCODING_NAME = "UTF-8";

    public static final Charset DEFAULT_ENCODING = Charset.forName(DEFAULT_ENCODING_NAME);

    @SuppressWarnings("unchecked")
    public static <R, V> R uncheckedCast(V value) {
        return (R) value;
    }

    public static void assertNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AssertionError("It must not on the main thread!");
        }
    }

    @NonNull
    public static String encodeUrlComponent(@Nullable String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        try {
            return URLEncoder.encode(s, DEFAULT_ENCODING_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("never reached", e);
        }
    }

    @NonNull
    public static ExecutorService createDefaultThreadPoolExecutor() {
        return Executors.newFixedThreadPool(4);
    }
}
