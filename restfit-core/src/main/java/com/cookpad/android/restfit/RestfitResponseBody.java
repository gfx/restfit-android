package com.cookpad.android.restfit;

import com.cookpad.android.restfit.exception.RestfitReadResponseException;
import com.cookpad.android.restfit.internal.RestfitParcelable;
import com.cookpad.android.restfit.internal.RestfitUtils;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import rx.Single;
import rx.SingleSubscriber;

public class RestfitResponseBody extends RestfitParcelable {

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
        RestfitUtils.assertNotOnMainThread();

        if (stringBuffer != null) {
            return stringBuffer;
        } else if (byteBuffer != null) {
            stringBuffer = new String(byteBuffer, RestfitUtils.DEFAULT_ENCODING);
            return stringBuffer;
        }

        char[] buffer = new char[RestfitUtils.BUFFER_SIZE];
        StringBuilder result = new StringBuilder();
        Reader reader = new InputStreamReader(inputStream, RestfitUtils.DEFAULT_ENCODING);

        for (; ; ) {
            try {
                int size = reader.read(buffer);
                if (size <= 0) {
                    break;
                }
                result.append(buffer, 0, size);
            } catch (IOException e) {
                throw new RestfitReadResponseException(request, e);
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            // ignore
        }

        return result.toString();
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
