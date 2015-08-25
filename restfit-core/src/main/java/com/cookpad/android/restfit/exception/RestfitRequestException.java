package com.cookpad.android.restfit.exception;

import com.cookpad.android.restfit.RestfitRequest;

public class RestfitRequestException extends RestfitException {

    public RestfitRequestException(RestfitRequest request, Throwable throwable) {
        super(request.toString(), throwable);
    }
}
