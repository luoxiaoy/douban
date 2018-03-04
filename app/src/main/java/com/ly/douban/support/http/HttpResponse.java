package com.ly.douban.support.http;


public class HttpResponse<T> {

    public int error_code;
    public boolean success;
    public int status_code;
    public String message;
    public String userid;

    public T data;
}
