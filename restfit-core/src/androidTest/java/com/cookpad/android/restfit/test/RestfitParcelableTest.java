package com.cookpad.android.restfit.test;

import com.cookpad.android.restfit.RestfitRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class RestfitParcelableTest {

    Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    <T extends Parcelable> T reload(T parcelable) {
        Parcel parcel = Parcel.obtain();

        parcel.writeParcelable(parcelable, 0);
        parcel.setDataPosition(0);

        T reloadedParcelable = parcel.readParcelable(getContext().getClassLoader());

        parcel.recycle();

        return reloadedParcelable;
    }

    @Test
    public void testRestfitRequest() throws Exception {
        RestfitRequest a = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com")
                .header("X-ABC-XYZ", "foo")
                .build();

        assertThat(reload(a), is(a));
    }


}
