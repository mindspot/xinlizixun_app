package com.framework.utils.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.framework.utils.BuildProperties;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏工具类
 */
public class StatusBarUtil {

    private Activity activity;

    public StatusBarUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param ShowSysStatusBar 是否不显示系统的状态栏
     */
    @TargetApi(19)
    public void setTranslucentStatus(boolean ShowSysStatusBar) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (ShowSysStatusBar) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
        //再次创建，有时候上面没有初始化状态栏
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(!ShowSysStatusBar);
    }


    /**
     * 设置状态栏文字色值为深色调
     *
     * @param useDart 是否使用深色调
     */
    public void setStatusTextColor(boolean useDart) {
        if (isFlyme()) {
            processFlyMe(useDart);
        } else if (isMIUI()) {
            processMIUI(useDart);
        } else {
            processAndroid(useDart);
        }
    }


    //**************** Flyme ****************

    /**
     * 判断手机是否是魅族
     *
     * @return
     */
    public boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private void processFlyMe(boolean useDark) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (useDark) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    //**************** MIUI ****************
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 判断手机是否是小米
     *
     * @return
     */
    public boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上  lightStatusBar为真时表示黑色字体
     * <p>
     * http://www.miui.com/thread-8946673-1-1.html
     * <p>
     * 附：开发者应该做的修改
     * 如果开发者需要设置「状态栏黑色字符」的效果， 需要做以下几件事：
     * <p>
     * 1. 在新的 MIUI 版本（即基于 Android 6.0 ，开发版 7.7.13 及以后版本）：
     * - 使用 `View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR` ，来设置「状态栏黑色字符」效果
     * - 同时要设置`WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS`，
     * - 并且无 `WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS`
     * <p>
     * 参考实例：
     * Window window = getWindow();
     * window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
     * window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     * window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
     * <p>
     * 2. 在旧的MIUI版本
     * 还有大量的米粉使用旧的MIUI版本，因此仍然需要使用 MIUI 原有的方法，即
     * Window window = getWindow();
     * window.addExtraFlags(MiuiWindowManager.LayoutParams.EXTRA_FLAG_STATUS_BAR_DARK_MODE);
     */
    private void processMIUI(boolean useDart) {
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }

        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, useDart ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        if (useDart) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }


    //**************** Android ****************
    private void processAndroid(boolean useDart) {
        Window window = activity.getWindow();
        if (window == null) {
            return;
        }

        if (useDart) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
        window.getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, 0);
    }
}
