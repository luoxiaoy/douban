package com.ly.douban.support.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ly.douban.BuildConfig;
import com.ly.douban.MainApplication;
import com.ly.douban.support.logger.Logger;
import com.ly.douban.view.CustomWebChromeClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class SysUtils {

    public static Application getApplication() {
        return MainApplication.getInstance();
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    /**
     * 取属性配置
     *
     * @param path
     * @return
     */
    public static Properties getProperties(String path) {
        Properties propterties = new Properties();
        InputStream inputStream = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            inputStream = new FileInputStream(file);
            propterties.load(inputStream);
        } catch (IOException e) {
            Logger.e("读取配置文件出错", e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return propterties;
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDayHourText(long minites) {
        if (minites <= 0) {
            return "1分钟内";
        }
        String s = "";
        if (minites % 60 != 0) {
            s = s + minites % 60 + "分钟";
        }
        if (minites < 60) {
            return s;
        }
        long h = minites / 60;
        if (h % 24 != 0) {
            s = h % 24 + "小时" + s;
        }
        if (h < 24) {
            return s;
        }
        long d = h / 24;
        if (d > 10) {  //超过10天不显示小时了
            s = d + "天";
        } else {
            s = d + "天" + s;
        }
        return s;
    }

    /**
     * 取今天的日期
     *
     * @return
     */
    public static Date getToday() {
        return getTodayCalendar().getTime();
    }

    /**
     * 取今天的日期
     *
     * @return
     */
    public static Calendar getTodayCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    /**
     * 取零点的日期
     *
     * @return
     */
    public static Date getDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    /**
     * 取零点的日期
     *
     * @return
     */
    public static Date getDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime();
    }

    /**
     * 当前时分
     *
     * @return
     */
    public static String getHourMinute() {
        return DateFormateUtils.format(new Date(), "HH:mm");
    }

    public static String getPath(String path) {
        String sdcard = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        return sdcard + path;
    }

    /**
     * 启动service
     *
     * @param context
     * @param serviceClass
     */
    public static void startService(Context context, Class<? extends Service> serviceClass) {
        Intent intent = new Intent(context, serviceClass);
        context.startService(intent);
    }

    public static void sleep(int timeSleep) {
        SystemClock.sleep(timeSleep);
    }

    public static <T> T getLast(List<T> dataList) {
        if (dataList == null) {
            return null;
        }
        if (dataList.isEmpty()) {
            return null;
        }
        return dataList.get(dataList.size() - 1);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    public static boolean checkTelephoneNumber(CharSequence telephone) {
        String regExp = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    public static boolean checkPassword(CharSequence str) {
        String regExp = "^\\w{6,50}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String setStar(String str, int startLength, int endLength) {
        int len = str.length() - startLength - endLength;
        String regex = "(\\d{" + startLength + "})\\d{" + len + "}(\\w{" + endLength + "})";
        StringBuilder replace = new StringBuilder();
        replace.append("$1");
        for (int i = 0; i < len; i++) {
            replace.append("*");
        }
        replace.append("$2");
        return str.replaceAll(regex, replace.toString());
    }

    public static void requestPermissions(Activity activity, String[] perms) {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(activity, 101, perms)
                        .setRationale("当前应用需要获取必要权限")
                        .setPositiveButtonText("确定")
                        .setNegativeButtonText("取消")
                        .build());
    }

    public static boolean isVideoFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return path.endsWith(".mp4");
    }

    public static void initWebView(Activity context, WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setJavaScriptEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setInitialScale(5);
        int screenDensity = context.getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        settings.setDefaultZoom(zoomDensity);

        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//点击超链接的时候重新在原来进程上加载URL
                return true;
            }

        });
    }

}
