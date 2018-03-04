package com.ly.douban;

import android.util.Base64;

/**
 * 网络请求相关的静态常量
 *
 * @author xuding
 */
public interface HttpConstants {


    int HTTP_CONNECT_TIMEOUT = 10 * 1000;

    /**
     * KEY
     */
    byte[] ENCRYPT_KEY = Base64.decode("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4".getBytes(), Base64.DEFAULT);
    /**
     * KEY向量IV
     */
    byte[] ENCRYPT_KEY_IV = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 安全码
     */
    String SAFE_CODE = "F67143D42F4A47266A9D0807EF988752";

    //--------------- 远程配置项 config开头 ------------------
    String CONFIG_UPDATE_INFO = "com.pinsmedical.pinslife2";
    String CONFIG_MAIN_URL = "com.pinsmedical.pinslife2.url";

    String PROP_LOCK_SETTING = "PROP_LOCK_SETTING";

    String CODE_SOURCE_ANDROID = "3";
}
