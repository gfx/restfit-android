package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHttpHandler;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;

import org.junit.Test;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class RestfitClientTest {

    class DummyHttpHandler implements RestfitHttpHandler {

        @NonNull
        @Override
        public Single<RestfitResponse> execute(@NonNull RestfitRequest request) {
            return Single.error(new AssertionError("never called"));
        }
    }

    @Test
    public void testCreateInstanceWithoutUserAgent() throws Exception {
        try {
            new RestfitClient.Builder()
                    .httpHandler(new DummyHttpHandler())
                    .build();
            fail("never reached");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("No userAgent specified"));
        }
    }

    @Test
    public void testCreateInstanceWithoutHttpHandler() throws Exception {
        try {
            new RestfitClient.Builder()
                    .userAgent("RestfitTest/1.0")
                    .build();
            fail("never reached");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("No httpHandler specified"));
        }
    }

    @Test
    public void testCreateInstance() throws Exception {
        RestfitClient client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpHandler(new DummyHttpHandler())
                .build();

        assertThat(client.getUserAgent(), is("RestfitTest/1.0"));
    }


    @Test
    public void testCallRequest() throws Exception {
        final RestfitResponse response = new RestfitResponse();

        RestfitClient client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpHandler(new RestfitHttpHandler() {
                    @NonNull
                    @Override
                    public Single<RestfitResponse> execute(@NonNull RestfitRequest request) {
                        return Single.just(response);
                    }
                })
                .build();

        RestfitRequest request = new RestfitRequest.Builder()
                .build();

        TestSubscriber<RestfitResponse> testSubscriber = TestSubscriber.create();

        client.call(request).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertReceivedOnNext(Collections.singletonList(response));
    }
}
