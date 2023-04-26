package com.sallet.cold.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * JSON Conversion Tool Class
 */

public class JsonUtils {
    private static Gson mGson = new GsonBuilder().create();

    public static <T> String serialize(T t) {
        return mGson.toJson((Object) t);
    }

    public static <T> T deserialize(String str, Class<T> cls) throws JsonSyntaxException {
        return (T) mGson.fromJson(str, (Class) cls);
    }

    public static <T> T deserialize(JsonObject jsonObject, Class<T> cls) throws JsonSyntaxException {
        return (T) mGson.fromJson((JsonElement) jsonObject, (Class) cls);
    }

    public static <T> T deserialize(String str, Type type) throws JsonSyntaxException {
        return (T) mGson.fromJson(str, type);
    }

    public static <T> List<T> deserializeList(String str, Class<T[]> cls) {
        return (List<T>) Arrays.asList((Object[]) new Gson().fromJson(str, (Class) cls));
    }
}
