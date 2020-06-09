package util;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.framework.component.interfaces.NoDoubleClickListener;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

public class CommonPopupWindowUtil {

    public static List<Page.ActivityPage> activities = new ArrayList<>();
    public static PopupWindow popupWindow;
    public static TextView tv_no_connect;
    public static String TAG_POP_STYLE_NOCONNECT = "TAG_POP_STYLE_NOCONNECT";//无网络
    public static String TAG_POP_STYLE_NORECORD = "TAG_POP_STYLE_NORECORD";//无数据

    /***********
     * 默认页面
     */
    private static void showDefaultPopWin(final Page.ActivityPage activity, final IOnTouchRefresh touchRefresh, final int x, final int y, int width, int height, String style) {
        if (popupWindow == null) {
            View contentView = null;
            if (TAG_POP_STYLE_NOCONNECT.equals(style)) {
                contentView = LayoutInflater.from(activity.context()).inflate(R.layout.layout_pop_noconnect_style, null);
            } else if (TAG_POP_STYLE_NORECORD.equals(style)) {
                contentView = LayoutInflater.from(activity.context()).inflate(R.layout.layout_pop_norecord_style, null);
                tv_no_connect = (TextView) contentView.findViewById(R.id.tv_no_connect);
            }
            contentView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (touchRefresh != null) {
                        touchRefresh.refresh();
                    }
                }
            });

            popupWindow = new PopupWindow(activity.context());
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            popupWindow.setContentView(contentView);
//			popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//	        popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(false);
        }

        if (popupWindow.isShowing()) {
            return;
        }

        if (activity.hasWindowFocus()) {
            popupWindow.showAtLocation(((ViewGroup) activity.root()).getChildAt(0), Gravity.CENTER, x, y);
        } else {
            activity.setOnIWindowFocus(new Page.ActivityPage.IWindowFocus() {
                @Override
                public void focused() {
                    if (popupWindow == null || (popupWindow != null && popupWindow.isShowing())) {
                        return;
                    }

                    popupWindow.showAtLocation(((ViewGroup) activity.root()).getChildAt(0), Gravity.CENTER, x, y);
                }
            });
        }

    }

    private static void showDefaultPopWin(IOnTouchRefresh touchRefresh, String style) {
        int pos = activities.size() - 1;
        Page.ActivityPage activity = activities.get(pos);
        int screenH = com.framework.utils.ViewUtil.getScreenHeight(activity.context());
        int screenW = com.framework.utils.ViewUtil.getScreenWidth(activity.context());
        int titleHeight = (int) (screenH * 0.08);
        int buf = titleHeight + com.framework.utils.ViewUtil.getStatusBarH(activity.context());
        showDefaultPopWin(activity, touchRefresh, 0, buf, screenW, screenH - buf, style);
    }

    public static void showDefaultPopWin(Activity context, HttpError error, IOnTouchRefresh touchRefresh, String style) {
        if (context.isFinishing()) {
            return;
        }
        if (error == null || error.getErrCode() == HttpError.HTTP_ERROR_CONNECT || error.getErrCode() == HttpError.HTTP_ERROR_SERVER
                || error.getErrCode() == HttpError.HTTP_ERROR_TIMEOUT) {
            showDefaultPopWin(touchRefresh, style);
        }
    }

    /********
     * 关闭
     */
    public static void hidePopWin() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow = null;
    }


    public interface IOnTouchRefresh {
        void refresh();
    }


}
