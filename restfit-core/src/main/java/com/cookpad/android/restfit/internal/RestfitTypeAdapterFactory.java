package com.cookpad.android.restfit.internal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class RestfitTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        return new TypeAdapter<T>() {

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                JsonObject object = (JsonObject) new Gson().toJsonTree(out);
                object.addProperty("$class", typeToken.toString());
                gson.toJson(object, out);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return gson.fromJson(in, typeToken.getType());
            }
        };
    }
}
