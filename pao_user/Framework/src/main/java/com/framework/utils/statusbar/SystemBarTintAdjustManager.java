package com.framework.utils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设定状态栏的管理类，封装好了方法，直接调用
 *
 * @author calex_feng
 */
public class SystemBarTintAdjustManager {
    private Activity mActivity;

    private SystemBarTintAdjustManager() {
    }

    public static SystemBarTintAdjustManager getInstance() {
        return new SystemBarTintAdjustManager();
    }

    /**
     * 设置activity
     *
     * @param activity 要设置的activity
     * @return 这个实例
     */
    public SystemBarTintAdjustManager setActivity(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    /**
     * 设置系统的状态条颜色
     *
     * @param color            颜色
     * @param useResourceColor 如果是ture，则使用resourceid
     *                         如果是false，则使用int，例如0xffffee
     * @param rootView         如果传进来视图，则用传进来的视图rootView
     * @param isPadding        是否融进状态栏
     * @param isChangingColor  为true的话，设为color，为false的话，设为透明
     */
    public synchronized void setStatusBar(int color, boolean useResourceColor, View rootView, boolean isPadding) {
        SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View v = null;
            if (rootView != null)
                v = rootView;
            else
                v = getRootView(mActivity);
            //随意设定一个值，10
            if (v != null && v.getPaddingTop() <= 10 && isPadding)
                v.setPadding(0, tintManager.getConfig().getStatusBarHeight(), 0, 0);
            else if (v != null && !isPadding)
                v.setPadding(0, 0, 0, 0);
            setTranslucentStatus(true);
        }

        //再次创建，有时候上面没有初始化状态栏
        tintManager = new SystemBarTintManager(mActivity);
        tintManager.setStatusBarTintEnabled(true);
        //标准的红色0xffe7382b
        if (useResourceColor)
            tintManager.setStatusBarTintResource(color);
        else
            tintManager.setStatusBarTintColor(color);
    }

    /**
     * 设置系统的状态条颜色
     *
     * @param color            颜色
     * @param useResourceColor 如果是ture，则使用resourceid
     *                         如果是false，则使用int，例如0xffffee
     * @param rootView         如果传进来视图，则用传进来的视图rootView
     */
    public synchronized void setStatusBar(int color, boolean useResourceColor) {
        setStatusBar(color, useResourceColor, null, true);
    }

    /**
     * 设置系统的状态条颜色
     *
     * @param color            颜色
     * @param useResourceColor 如果是ture，则使用resourceid
     *                         如果是false，则使用int，例如0xffffee
     */
    public synchronized void setStatusBar(int color, boolean useResourceColor, View rootView) {
        setStatusBar(color, useResourceColor, rootView, true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 得到context的根视图
     *
     * @param context 所在的activity
     * @return 根视图
     */
    private View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }
}
