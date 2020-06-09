package util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

/**
 * Created by User on 2017/11/21.
 * 解决WebView 输入框被遮挡问题
 */
public class AndroidBug5497Workaround {

    private final Activity activity;

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static void assistActivity(Activity activity) {
        new AndroidBug5497Workaround(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int statusBarHeight;//状态栏高度

    private AndroidBug5497Workaround(final Activity activity) {
        this.activity = activity;
        if (checkDeviceHasNavigationBar(activity)) {
            //获取状态栏的高度
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        //1､找到Activity的最外层布局控件，它其实是一个DecorView,它所用的控件就是FrameLayout
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        //2､获取到setContentView放进去的View
        mChildOfContent = content.getChildAt(0);
        //3､给Activity的xml布局设置View树监听，当布局有变化，如键盘弹出或收起时，都会回调此监听
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //4､软键盘弹起会使GlobalLayout发生变化
            public void onGlobalLayout() {
                //5､当前布局发生变化时，对Activity的xml布局进行重绘
                possiblyResizeChildOfContent(checkDeviceHasNavigationBar(activity));
            }
        });
        //6､获取到Activity的xml布局的放置参数
        frameLayoutParams = (FrameLayout.LayoutParams)
                mChildOfContent.getLayoutParams();
    }

    /**
     * 重新调整布局高度
     * 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
     *
     * @param hasNav
     */
    private void possiblyResizeChildOfContent(boolean hasNav) {
        //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        int usableHeightNow = computeUsableHeight(hasNav);
        //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3､获取Activity中xml中布局在当前界面显示的高度
            int usableHeightSansKeyboard;
            if (hasNav)
                usableHeightSansKeyboard = mChildOfContent.getHeight();//兼容华为等机型
            else {
                usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
                //这个判断是为了解决19之前的版本不支持沉浸式状态栏导致布局显示不完全的问题
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    Rect frame = new Rect();
                    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;
                    usableHeightSansKeyboard -= statusBarHeight;
                }
            }
            //4､Activity中xml布局的高度-当前可用高度
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            //5､高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasNav) {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + statusBarHeight;
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                if (hasNav)
                    frameLayoutParams.height = usableHeightNow + statusBarHeight;
                else
                    frameLayoutParams.height = usableHeightSansKeyboard;
            }
            //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算mChildOfContent可见高度 ** @return
     */
    private int computeUsableHeight(boolean hasNav) {
        if (hasNav) {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);
            // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
            if (r.top < statusBarHeight)
                return r.bottom - statusBarHeight;
            else
                return r.bottom - r.top;
        } else {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);

            //这个判断是为了解决19之后的版本在弹出软键盘时，键盘和推上去的布局（adjustResize）之间有黑色区域的问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return (r.bottom - r.top) + statusBarHeight;
            }
            return (r.bottom - r.top);
        }
    }

    /**
     * 通过"qemu.hw.mainkeys"判断是否存在NavigationBar
     *
     * @return 是否有NavigationBar
     */
    private static boolean checkDeviceHasNavigationBar(Activity activity) {
        boolean hasNavigationBar = false;
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            } else {
                hasNavigationBar = hasNavBar(activity);
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 根据屏幕真实宽高-可用宽高>0来判断是否存在NavigationBar
     *
     * @param activity 上下文
     * @return 是否有NavigationBar
     */
    private static boolean hasNavBar(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
}