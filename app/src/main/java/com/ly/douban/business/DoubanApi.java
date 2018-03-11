package com.ly.douban.business;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ly on 2018/2/11.
 *
 *
 * https://developers.douban.com/wiki/?title=api_v2
 */

public interface DoubanApi {

    /**
     * 正在热映
     * @return
     */
    @GET("movie/in_theaters")
    Observable<TheatersBean> getTheaters(@Query("start") int start,@Query("count") int count);


    /**
     * 即将上映
     * @return
     */
    @GET("movie/coming_soon")
    Observable<TheatersBean> getComingSoon(@Query("start") int start,@Query("count") int count);

    /**
     * Top250
     * @return
     */
    @GET("movie/top250")
    Observable<TheatersBean> getTop250(@Query("start") int start,@Query("count") int count);
}
