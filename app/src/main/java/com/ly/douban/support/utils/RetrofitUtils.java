package com.ly.douban.support.utils;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static final String BASE_URL = "https://api.douban.com/v2/";

    static volatile Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitUtils.class){
                if (retrofit == null){
                    resetBaseUrl(BASE_URL);
                }
            }
        }
        return retrofit;
    }

    public static void resetBaseUrl(String url) {
        Converter.Factory factory = GsonConverterFactory.create(JsonUtils.getGson());
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(factory)
                .client(OkHttpUtils.getInstance())
                .baseUrl(url);
        retrofit = builder.build();
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}