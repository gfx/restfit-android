package com.cookpad.android.restfit.test;

import com.google.gson.JsonObject;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitJsonRequestBody;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.restfit.exception.RestfitTimeoutException;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.TimeUnit;

import rx.observers.TestSubscriber;

import static com.cookpad.android.restfit.internal.RestfitUtils.DEFAULT_ENCODING;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
@RunWith(RobolectricTestRunner.class)
public class RestfitHurlStackTest {

    MockWebServer server;

    RestfitClient client;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();

        server.start();

        client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitHurlStack())
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

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getStatusMessage(), is("OK"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform400() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 400 Bad Request")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(400));
        assertThat(response.getStatusMessage(), is("Bad Request"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform401() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 401 Unauthorized")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(401));
        assertThat(response.getStatusMessage(), is("Unauthorized"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform403() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 403 Forbidden")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(403));
        assertThat(response.getStatusMessage(), is("Forbidden"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform404() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 404 Not Found")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(404));
        assertThat(response.getStatusMessage(), is("Not Found"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform422() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 422 Unprocessable Entity")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(422));
        assertThat(response.getStatusMessage(), is("Unprocessable Entity"));
        assertThat(response.getHeaders().get("x-runtime"), is("42"));
        assertThat(response.getBody().getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testPerform500() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.call(client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .build())
                .toObservable()
                .toBlocking()
                .single();

        assertThat(response.getStatusCode(), is(500));
        assertThat(response.getStatusMessage(), is("Internal Server Error"));
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

        client.call(client.requestBuilder()
                .method("POST")
                .url(server.url("/hello").url())
                .body(new RestfitJsonRequestBody(json))
                .build())
                .toObservable()
                .toBlocking()
                .single();

        RecordedRequest request = server.takeRequest();

        assertThat(request.getMethod(), is("POST"));
        assertThat(request.getPath(), is("/hello"));
        assertThat(request.getHeader("content-type"), is("application/json"));
        assertThat(request.getHeader("content-length"), is(String.valueOf(sizeOfString(json.toString()))));
        assertThat(request.getBody().readString(DEFAULT_ENCODING), is(json.toString()));
    }

    @Test
    public void testTimeout() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody("hello, world!")
                .throttleBody(1, 100, TimeUnit.MILLISECONDS));

        TestSubscriber<RestfitResponse> testSubscriber = TestSubscriber.create();

        client.call(client.requestBuilder()
                .method("POST")
                .url(server.url("/hello").url())
                .connectTimeoutMillis(1)
                .readTimeoutMillis(1)
                .build())
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);
        testSubscriber.assertError(RestfitTimeoutException.class);
    }

    int sizeOfString(String s) {
        return s.getBytes(DEFAULT_ENCODING).length;
    }
}
