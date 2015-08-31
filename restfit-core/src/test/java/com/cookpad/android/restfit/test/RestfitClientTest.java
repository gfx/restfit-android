package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.observers.TestSubscriber;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitClientTest {

    class DummyHttpStack implements RestfitHttpStack {

        @NonNull
        @Override
        public Single<RestfitResponse> perform(@NonNull RestfitRequest request) {
            return Single.error(new AssertionError("never called"));
        }
    }

    RestfitResponse response;

    @Test
    public void testCreateInstanceWithoutUserAgent() throws Exception {
        try {
            new RestfitClient.Builder()
                    .httpStack(new DummyHttpStack())
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
            assertThat(e.getMessage(), is("No httpStack specified"));
        }
    }

    @Test
    public void testCreateInstance() throws Exception {
        RestfitClient client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new DummyHttpStack())
                .build();

        assertThat(client.getUserAgent(), is("RestfitTest/1.0"));
    }

    @Test
    public void testCallRequest() throws Exception {

        RestfitClient client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitHttpStack() {
                    @NonNull
                    @Override
                    public Single<RestfitResponse> perform(@NonNull RestfitRequest request) {
                        return Single.just(response);
                    }
                })
                .build();

        RestfitRequest request = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .build();

        response = new RestfitResponse.Builder()
                .request(request)
                .build();

        TestSubscriber<RestfitResponse> testSubscriber = TestSubscriber.create();

        client.call(request).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertReceivedOnNext(Collections.singletonList(response));
    }
}
