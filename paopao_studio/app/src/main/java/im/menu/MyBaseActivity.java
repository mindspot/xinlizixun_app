package im.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.framework.page.Page;
import com.framework.utils.CustomToast;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.hyphenate.easeui.ui.EaseBaseActivity;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package im.menu
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.04.29 下午 6:07
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class MyBaseActivity extends EaseBaseActivity implements Page {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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
}
