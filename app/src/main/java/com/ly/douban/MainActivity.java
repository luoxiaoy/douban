package com.ly.douban;


import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ly.douban.business.DoubanApi;
import com.ly.douban.support.logger.Logger;
import com.ly.douban.support.utils.RetrofitUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class MainActivity extends BaseActivity {

    @BindView(R.id.banner)
    TextView banner;

    @BindView(R.id.container)
    ConstraintLayout container;

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    URL url = new URL("http://www.160.com/");
//                    //打开连接
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                    // set header
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setRequestProperty("accept", "*/*");
//                    urlConnection.setRequestProperty("user-agent", "youtu-android-sdk");
//                    urlConnection.setRequestProperty("Authorization", "mySign");
//
////		connection.setConnectTimeout(30000);
////		connection.setReadTimeout(30000);
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setDoInput(true);
//                    urlConnection.setUseCaches(false);
//                    urlConnection.setInstanceFollowRedirects(true);
//                    urlConnection.setRequestProperty("Content-Type", "text/json");
//                    urlConnection.connect();
//
//                    if (200 == urlConnection.getResponseCode()) {
//                        InputStream is = urlConnection.getInputStream();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        byte[] buffer = new byte[1024];
//                        int len = 0;
//                        while (-1 != (len = is.read(buffer))) {
//                            baos.write(buffer, 0, len);
//                            baos.flush();
//                        }
//                        Log.e("test", "------- " + baos.toString("utf-8"));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void build() {
        sendRequset(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                DoubanApi doubanApi = RetrofitUtils.getRetrofit().create(DoubanApi.class);
                return doubanApi.getTheaters();
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Logger.e(s);
            }
        });

//        container.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("test","------- onTouch ------- container");
//                return false;
//            }
//        });

//        Log.e("test",)
    }


//    @OnClick(R.id.banner)
//    public void onClickBanner(){
//        Log.e("test","-------------- banner");
//    }

    @OnClick(R.id.container)
    public void onContainer(){
        Log.e("test","-------------- container");
    }


}
