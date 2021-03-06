package com.cookpad.android.restfit.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static com.cookpad.android.restfit.internal.RestfitUtils.uncheckedCast;

public abstract class RestfitBaseModel implements Parcelable {

    protected static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(Uri.class, new UriTypeAdapter())
            .create();

    public static <T extends RestfitBaseModel> T readFromParcel(@NonNull Parcel source, @NonNull Class<T> klass) {
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + GSON.toJson(this);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        // FIXME it's not the best performance
        if (o != null && this.getClass() == o.getClass()) {
            RestfitBaseModel other = (RestfitBaseModel) o;
            return this.toString().equals(other.toString());
        } else {
            return false;
        }
    }

    public static class EasyCreator<T extends RestfitBaseModel> implements Creator<T> {

        final Class<T> klass;

        public EasyCreator(Class<T> klass) {
            this.klass = klass;
        }


        @Override
        public T createFromParcel(Parcel source) {
            return readFromParcel(source, klass);
        }

        @Override
        public T[] newArray(int size) {
            return uncheckedCast(new Object[size]);
        }
    }
}
