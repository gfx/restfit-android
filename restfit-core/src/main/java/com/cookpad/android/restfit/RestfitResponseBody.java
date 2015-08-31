package com.cookpad.android.restfit;

import com.cookpad.android.restfit.exception.RestfitReadResponseException;
import com.cookpad.android.restfit.internal.RestfitBaseModel;
import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitResponseBody extends RestfitBaseModel {

    final RestfitRequest request;

    final InputStream inputStream;

    String stringBuffer = null;

    byte[] byteBuffer = null;

    public RestfitResponseBody(@NonNull RestfitRequest request, @NonNull InputStream inputStream) {
        this.request = request;
        this.inputStream = inputStream;
    }

    @NonNull
    public InputStream getInputStream() {
        return inputStream;
    }

    @NonNull
    public synchronized String getAsStringSync() throws RestfitReadResponseException {
        if (BuildConfig.DEBUG) {
            RestfitUtils.assertNotOnMainThread();
        }

        if (stringBuffer != null) {
            return stringBuffer;
        } else if (byteBuffer != null) {
            stringBuffer = new String(byteBuffer, RestfitUtils.DEFAULT_ENCODING);
            return stringBuffer;
        }

        String result;

        try {
            result = RestfitUtils.slurpAsString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RestfitReadResponseException(request, e);
        }

        return result;
    }


    @NonNull
    public Single<String> getAsString() {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                try {
                    singleSubscriber.onSuccess(getAsStringSync());
                } catch (RestfitReadResponseException e) {
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    public static Creator<RestfitResponseBody> CREATOR = new EasyCreator<>(RestfitResponseBody.class);
}
