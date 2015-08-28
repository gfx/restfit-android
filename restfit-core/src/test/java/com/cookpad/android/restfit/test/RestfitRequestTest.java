package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitQueryString;
import com.cookpad.android.restfit.RestfitRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.net.Uri;
import android.os.Build;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
@RunWith(RobolectricTestRunner.class)
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

    @Test
    public void testQueryString() throws Exception {
        RestfitRequest r = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com/")
                .queryString(new RestfitQueryString()
                        .put("foo", "bar")
                        .put("ほげ", "ふが"))
                .build();

        assertThat(r.getUrl(), is(Uri.parse("http://example.com/?foo=bar&%E3%81%BB%E3%81%92=%E3%81%B5%E3%81%8C")));
    }

    @Test
    public void testQueryString2() throws Exception {
        RestfitRequest r = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com/?foo=bar")
                .queryString(new RestfitQueryString()
                        .put("ほげ", "ふが"))
                .build();

        Uri url = r.getUrl();
        assertThat(url.getQueryParameterNames(), is(makeSet("foo", "ほげ")));
        assertThat(url.getQueryParameter("foo"), is("bar"));
        assertThat(url.getQueryParameter("ほげ"), is("ふが"));
    }

    @SafeVarargs
    final <T> Set<T> makeSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}
