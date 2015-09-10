package com.cookpad.android.restfit.spec;

import com.google.gson.JsonObject;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHttpStack;
import com.cookpad.android.restfit.RestfitRequestJsonBody;
import com.cookpad.android.restfit.RestfitResponse;
import com.cookpad.android.restfit.exception.RestfitRequestException;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import rx.observers.TestSubscriber;

import static com.cookpad.android.restfit.internal.RestfitUtils.DEFAULT_ENCODING;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RestfitHttpStackSpec {

    final MockWebServer server = new MockWebServer();

    final RestfitClient client;

    public interface HttpStackFactory {

        RestfitHttpStack createInstance();
    }

    @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> getParameters() {
        Object[][] parameters = new Object[][]{
                {HurlStackFactory.class.getName()},
                {OkHttpStackFactory.class.getName()}
        };
        return Arrays.asList(parameters);
    }

    public RestfitHttpStackSpec(String factoryClassName) throws Exception {
        // XXX: hack to avoid class loader mismatching problems!
        Class<?> factoryClass = Class.forName(factoryClassName);
        HttpStackFactory httpStackFactory = (HttpStackFactory) factoryClass.newInstance();
        client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(httpStackFactory.createInstance())
                .build();
    }


    @Before
    public void setUp() throws Exception {
        server.start();
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
                .url(server.url("/hello").url())
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
    public void testPerform400() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 400 Bad Request")
                .setHeader("X-Runtime", 42)
                .setBody("hello, world!"));

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        RestfitResponse response = client.requestBuilder()
                .method("GET")
                .url(server.url("/hello").url())
                .toSingle()
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

        client.requestBuilder()
                .method("POST")
                .url(server.url("/hello").url())
                .body(new RestfitRequestJsonBody(json))
                .toSingle()
                .toObservable()
                .toBlocking()
                .single();

        RecordedRequest request = server.takeRequest();

        assertThat(request.getMethod(), is("POST"));
        assertThat(request.getPath(), is("/hello"));
        assertThat(request.getHeader("content-type"), is("application/json"));
        assertThat(request.getBody().readString(DEFAULT_ENCODING), is(json.toString()));
    }

    @Test
    public void testRequestChunkedBody() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setChunkedBody("hello, world!", 1));

        JsonObject json = new JsonObject();
        json.addProperty("foo", "bar");

        client.requestBuilder()
                .method("POST")
                .url(server.url("/hello").url())
                .body(new RestfitRequestJsonBody(json))
                .toSingle()
                .toObservable()
                .toBlocking()
                .single();

        RecordedRequest request = server.takeRequest();

        assertThat(request.getMethod(), is("POST"));
        assertThat(request.getPath(), is("/hello"));
        assertThat(request.getHeader("content-type"), is("application/json"));
        assertThat(request.getBody().readString(DEFAULT_ENCODING), is(json.toString()));
    }

    @Ignore // TODO: do it correctly
    @Test
    public void testTimeout() throws Exception {
        //assumeThat(client.getHttpStack(), is(not(instanceOf(RestfitOkHttpStack.class))));

        int responseBodySize = 16 * 1024 * 1024; // 16 MiB
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody(new Buffer().write(new byte[responseBodySize]))
                .throttleBody(1024, 1, TimeUnit.SECONDS));

        TestSubscriber<RestfitResponse> testSubscriber = TestSubscriber.create();

        long t0 = System.currentTimeMillis();

        client.requestBuilder()
                .method("GET")
                .url(server.url("/hello-timeout").url())
                .connectTimeoutMillis(50)
                .readTimeoutMillis(50)
                .toSingle()
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        System.out.println("XXX elapsed " + (System.currentTimeMillis() - t0));

        testSubscriber.assertCompleted();
        testSubscriber.assertError(RestfitRequestException.class);
        RestfitRequestException e = (RestfitRequestException) testSubscriber.getOnErrorEvents().get(0);
        assertThat(e.isTimeout(), is(true));
    }

    @Test
    public void testUnknownHost() throws Exception {
        server.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 200 OK")
                .setBody("hello, world!")
                .throttleBody(1, 100, TimeUnit.MILLISECONDS));

        TestSubscriber<RestfitResponse> testSubscriber = TestSubscriber.create();

        client.requestBuilder()
                .method("GET")
                .url("http://not-a-valid-host/hello")
                .toSingle()
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent(1, TimeUnit.SECONDS);

        testSubscriber.assertError(RestfitRequestException.class);
        RestfitRequestException e = (RestfitRequestException) testSubscriber.getOnErrorEvents().get(0);
        assertThat(e.isUnknownHost(), is(true));
    }
}
