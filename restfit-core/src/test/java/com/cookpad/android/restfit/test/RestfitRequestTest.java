package com.cookpad.android.restfit.test;

import com.google.gson.JsonObject;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitQueryStringBuilder;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitRequestJsonBody;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.net.Uri;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitRequestTest {

    RestfitRequest a, b, c;

    RestfitClient client;

    @Before
    public void setUp() throws Exception {
        client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitHurlStack())
                .endpoint("https://example.com")
                .build();

        a = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "bar")
                .build();

        b = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "bar")
                .build();

        c = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "baz")
                .build();
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(a, is(b));
        assertThat(a, is(not(c)));
    }

    @Test
    public void testEqualsForLists() throws Exception {
        assertThat(Collections.singletonList(a), is(Collections.singletonList(b)));
        assertThat(Collections.singletonList(a), is(not(Collections.singletonList(c))));
    }


    @Test
    public void testHashCode() throws Exception {
        assertThat(a.hashCode(), is(b.hashCode()));
    }

    @Test
    public void testPath() throws Exception {
        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .url("http://example.com/v1")
                .path("hello")
                .build();

        assertThat(r.getUrl(), is(Uri.parse("http://example.com/v1/hello")));
    }

    @Test
    public void testEndpointAndPath() throws Exception {
        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .path("hello")
                .build();

        assertThat(r.getUrl(), is(Uri.parse("https://example.com/hello")));
    }


    @Test
    public void testQueryString() throws Exception {
        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .queryString(new RestfitQueryStringBuilder()
                        .put("foo", "bar")
                        .put("ほげ", "ふが"))
                .build();

        assertThat(r.getUrl(), is(Uri.parse("http://example.com/?foo=bar&%E3%81%BB%E3%81%92=%E3%81%B5%E3%81%8C")));
    }

    @Test
    public void testQueryString2() throws Exception {
        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .url("http://example.com/?foo=bar")
                .queryString(new RestfitQueryStringBuilder()
                        .put("ほげ", "ふが"))
                .build();

        Uri url = r.getUrl();
        assertThat(url.getQueryParameterNames(), is(makeSet("foo", "ほげ")));
        assertThat(url.getQueryParameter("foo"), is("bar"));
        assertThat(url.getQueryParameter("ほげ"), is("ふが"));
    }

    @Test
    public void testRequestBody() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("foo", "bar");

        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .body(new RestfitRequestJsonBody(json))
                .build();

        assertThat(r.getHeaders().get("content-type"), is(notNullValue()));
        assertThat(r.getHeaders().get("content-length"), is(notNullValue()));
        assertThat(r.hasBody(), is(true));
        assertThat(r.getBody(), is(notNullValue()));
    }

    @SafeVarargs
    final <T> Set<T> makeSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}
