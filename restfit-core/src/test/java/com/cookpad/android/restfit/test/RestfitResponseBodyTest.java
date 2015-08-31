package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponseBody;
import com.cookpad.android.restfit.internal.RestfitUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitResponseBodyTest {

    InputStream inputStream;

    RestfitResponseBody body;

    @Before
    public void setUp() throws Exception {
        RestfitClient client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitHurlStack())
                .build();

        RestfitRequest request = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .build();

        inputStream = new ByteArrayInputStream("hello, world!".getBytes(RestfitUtils.DEFAULT_ENCODING));
        body = new RestfitResponseBody(request, inputStream);
    }

    @Test
    public void testGetAsString() throws Exception {
        assertThat(body.getAsString().toObservable().toBlocking().single(), is("hello, world!"));
    }

    @Test
    public void testGetAsStringSync() throws Exception {
        assertThat(body.getAsStringSync(), is("hello, world!"));
    }

    @Test
    public void testGetInputStream() throws Exception {
        InputStream in = body.getInputStream();

        assertThat(in.available(), is(greaterThan(0)));
        assertThat(RestfitUtils.slurpAsString(in), is("hello, world!"));
    }
}
