package com.ly.douban;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.douban.support.ActivityScorllWorkaround;
import com.ly.douban.support.ParamMap;
import com.ly.douban.support.http.CommonCallback;
import com.ly.douban.support.http.HttpResponse;
import com.ly.douban.support.utils.RetrofitUtils;
import com.ly.douban.support.utils.UiUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {

    protected Activity context;
    protected LayoutInflater layoutInflater;
    protected ViewGroup flViewContent;

    @BindView(R.id.activity_base_toolbar)
    protected Toolbar activityBaseToolbar;
    @BindView(R.id.left_btn)
    protected ImageView leftBtn;
    @BindView(R.id.icon_right)
    protected ImageView rightBtn;
    @BindView(R.id.title_view)
    protected TextView titleView;
    @BindView(R.id.title_right)
    protected TextView titleRightView;

    protected Map<Function, ProgressDialog> progressDialogStash = new HashMap<>(5);
    Disposable requestDisposable;
    DialogInterface.OnClickListener progressDialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            requestDisposable.dispose();
        }
    };
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        this.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.setContentView(R.layout.activity_base);
        layoutInflater = LayoutInflater.from(context);
        flViewContent = (ViewGroup) findViewById(R.id.content_layout);
        LayoutInflater.from(this).inflate(getLayoutId(), flViewContent);
        ButterKnife.bind(this);
        ActivityScorllWorkaround.assistActivity(this);
        UiUtils.initToolbar(this);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        build();
    }

    public void hideToolbar() {
        UiUtils.hide(activityBaseToolbar);
    }

    public void hideTitleBack() {
        UiUtils.hide(leftBtn);
    }

    public void setTitleBack(int drawableId) {
        leftBtn.setImageResource(drawableId);
    }

    public void setTitleBack(View.OnClickListener clickListener) {
        leftBtn.setOnClickListener(clickListener);
    }

    public void setTitleRightIcon(int drawableId) {
        rightBtn.setImageResource(drawableId);
        rightBtn.setVisibility(View.VISIBLE);
    }

    public void setTitleRightListener(View.OnClickListener listener) {
        rightBtn.setOnClickListener(listener);
    }

    public void setTitle(int titleId) {
        titleView.setText(titleId);
        UiUtils.show(titleView);
    }

    public void setTitle(CharSequence titleStr) {
        titleView.setText(titleStr);
        UiUtils.show(titleView);
    }

    public void setTitleRightView(int titleId) {
        titleRightView.setText(titleId);
        UiUtils.show(titleRightView);
    }

    public void setTitleRight(CharSequence titleStr) {
        titleRightView.setText(titleStr);
        UiUtils.show(titleRightView);
    }

    public void setTitleRight(View.OnClickListener listener) {
        titleRightView.setOnClickListener(listener);
    }

    //-----------------------    UI    -----------------------
    public void showToast(final int resId) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              UiUtils.showToast(context, resId);
                          }
                      }
        );
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              UiUtils.showToast(context, msg);
                          }
                      }
        );
    }

    //-------------------------  HTTP -------------------------
    public <T> void sendRequset(Function<String, ObservableSource<T>> apiObservableFunction, Consumer<T> consumer) {
        sendRequset(apiObservableFunction, consumer, null);
    }

    public <T> void sendRequset(final Function<String, ObservableSource<T>> apiObservableFunction, Consumer<T> consumer, final CommonCallback<Void> allComplete) {
        requestDisposable = Observable.just("")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", progressDialogListener);
                        progressDialog.setMessage("通信中，请稍候...");
                        progressDialog.show();
                        progressDialogStash.put(apiObservableFunction, progressDialog);
                        return s;
                    }
                })
//                .observeOn(Schedulers.io())
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(@NonNull String s) throws Exception {
//                        RetrofitUtils.resetBaseUrl("");
//                        return "";
//                    }
//                })
                .observeOn(Schedulers.io())
                .flatMap(apiObservableFunction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test","数据请求出错"+ throwable);
                        UiUtils.showToast(context, "网络异常");
                        if (allComplete != null) {
                            allComplete.callback(null);
                        }
                        if (progressDialogStash.containsKey(apiObservableFunction)) {
                            progressDialogStash.get(apiObservableFunction).cancel();
                            progressDialogStash.remove(apiObservableFunction);
                        }
                    }

                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (allComplete != null) {
                            allComplete.callback(null);
                        }
                        if (progressDialogStash.containsKey(apiObservableFunction)) {
                            progressDialogStash.get(apiObservableFunction).cancel();
                            progressDialogStash.remove(apiObservableFunction);
                        }
                    }
                });
    }

    public <T> void sendRequsetBackground(Function<String, ObservableSource<HttpResponse<T>>> apiObservableFunction, Consumer<HttpResponse<T>> consumer) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        RetrofitUtils.resetBaseUrl("");
                        return "";
                    }
                })
                .flatMap(apiObservableFunction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("test","数据请求出错" + throwable);
                    }

                });
    }

    public <T> Observable<HttpResponse<T>> addResponseFunction(Observable<HttpResponse<T>> requestObservable, Function<HttpResponse<T>, HttpResponse<T>> responseFunction) {
        return addResponseFunction(requestObservable, responseFunction, false);
    }

    public <T> Observable<HttpResponse<T>> addResponseFunction(Observable<HttpResponse<T>> requestObservable, Function<HttpResponse<T>, HttpResponse<T>> responseFunction, boolean newThread) {
        if (newThread) {
            requestObservable = requestObservable.subscribeOn(Schedulers.newThread());
        }
        return requestObservable.observeOn(AndroidSchedulers.mainThread()).map(responseFunction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestDisposable != null) {
            requestDisposable.dispose();
        }
    }

    protected ParamMap newParam() {
        return new ParamMap();
    }

    protected ProgressDialog getCurrentProgressDialog(Function function) {
        return progressDialogStash.get(function);
    }

    protected boolean onKeyExit(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                UiUtils.showToast(context, "双击退出应用");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void build();
}
