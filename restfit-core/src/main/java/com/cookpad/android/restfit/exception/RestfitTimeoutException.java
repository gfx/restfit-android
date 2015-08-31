package com.cookpad.android.restfit.exception;

import com.cookpad.android.restfit.RestfitRequest;

public class RestfitTimeoutException extends RestfitRequestException {

    public RestfitTimeoutException(RestfitRequest request, Throwable throwable) {
        super(request, throwable);
    }
}
