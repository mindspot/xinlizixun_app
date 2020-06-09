package permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;

import java.util.HashMap;

/**
 * Created by dfqin on 2017/1/20.
 */

public class PermissionsUtil {

    public static final String TAG = "PermissionGrantor";
    private static final HashMap<String, PermissionListener> LISTENER_MAP = new HashMap<>();

    /**
     * 申请授权，当用户拒绝时，会显示默认一个默认的Dialog提示用户
     *
     * @param context
     * @param listener
     * @param permission 要申请的权限
     */
    public static void requestPermission(@NonNull Context context, @NonNull PermissionListener listener,
                                         @NonNull String... permission) {
        requestPermission(context, listener, true, null, permission);
    }

    public static void requestPermission(@NonNull Context context, @NonNull PermissionListener listener,
                                         @Nullable PermissionTipInfo tip, @NonNull String... permission) {
        requestPermission(context, listener, true, tip, permission);
    }

    /**
     * 申请授权，当用户拒绝时，可以设置是否显示Dialog提示用户，也可以设置提示用户的文本内容
     *
     * @param context
     * @param listener
     * @param permission 需要申请授权的权限
     * @param showTip    当用户拒绝授权时，是否显示提示
     * @param tip        当用户拒绝时要显示Dialog设置
     */
    public static void requestPermission(@NonNull Context context, @NonNull PermissionListener listener,
                                         boolean showTip, @Nullable PermissionTipInfo tip, @NonNull String... permission) {
        if (hasPermission(context, permission)) {
            listener.permissionGranted(permission);
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.permissionGranted(permission);
            return;
        }
        final String listenerKey = String.valueOf(System.currentTimeMillis());
        LISTENER_MAP.put(listenerKey, listener);

        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(PermissionActivity.EXTRA_PERMISSION, permission);
        intent.putExtra(PermissionActivity.EXTRA_LISTENER_KEY, listenerKey);
        intent.putExtra(PermissionActivity.EXTRA_SHOW_TIP, showTip);
        intent.putExtra(PermissionActivity.EXTRA_TIP, tip);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }


    /**
     * 判断权限是否授权
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (permissions.length == 0) {
            return false;
        }
        for (String per : permissions) {
            int result = PermissionChecker.checkSelfPermission(context, per);
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一组授权结果是否为授权通过
     */
    public static boolean isGranted(@NonNull int... grantResult) {
        if (grantResult.length == 0) {
            return false;
        }
        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    static PermissionListener fetchListener(String key) {
        return LISTENER_MAP.remove(key);
    }


}