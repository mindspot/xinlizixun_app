package com.framework.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;


/**
 * Created by User on 2018/7/17.
 */

public enum CustomToast {
    INSTANCE;// 实现单例
    private Toast mToast;
    private TextView mTvToast;

    public void showToast(Context ctx, String content, boolean isCenter) {
        if (mToast == null) {
            mToast = new Toast(ctx);
            /**
             * 设置toast显示的位置，这是居中
             */
            if (isCenter) {
                mToast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 300);
            }
            /**
             * 设置toast显示的时长
             */
            mToast.setDuration(Toast.LENGTH_SHORT);
            /**
             * 自定义样式，自定义布局文件
             */
            View _root = LayoutInflater.from(ctx).inflate(R.layout.toast_custom_common, null);
            mTvToast = (TextView) _root.findViewById(R.id.tvCustomToast);
            mToast.setView(_root);
        }
        /**
         * 设置文本
         */
        mTvToast.setText(content);
        /**
         * 展示toast
         */
        mToast.show();
    }

    public void showToast(Context ctx, int stringId) {
        showToast(ctx, ctx.getString(stringId), false);
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
            mTvToast = null;
        }
    }
}

