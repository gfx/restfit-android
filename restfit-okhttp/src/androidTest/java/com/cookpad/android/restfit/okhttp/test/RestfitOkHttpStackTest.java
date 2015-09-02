package com.cookpad.android.restfit.okhttp.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.restfit.okhttp.RestfitOkHttpStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RestfitOkHttpStackTest {

    RestfitClient client;

    MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();

        client = new RestfitClient.Builder()
                .endpoint(server.url("/").url())
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitOkHttpStack(new OkHttpClient()))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testPerform200() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .path("/hello")
                .toSingle()
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getStatusMessage(), is("OK"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }
}