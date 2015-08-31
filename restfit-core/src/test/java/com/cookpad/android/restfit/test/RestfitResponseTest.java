package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitResponseTest {

    RestfitClient client = new RestfitClient.Builder()
            .userAgent("RestfitTest/1.0")
            .httpStack(new RestfitHurlStack())
            .build();

    RestfitResponse a, b, c;

    @Before
    public void setUp() throws Exception {
        RestfitRequest r = client.requestBuilder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "bar")
                .build();

        a = new RestfitResponse.Builder()
                .request(r)
                .status(200, "OK")
                .build();

        b = new RestfitResponse.Builder()
                .request(r)
                .status(200, "OK")
                .build();

        c = new RestfitResponse.Builder()
                .request(r)
                .status(201, "OK")
                .build();
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(a, is(b));
        assertThat(a, is(not(c)));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(a.hashCode(), is(b.hashCode()));
    }
}
