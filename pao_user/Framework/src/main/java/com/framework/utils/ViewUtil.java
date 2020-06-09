package com.framework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.framework.R;
import com.framework.page.Page;

import java.text.SimpleDateFormat;
import java.util.List;

public class ViewUtil {

    public static final String LOADING_TIP = "正在加载...";

    /*********
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {

        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    /***********
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int height = metric.heightPixels;     // 屏幕高度（像素）
        return height;
    }

    /******
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarH(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /*************
     * 获取屏幕宽跟高
     *
     * @param context
     * @return
     */
    public static int[] getScreenExtent(Context context) {
        int[] buf = new int[2];
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        int height = metric.heightPixels;     // 屏幕高度（像素）
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        buf[0] = width;
        buf[1] = height;
        return buf;
    }

    /**
     * 获取当前app的版本号
     * 在主module中可以使用 BuildConfig.VERSION_NAME
     */
    public static synchronized String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取当前设备ID
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取当前设备ID
    public static String getInstalledTime(Context context) {
        String installedTime = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long firstInstallTime = packageInfo.firstInstallTime;//应用第一次安装的时间
            installedTime = sdf.format(firstInstallTime);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return installedTime;
    }

    //获取当前设备信息
    public static String getDeviceName() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取当前系统的版本号
    public static String getOsVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*********
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**************
     * 获取sdk路径
     *
     * @return
     */
    public static String getSdCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * 还原View的触摸和点击响应范围,最小不小于View自身范围
     *
     * @param view
     */
    public static void restoreViewTouchDelegate(final View view) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                bounds.setEmpty();
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }


    //自定义内容加载提示窗
    private static AlertDialog loadingDialog;
    private static View loadingView;

    public static void showLoading(Activity activity, String content) {
        hideLoading();
        loadingDialog = new AlertDialog.Builder(activity, R.style.alert_dialog).create();
        loadingDialog.setCancelable(false);
//		loadingDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        if (!activity.isFinishing()) {
            loadingDialog.show();
            Window window = loadingDialog.getWindow();
            if (loadingView != null) {
                window.setContentView(R.layout.layout_loading);
                LinearLayout view = (LinearLayout) window.findViewById(R.id.loading_content);
                view.addView(loadingView);
            } else {
                window.setContentView(R.layout.layout_dialog_progress);
                //适配大小
                int screenWidth = getScreenWidth(activity);
                TextView contentView = (TextView) window.findViewById(R.id.contentView);
                contentView.setText(content);
            }
        }

    }

    public static void hideSystemUI(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static void setLoadingView(Context context, View view) {
        loadingView = view;
    }

    /*******
     * 关闭loading
     */
    public static void hideLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                try {
                    loadingDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (loadingView != null) {
                ((LinearLayout) (loadingDialog.getWindow().findViewById(R.id.loading_content))).removeAllViews();
            }
        }
        loadingDialog = null;
    }

    /*
     * 计算ListView内容高度
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        params.height = height + listView.getPaddingTop() + listView.getPaddingBottom();
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断 用户是否安装支付宝客户端
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 唤醒第三方APP
     *
     * @param context     上下文
     * @param packageName 唤醒的APP的包名
     * @return
     */
    public static boolean openAppByPackageName(Context context, String packageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);

            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);

            // 通过getPackageManager()的queryIntentActivities方法遍历
            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                Intent intent = new Intent(Intent.ACTION_MAIN);// LAUNCHER Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个
                // cls: 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                ComponentName cn = new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name);
                intent.setComponent(cn);
                context.startActivity(intent);
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static boolean isFinishedPage(@NonNull Page page) {
        if (page instanceof Fragment || page instanceof android.app.Fragment) {
            Activity activity = page.activity();
            return activity == null || isFinished(activity);
        }
        if (page instanceof Activity) {
            return isFinished((Activity) page);
        }
        // error
        return false;
    }

    public static boolean isFinished(@NonNull Activity activity) {
        boolean isFinishing = activity.isFinishing();
        boolean isDestroyed = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isDestroyed = activity.isDestroyed();
        }
        return isFinishing || isDestroyed;
    }

    /**
     * 显示隐藏密码
     *
     * @param pass
     */
    public static void passShow(final EditText pass, final boolean isShow) {
        if (isShow) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        pass.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = pass.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
