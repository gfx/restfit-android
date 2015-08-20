package com.cookpad.android.restfit;

import com.cookpad.android.restfit.internal.RestfitParcelable;

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.TreeMap;

public class RestfitRequestHeaders extends RestfitParcelable {

    public static String KEY_USER_AGENT = "User-Agent";

    final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public RestfitRequestHeaders() {

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

    // Parcelable


    public static final Creator<RestfitRequestHeaders> CREATOR
            = new Creator<RestfitRequestHeaders>() {

        @Override
        public RestfitRequestHeaders createFromParcel(Parcel source) {
            return RestfitRequestHeaders.readFromParcel(source, null);
        }

        @Override
        public RestfitRequestHeaders[] newArray(int size) {
            return new RestfitRequestHeaders[size];
        }
    };
}
