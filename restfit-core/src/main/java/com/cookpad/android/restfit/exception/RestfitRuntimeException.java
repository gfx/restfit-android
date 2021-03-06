package com.cookpad.android.restfit.exception;

public class RestfitRuntimeException extends RuntimeException {

    public RestfitRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public RestfitRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
