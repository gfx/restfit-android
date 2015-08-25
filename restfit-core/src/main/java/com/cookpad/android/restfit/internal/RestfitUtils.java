package com.cookpad.android.restfit.internal;

public class RestfitUtils {

    @SuppressWarnings("unchecked")
    public static <R, V> R uncheckedCast(V value) {
        return (R) value;
    }
}
