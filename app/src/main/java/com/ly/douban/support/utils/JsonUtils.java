package com.ly.douban.support.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.ly.douban.support.utils.gson.DateDeserializer;
import com.ly.douban.support.utils.gson.DateSerializer;
import com.ly.douban.support.utils.gson.StringDeserializer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;


public class JsonUtils {

    static Gson gson;

    static {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Date.class, new DateSerializer());
        gb.registerTypeAdapter(Date.class, new DateDeserializer());
        gb.registerTypeAdapter(String.class, new StringDeserializer());
        gson = gb.create();
    }

    public static Gson getGson() {
        return gson;
    }

    /**
     * 解析json串为对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        return gson.fromJson(jsonString, clazz);
    }

    /**
     * 解析json串为对象
     *
     * @param jsonString
     * @param typeToken
     * @return
     */
    public static <T> T fromJson(String jsonString, TypeToken typeToken) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return gson.fromJson(jsonString, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析json串为对象
     *
     * @param jsonString
     * @param type
     * @return
     */
    public static <T> T fromJson(String jsonString, Type type) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        return gson.fromJson(jsonString, type);
    }

    /**
     * 解析json串为List
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T toList(String jsonString, Class<?> clazz) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        Type type = $Gson$Types.newParameterizedTypeWithOwner(null, TypeToken.get(List.class).getType(), TypeToken.get(clazz).getType());
        return gson.fromJson(jsonString, type);
    }

    /**
     * 解析json串为数组
     *
     * @param jsonString
     * @param typeToken
     * @return
     */
    public static <T> T[] toArray(String jsonString, TypeToken<T> typeToken) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        Type type = $Gson$Types.arrayOf(typeToken.getType());
        return gson.fromJson(jsonString, type);
    }

    /**
     * 把对象序列化为json串
     *
     * @param srcObject
     * @return
     */
    public static String toJson(Object srcObject) {
        return gson.toJson(srcObject);
    }

    /**
     * JsonElement
     *
     * @param obj
     * @return
     */
    public static JsonElement toJsonElement(Object obj) {
        return gson.toJsonTree(obj);
    }
}
