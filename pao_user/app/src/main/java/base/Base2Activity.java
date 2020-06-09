package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.framework.page.Page;
import com.framework.utils.CustomToast;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.paopao.paopaouser.R;
import com.umeng.socialize.UMShareAPI;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import common.repository.http.HttpApi;
import common.router.CommandRouter;
import common.webview.custom.BaiTiaoWebView;
import common.webview.page.WebViewActivity;
import me.jessyan.autosize.internal.CustomAdapt;
import module.app.MyApplication;
import util.CommonPopupWindowUtil;
import util.TimeUtils;
import util.ToolBarUtil;

/**
 * activity的基类
 *
 * @author Administrator
 */
public abstract class Base2Activity extends AppCompatActivity implements Page.ActivityPage, CustomAdapt {


    /**
     * 设置是否添加到Activity管理列表，以免启动页等弹出Dialog
     */
    public boolean canAdd = true;
    private Unbinder unbinder;

    private ToolBarUtil toolBarUtil;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("android:support:fragments", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 防止Activity被回收后，重新启动时，将fragment再次启动造成的空指针bug
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent
                    .ACTION_MAIN)) {
                finish();
                return;
            }
        }
        if (canAdd) {
            CommonPopupWindowUtil.activities.add(this);
        }
        // 虚拟导航栏 背景色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        toolBarUtil = new ToolBarUtil(this);
        toolBarUtil.setTranslucentStatus(true);
        toolBarUtil.setStatusTextColor(true);

        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);

        /**
         * 去除灰色遮罩 Android5.0以上
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Android4.4以上,5.0以下
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        toolBarUtil.setStatusTextColor(true);

        initView(savedInstanceState);
        initListener();
        loadData();
    }

    protected abstract int getContentViewId();

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initListener();

    public abstract void loadData();


    // ************** handler **************

    private Handler myHandler;

    public Handler getHandler() {
        if (myHandler == null) {
            myHandler = new Handler(Looper.getMainLooper());
        }
        return myHandler;
    }

    private void clearHandler() {
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }
    }


    // ************** key down **************

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onInterruptEvent != null) {
                boolean flag = onInterruptEvent.onPressBackInterrupt();
                return flag || super.onKeyDown(keyCode, event);
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    OnInterruptEvent onInterruptEvent;

    public void setOnInterruptEvent(OnInterruptEvent event) {
        onInterruptEvent = event;
    }

    public interface OnInterruptEvent {
        /**
         * @return true 表示拦截
         */
        boolean onPressBackInterrupt();
    }


    // ************** window focus changed **************

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (iFocus != null) {
                iFocus.focused();
            }
        }
    }

    private IWindowFocus iFocus;

    @Override
    public void setOnIWindowFocus(IWindowFocus windowFocus) {
        iFocus = windowFocus;
    }


    // ************** life cycle **************

    @Override
    protected void onResume() {
        super.onResume();
        if (getJumpByPushIfNeed()) {
            CommandRouter.jumpByPushIfNeed(this);
        }
    }

    /**
     * 设置是否执行Push/OpenApp 唤醒某个页面的操作，引导页、闪屏页、登录页、注册页 不需要执行唤醒
     */
    public boolean getJumpByPushIfNeed() {
        return true;
    }

    @Override
    protected void onPause() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearHandler();
//        unbinder.unbind();
//        UMShareAPIWrapper.release(this);
        CommandRouter.detached(this);
        HttpApi.cancelRequest(this);
        CommonPopupWindowUtil.activities.remove(this);
        CommonPopupWindowUtil.hidePopWin();
        CustomToast.INSTANCE.cancelToast();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CommandRouter.onActivityResult(this, requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CommandRouter.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // ************** page **************

    @Override
    public Context context() {
        return this;
    }

    @Override
    public Activity activity() {
        return this;
    }

    @Override
    public void showToast(String message) {
        if (!StringUtil.isBlank(message) && !ViewUtil.isFinishedPage(this)) {
            CustomToast.INSTANCE.showToast(this, message, isToastCenter());
        }
    }

    protected boolean isToastCenter() {
        return false;
    }

    @Override
    public <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public View root() {
        return $(android.R.id.content);
    }


    // ************** process recycled **************

    /**
     * 防止APP进程被回收后，造成的view不显示的问题
     * 清空Activity record上所有Activity
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if (getClass() == StoreMainActivity.class) {
//            Intent intent = new Intent(this, SelectURLActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        } else {
//            finish();
//        }
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }

    public void toWebViewActivity(String url) {
        Intent intent = new Intent(activity(), WebViewActivity.class);
        intent.putExtra(BaiTiaoWebView.EXTRA_URL, url);
        activity().startActivity(intent);
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private long lastClickTime;

    public boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TimeUtils.DOUBLE_CLICK_TIME) {
            lastClickTime = time;
            return false;
        }
        return true;
    }
}
