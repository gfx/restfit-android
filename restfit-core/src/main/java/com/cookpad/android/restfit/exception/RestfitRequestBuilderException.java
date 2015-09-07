package com.cookpad.android.restfit.exception;

import com.cookpad.android.restfit.RestfitRequest;

public class RestfitRequestBuilderException extends RestfitException {

    public RestfitRequestBuilderException(RestfitRequest.Builder builder, Throwable throwable) {
        super(builder.toString(), throwable);
    }
}
