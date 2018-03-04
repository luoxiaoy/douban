package com.ly.douban.support.utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ly.douban.R;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * 界面组件
 *
 * @author xuding
 */
public class UiUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 显示简单toast
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示简单toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean openActivityOnLogin(Context context, Class<? extends Activity> activityClass) {
        Intent nextIntent = new Intent(context, activityClass);
        return openActivityOnLogin(context, nextIntent);
    }

    public static boolean openActivityOnLogin(Context context, Intent nextIntent) {
//        if (!SysUtils.isLogin()) {
//            UiUtils.showToast(context, R.string.user_info_unlogin);
//            Intent loginIntent = new Intent(context, LoginActivity.class);
//            loginIntent.putExtra(LoginActivity.P_NEXT_INTENT, nextIntent);
//            if (!(context instanceof Activity)) {
//                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(loginIntent);
//            return false;
//        }
        context.startActivity(nextIntent);
        return true;
    }

    /**
     * 打开activity
     *
     * @param context
     * @param activityClass
     */
    public static void openActivity(Context context, Class<? extends Activity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void openActivity(Context context, Class<? extends Activity> activityClass, String key, int value) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(key, value);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 打开activity，并关闭自身
     *
     * @param activity
     * @param activityClass
     */
    public static void jumpActivity(Activity activity, Class<? extends Activity> activityClass) {
        openActivity(activity, activityClass);
        activity.finish();
    }

    /**
     * 切换控件隐藏或显示状态
     *
     * @param view
     */
    public static void toggle(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param view
     */
    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    /**
     * @param view
     */
    public static void hide(View view) {
        view.setVisibility(View.GONE);
    }

    /**
     * 遍历布局，并禁用所有子控件
     *
     * @param viewGroup 布局对象
     */
    public static void disableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setEnabled(false);
                    v.setAlpha(0.4f);
                } else if (v instanceof ListView) {
                    v.setEnabled(false);
                    v.setAlpha(0.4f);
                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                v.setEnabled(false);
                v.setAlpha(0.4f);
            } else if (v instanceof TextView) {
                v.setEnabled(false);
                v.setAlpha(0.4f);
            } else if (v instanceof ImageView) {
                v.setEnabled(true);
                v.setAlpha(0.4f);
            }
        }
    }

    public static void enableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setEnabled(true);
                    spinner.setAlpha(1f);
                } else if (v instanceof ListView) {
                    v.setEnabled(true);
                    v.setAlpha(1f);
                } else {
                    enableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                v.setEnabled(true);
                v.setAlpha(1f);
            }
            if (v instanceof TextView) {
                v.setEnabled(true);
                v.setAlpha(1f);
            } else if (v instanceof ImageView) {
                v.setEnabled(true);
                v.setAlpha(1f);
            }
        }
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 设置点击态图片
     *
     * @param imageView
     * @param defaultRes
     * @param pressedRes
     */
    public static void setDrawable(Context context,ImageView imageView, int defaultRes, int pressedRes) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, defaultRes));
        drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, pressedRes));
        drawable.addState(new int[]{android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, pressedRes));
        imageView.setImageDrawable(drawable);
    }

    /**
     * 获取点击态图片
     *
     * @param defaultRes
     * @param changeRes
     */
    public static StateListDrawable getDrawableState(Context context,int defaultRes, int changeRes) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected},
                ContextCompat.getDrawable(context, changeRes));
        drawable.addState(new int[]{android.R.attr.state_checked},
                ContextCompat.getDrawable(context, changeRes));
        drawable.addState(new int[]{android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, changeRes));
        drawable.addState(new int[]{-android.R.attr.state_checked, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, defaultRes));
        return drawable;
    }

    /**
     * 解析html
     *
     * @param html
     * @return
     */
    public static CharSequence parseHtmlText(String html) {
        if (html == null) {
            return "";
        }
        return Html.fromHtml(html);
    }

    /**
     * 结束所有的activity,由于在全局异常捕获时直接结束进程，android系统会自动重启到上一个activity中，必须先将其全部结束
     * android本身没提供这样的方法，为了解决问题，只能先是用反射的机制绕过去，如果谁有重好的方案，请告知丁建水
     *
     * @throws Exception 异常
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void finishAllActivities() {
        try {
            Context baseContext = SysUtils.getApplication().getBaseContext();
            Field mainThreadField = baseContext.getClass()
                    .getDeclaredField("mMainThread");
            mainThreadField.setAccessible(true);
            Object mainThread = mainThreadField.get(baseContext);
            Field activitiesMapField = mainThread.getClass().getDeclaredField(
                    "mActivities");
            activitiesMapField.setAccessible(true);
            Map<Object, Object> activitiesMap = (Map<Object, Object>) activitiesMapField
                    .get(mainThread);
            if (null != activitiesMap && !activitiesMap.isEmpty()) {
                Collection<Object> activities = activitiesMap.values();
                for (Object object : activities) {
                    Field activityField = object.getClass().getDeclaredField(
                            "activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(object);
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有的service
     *
     * @throws Throwable
     * @author dingjsh
     * @time 2014-10-15下午4:59:29
     */
    public static void finishAllServices() {
        try {
            Context baseContext = SysUtils.getApplication().getBaseContext();
            Field mainThreadField = SysUtils.getApplication().getBaseContext().getClass()
                    .getDeclaredField("mMainThread");
            mainThreadField.setAccessible(true);
            Object mainThread = mainThreadField.get(baseContext);
            Field servicesMapField = mainThread.getClass().getDeclaredField(
                    "mServices");
            servicesMapField.setAccessible(true);
            Map<Object, Object> servicesMap = (Map<Object, Object>) servicesMapField
                    .get(mainThread);
            if (null != servicesMap && !servicesMap.isEmpty()) {
                Collection<Object> services = servicesMap.values();
                for (Object serviceObj : services) {
                    if (serviceObj instanceof Service) {
                        Service service = (Service) serviceObj;
                        service.stopSelf();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示或隐藏键盘
     *
     * @param context
     */
    public static void toggleSoftInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示键盘
     *
     * @param context
     */
    public static void showInputMethod(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(context.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
    }

    /**
     * 显示键盘
     *
     * @param context
     */
    public static void showInputMethod(final Context context, final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hideInputMethod(final Context context, final View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 灰色显示图片
     *
     * @param drawable
     */
    public static void setShade(Drawable drawable) {
        drawable.mutate();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(cf);
    }

    public static String getString(int resid) {
        return SysUtils.getApplication().getString(resid);
    }

    public static String formatStr(int resId, Object... text) {
        return String.format(SysUtils.getApplication().getString(resId), text);
    }

    /**
     * @param activity
     * @param dialogFragment
     */
    public static void showDialog(Activity activity, DialogFragment dialogFragment) {
        if (activity.isFinishing()) {
            return;
        }
        dialogFragment.show(activity.getFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    public static void initToolbar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  // 填充statusbar
                ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
                ViewGroup mContentParent = (ViewGroup) mContentView.getParent();
                View statusBarView = mContentParent.getChildAt(0);
                if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == SysUtils.getStatusBarHeight(activity)) {
                    statusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                    return;
                }
                statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        SysUtils.getStatusBarHeight(activity));
                statusBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue));
                mContentParent.addView(statusBarView, 0, lp);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int statusBarHeight = SysUtils.getStatusBarHeight(activity);
                ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
                mContentView.setPadding(0, statusBarHeight, 0, 0);    //给content设置paddingTop
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.black)); //statusbr字体颜色
            }
        }
    }

//    /**
//     * 构建输入框事件
//     *
//     * @param activity
//     * @param view
//     */
//    public static void buildEditEvent(Activity activity, TextView view, int maxInputLength) {
//        buildEditEvent(activity, view, maxInputLength, null);
//    }
//
//    /**
//     * 构建输入框事件
//     *
//     * @param activity
//     * @param view
//     * @param maxInputLength
//     * @param iCallback1
//     */
//    public static void buildEditEvent(final Activity activity, final TextView view, final int maxInputLength, CommonCallback<String> iCallback1) {
//        view.setFocusable(false);
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Subscribe
//            public void onEventInput(InputActivity.InputValue inputValue) {
//                if (inputValue.value() != null) {
//                    view.setText(inputValue.value());
//                }
//                SysUtils.unRegisterEvent(this);
//            }
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, InputActivity_.class);
//                intent.putExtra("initText", view.getText().toString());
//                if (view.getHint() != null) {
//                    intent.putExtra("hint", view.getHint().toString());
//                }
//                intent.putExtra("textMaxLength", maxInputLength);
//                activity.startActivity(intent);
//                if (!EventBus.getDefault().isRegistered(this)) {
//                    EventBus.getDefault().register(this);
//                }
//            }
//        });
//    }

    public static void dismissDialog(final Activity activity, final DialogFragment dialogFragment) {
        if (dialogFragment == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogFragment.dismiss();
            }
        });
    }

    public int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    public static int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }
}
