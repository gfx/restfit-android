package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitRequest;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class RestfitRequestTest {

    RestfitRequest a, b, c;


    @Before
    public void setUp() throws Exception {
        a = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "bar")
                .build();

        b = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com/")
                .header("x-foo", "bar")
                .build();

        c = new RestfitRequest.Builder()
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
}
