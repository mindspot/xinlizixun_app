package com.framework.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationManager;
import android.provider.Settings;

/**************
 * 权限控制
 * @author admin
 *
 */
public class PermissionUtil {

    /********
     * 判断相机是否可用
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isCamareAviable(Context context) {
        boolean flag = false;

        try {
            Camera camera = Camera.open();
            camera.release();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
//		PackageManager pkm = context.getPackageManager();
//		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.CAMERA", context.getPackageName()));
//		return has_permission;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean gpsIsOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static void settingGPS(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
//        Intent GPSIntent = new Intent();  
//        GPSIntent.setClassName("com.android.settings",  
//                "com.android.settings.widget.SettingsAppWidgetProvider");  
//        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");  
//        GPSIntent.setData(Uri.parse("custom:3"));  
//        try {  
//            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();  
//        } catch (CanceledException e) {  
//            e.printStackTrace();  
//        }  
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtil.show(context, "无法打开设置页面，请手动设置");
        }
    }
}
