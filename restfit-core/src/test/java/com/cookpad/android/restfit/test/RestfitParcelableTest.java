package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitClient;
import com.cookpad.android.restfit.RestfitHurlStack;
import com.cookpad.android.restfit.RestfitRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class RestfitParcelableTest {

    RestfitClient client;

    Context getContext() {
        return RuntimeEnvironment.application;
    }

    <T extends Parcelable> T reload(T parcelable) {
        Parcel parcel = Parcel.obtain();

        parcel.writeParcelable(parcelable, 0);
        parcel.setDataPosition(0);

        T reloadedParcelable = parcel.readParcelable(getContext().getClassLoader());

        parcel.recycle();

        return reloadedParcelable;
    }

    @Before
    public void setUp() throws Exception {
        client = new RestfitClient.Builder()
                .userAgent("RestfitTest/1.0")
                .httpStack(new RestfitHurlStack())
                .build();
    }

    @Test
    public void testRestfitRequest() throws Exception {
        RestfitRequest a = client.requestBuilder()
                .method("GET")
                .url("http://example.com")
                .header("X-ABC-XYZ", "foo")
                .build();

        assertThat(reload(a), is(a));
    }
}
