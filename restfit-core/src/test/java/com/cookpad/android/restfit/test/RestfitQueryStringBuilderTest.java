package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitQueryStringBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitQueryStringBuilderTest {

    @Test
    public void testParse() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder("foo=1&bar=baz");

        assertThat(b.build(), is("foo=1&bar=baz"));
    }

    @Test
    public void testParseSemicolons() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder("foo=1;bar=baz");

        assertThat(b.build(), is("foo=1&bar=baz"));
    }

    @Test
    public void testParseIncludingMultipleEquals() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder("foo=1;bar=b=a=z");

        assertThat(b.build(), is("foo=1&bar=b%3Da%3Dz"));
    }


    @Test
    public void testPutQueryString() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();
        b.putQueryString("foo=1;bar=baz");

        assertThat(b.build(), is("foo=1&bar=baz"));
    }


    @Test
    public void testPutStrings() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo&;=/", "&;=/");
        b.put("bar", "");

        assertThat(b.build(), is("foo%26%3B%3D%2F=%26%3B%3D%2F&bar="));
    }

    @Test
    public void testPutIfNotEmpty() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.putIfNotEmpty("foo", "&;=");
        b.putIfNotEmpty("bar", "");

        assertThat(b.build(), is("foo=%26%3B%3D"));
    }

    @Test
    public void testPutEncodedValue() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.putEncodedValue("foo&;=/", "%26%3B%3D%2F");
        b.putEncodedValue("bar", "");

        assertThat(b.build(), is("foo%26%3B%3D%2F=%26%3B%3D%2F&bar="));
    }

    @Test
    public void testPutEncodedPair() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.putEncodedPair("foo%26%3B%3D%2F", "%26%3B%3D%2F");
        b.putEncodedPair("bar", "");

        assertThat(b.build(), is("foo%26%3B%3D%2F=%26%3B%3D%2F&bar="));
    }

    @Test
    public void testPut1() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", 0);
        b.put("bar", 1);

        assertThat(b.build(), is("foo=0&bar=1"));
    }

    @Test
    public void testPut2() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", 0.0);
        b.put("bar", 1.5);

        assertThat(b.build(), is("foo=0.0&bar=1.5"));
    }

    @Test
    public void testPut3() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", true);
        b.put("bar", false);

        assertThat(b.build(), is("foo=true&bar=false"));
    }

    @Test
    public void testPut4() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        Map<String, String> m = new HashMap<>();
        m.put("foo", "bar");
        m.put("baz", "bax");

        b.put("foo", m);
        b.put("bar", 42);

        assertThat(b.build(), is("foo[foo]=bar&foo[baz]=bax&bar=42"));
    }

    @Test
    public void testPutAll() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", "bar");
        b.putAll(new RestfitQueryStringBuilder("baz=bax"));

        assertThat(b.build(), is("foo=bar&baz=bax"));
    }

    @Test
    public void testKeySet() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", "hello");
        b.put("bar", "world");

        Set<String> expectedKeySet = new HashSet<>();
        expectedKeySet.add("foo");
        expectedKeySet.add("bar");

        assertThat(b.keySet(), is(expectedKeySet));
    }

    @Test
    public void testGet() throws Exception {
        RestfitQueryStringBuilder b = new RestfitQueryStringBuilder();

        b.put("foo", "hello");
        b.put("bar", "world");

        assertThat(b.get("foo"), is("hello"));
        assertThat(b.get("bar"), is("world"));
        assertThat(b.get("baz"), is(nullValue()));
    }

}
