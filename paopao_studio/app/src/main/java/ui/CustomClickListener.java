package ui;

import android.view.View;

import util.TimeUtils;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_user
 * @Package ui
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.04.21 上午 8:47
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public abstract class CustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = TimeUtils.DOUBLE_CLICK_TIME;

    public CustomClickListener() {
    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onClick();
            mLastClickTime = nowTime;
        }
    }

    protected abstract void onClick();
}