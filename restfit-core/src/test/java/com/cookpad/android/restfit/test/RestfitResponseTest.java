package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitRequest;
import com.cookpad.android.restfit.RestfitResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
public class RestfitResponseTest {

    RestfitResponse a, b, c;

    @Before
    public void setUp() throws Exception {
        RestfitRequest r = new RestfitRequest.Builder()
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
