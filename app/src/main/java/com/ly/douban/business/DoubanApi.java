package com.ly.douban.business;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ly on 2018/2/11.
 */

public interface DoubanApi {

    @GET("movie/in_theaters")
    Observable<String> getTheaters();
}
