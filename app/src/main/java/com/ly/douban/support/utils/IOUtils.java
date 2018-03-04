package com.ly.douban.support.utils;

import com.ly.douban.support.logger.Logger;

import java.io.Closeable;

public class IOUtils {

    public static void closeSilently(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (Throwable var2) {
                Logger.e("IOUtils", var2);
            }
        }
    }
}
