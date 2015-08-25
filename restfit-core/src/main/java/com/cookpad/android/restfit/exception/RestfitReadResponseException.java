package com.cookpad.android.restfit.exception;

import com.cookpad.android.restfit.RestfitRequest;

public class RestfitReadResponseException extends RestfitException {

    public RestfitReadResponseException(RestfitRequest request, Throwable throwable) {
        super(request.toString(), throwable);
    }
}
