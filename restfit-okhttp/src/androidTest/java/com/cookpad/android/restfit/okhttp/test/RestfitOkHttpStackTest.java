package com.cookpad.android.restfit.okhttp.test;

import com.google.gson.JsonObject;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.restfit.internal.RestfitUtils;
import com.cookpad.android.restfit.okhttp.RestfitOkHttpStack;
import com.cookpad.android.restfit.okhttp.RestfitRequestOkHttpBody;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

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

    @Test
    public void testRequestBody() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody("hello, world!"));

        JsonObject json = new JsonObject();
        json.addProperty("foo", "bar");

        client.requestBuilder()
                .method("POST")
                .url(server.url("/hello").url())
                .body(new RestfitRequestOkHttpBody(RequestBody.create(MediaType.parse("application/json"), json.toString())))
                .toSingle()
                .toObservable()
                .toBlocking()
                .single();

        RecordedRequest request = server.takeRequest();

        assertThat(request.getMethod(), is("POST"));
        assertThat(request.getPath(), is("/hello"));
        assertThat(request.getHeader("content-type"), is("application/json; charset=utf-8"));
        assertThat(request.getBody().readString(RestfitUtils.DEFAULT_ENCODING), is(json.toString()));
    }

}