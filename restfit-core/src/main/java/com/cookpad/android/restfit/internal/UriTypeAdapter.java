package com.cookpad.android.restfit.internal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.net.Uri;

import java.io.IOException;

class UriTypeAdapter extends TypeAdapter<Uri> {

    @Override
    public void write(JsonWriter out, Uri value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Uri read(JsonReader in) throws IOException {
        return Uri.parse(in.nextString());
    }
}
