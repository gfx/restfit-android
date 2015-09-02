package com.cookpad.android.restfit.exception;

import com.cookpad.android.restfit.RestfitRequest;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class RestfitRequestException extends RestfitException {

    final RestfitRequest request;

    public RestfitRequestException(RestfitRequest request, Throwable throwable) {
        super(request.toString(), throwable);

        this.request = request;
    }

    public RestfitRequest getRequest() {
        return request;
    }

    public boolean isTimeout() {
        return getCause() instanceof SocketTimeoutException;
    }

    public boolean isUnknownHost() {
        return getCause() instanceof UnknownHostException;
    }
}
