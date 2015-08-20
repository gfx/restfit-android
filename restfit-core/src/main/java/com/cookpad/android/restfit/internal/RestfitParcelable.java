package com.cookpad.android.restfit.internal;

import com.google.gson.Gson;

import com.cookpad.android.restfit.RestfitRuntimeException;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class RestfitParcelable implements Parcelable {

    protected static final Gson GSON = new Gson();

    public static <T extends RestfitParcelable> T readFromParcel(Parcel source,
            ClassLoader loader) {
        String className = source.readString();
        Class<T> klass;
        try {
            klass = uncheckedCast(Class.forName(className, true, loader));
        } catch (ClassNotFoundException e) {
            throw new RestfitRuntimeException("Failed to load class: " + className, e);
        }
        String json = source.readString();
        return GSON.fromJson(json, klass);
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeString(GSON.toJson(this));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    private static <R, V> R uncheckedCast(V value) {
        return (R) value;
    }
}
