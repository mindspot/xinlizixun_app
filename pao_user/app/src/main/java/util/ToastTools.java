package util;

import android.content.Context;
import android.widget.Toast;

public abstract class ToastTools {
    private static Toast sToast;
    public static String Error30001 = "错误err_code:30001";

    private ToastTools() {
    }

    public static void init(Context context) {
        if (sToast == null) {
            sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    private static void check() {
        if (sToast == null) {
            throw new IllegalStateException("you must call ToastUtil.init(context) first");
        }
    }

    public static void showLong(Object object) {
        show(object, Toast.LENGTH_LONG);
    }


    public static void showShort(Object object) {
        show(object, Toast.LENGTH_SHORT);
    }

    public static void showError30001() {
        show("错误err_code:30001", Toast.LENGTH_SHORT);
    }

    private static void show(Object object, int length) {
        check();
        if (object != null) {
            sToast.setText(object.toString().replace("\"", ""));
            sToast.setDuration(length);
            sToast.show();
        }
    }

}
