package com.cookpad.android.restfit.exception;

public class RestfitException extends Exception {

    public RestfitException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
