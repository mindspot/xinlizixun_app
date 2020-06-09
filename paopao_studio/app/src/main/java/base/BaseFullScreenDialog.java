package base;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.framework.page.Page;

import util.TimeUtils;

/**
 * Dialog 基类
 * <p>
 * 简化dialogg代码
 */
public abstract class BaseFullScreenDialog extends BaseDialog {

    public BaseFullScreenDialog(@NonNull Page page) {
        super(page);
    }


    public void showDialog() {
        show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
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
