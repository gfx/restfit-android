package com.cookpad.android.restfit.test;

import com.google.gson.Gson;

import com.cookpad.android.restfit.RestfitRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class RestfitParcelableTest {

    @Test
    public void testRestfitRequest() throws Exception {

        RestfitRequest a = new RestfitRequest.Builder()
                .method("GET")
                .url("http://example.com")
                .build();

        Parcel parcel = Parcel.obtain();

        parcel.writeParcelable(a, 0);
        parcel.setDataPosition(0);

        RestfitRequest b = RestfitRequest.CREATOR.createFromParcel(parcel);

        Gson gson = new Gson();

        assertThat(gson.toJson(b), is(gson.toJson(a)));

    }
}
