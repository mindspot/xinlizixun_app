package com.framework.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;

public class ToastUtil {

    /**
     * 显示提示信息
     */
    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示提示信息
     */
    public static void showLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 在中部显示自定义UI的提示信息
     */
    public static void showCenter(Context context, String text) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);// 设置toast显示在屏幕的位置

        // 设置toast文本，把设置好的布局传进来
        TextView textView = (TextView) View.inflate(context, R.layout.toast_center_view, null);
        textView.setText(text);
        toast.setView(textView);

        toast.show();
    }

}
