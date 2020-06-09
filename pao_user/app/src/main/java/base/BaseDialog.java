package base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import butterknife.ButterKnife;
import util.TimeUtils;

/**
 * Dialog 基类
 * <p>
 * 简化dialogg代码
 */
abstract public class BaseDialog extends Dialog {
    protected Context mContext;
    private View contentView;
    protected Page page;

    public BaseDialog(@NonNull Page page) {
        super(page.context(), R.style.AlertDialogStyle);
        this.page = page;
        initDialog(page.context());
    }

    public BaseDialog(@NonNull Page page, @StyleRes int themeResId) {
        super(page.context(), themeResId);
        this.page = page;
        initDialog(page.context());
    }


    public BaseDialog(@NonNull Context mContext, @StyleRes int themeResId) {
        super(mContext, themeResId);
        initDialog(mContext);
    }

    private void initDialog(@NonNull Context mContext) {
        this.mContext = mContext;
        contentView = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        setGravity(Gravity.CENTER);
        initView(contentView);
    }

    public BaseDialog builder() {//兼容build 构建方式
        return this;
    }

    public void setMinimumWidth(int minWidth) {
        contentView.setMinimumWidth(minWidth);
    }

    public void setGravity(int gravity) {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(gravity);
        }
    }

    public Display getDefaultDisplay() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay();
    }

    abstract protected void initView(View rootView);

    @LayoutRes
    abstract protected int getLayoutId();

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
