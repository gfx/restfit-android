package com.cookpad.android.restfit;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestfitExecutors {

    @NonNull
    public static ExecutorService createDefaultThreadPoolExecutor() {
        return Executors.newFixedThreadPool(4);
    }
}
