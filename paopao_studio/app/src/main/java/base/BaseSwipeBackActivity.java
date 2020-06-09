package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.framework.page.Page;
import com.framework.utils.CustomToast;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.paopao.paopaostudio.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import common.repository.http.HttpApi;
import common.router.CommandRouter;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import util.CommonPopupWindowUtil;


/**
 * activity的基类
 *
 * @author Administrator
 */
public abstract class BaseSwipeBackActivity extends SwipeBackActivity implements Page.ActivityPage {

    /**
     * 设置是否添加到Activity管理列表，以免启动页等弹出Dialog
     */
    public boolean canAdd = true;
    private Unbinder unbinder;

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
        if (canAdd) {
            CommonPopupWindowUtil.activities.add(this);
        }
//        BiPageRecorder.add(this);
        // 虚拟导航栏 背景色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);

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
//        if (CommandRouter.onActivityResult(this, requestCode, resultCode, data)) {
//            return;
//        }
        CommandRouter.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPIWrapper.onActivityResult(this, requestCode, resultCode, data);
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
//        if (getClass() == MainActivity.class) {
//            Intent intent = new Intent(this, SelectURLActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        } else {
//            finish();
//        }
        finish();
    }

}
