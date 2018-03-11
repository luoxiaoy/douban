package com.ly.douban.business;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    /**
     * 电影条目信息
     * @return
     */
    @GET("movie/subject/{id}")
    Observable<TheatersBean> getSubject(@Path("id") int id);

    /**
     * 电影条目剧照
     * @return
     */
    @GET("movie/subject/{id}/photos")
    Observable<TheatersBean> getSubjectPhotos(@Path("id") int id);

    /**
     * 电影条目影评列表
     * @return
     */
    @GET("movie/subject/{id}/reviews")
    Observable<TheatersBean> getSubjectReviews(@Path("id") int id);

    /**
     * 电影条目短评列表
     * @return
     */
    @GET("movie/subject/{id}/comments")
    Observable<TheatersBean> getSubjectComments(@Path("id") int id);

    /**
     * 影人条目信息
     * @return
     */
    @GET("movie/celebrity/{id}")
    Observable<TheatersBean> getCelebrity(@Path("id") int id);

    /**
     * 影人剧照
     * @return
     */
    @GET("movie/celebrity/{id}/photos")
    Observable<TheatersBean> getCelebrityPhotos(@Path("id") int id);

    /**
     * 影人作品
     * @return
     */
    @GET("movie/celebrity/{id}/works")
    Observable<TheatersBean> getCelebrityWorks(@Path("id") int id);

    /**
     * 电影条目搜索
     * @return
     */
    @GET("movie/search")
    Observable<TheatersBean> getSearchQ(@Query("q") String text);

    /**
     * 电影条目搜索
     * @return
     */
    @GET("movie/search")
    Observable<TheatersBean> getSearchTag(@Query("tag") String text);

}
