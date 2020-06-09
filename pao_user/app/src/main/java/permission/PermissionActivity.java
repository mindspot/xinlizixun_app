package permission;
/**
 * Created by dfqin on 2017/1/22.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class PermissionActivity extends AppCompatActivity {

    public static final String EXTRA_PERMISSION = "permission";
    public static final String EXTRA_LISTENER_KEY = "key";
    public static final String EXTRA_SHOW_TIP = "showTip";
    public static final String EXTRA_TIP = "tip";


    private static final int PERMISSION_REQUEST_CODE = 64;
    private boolean isRequireCheck = false;

    private String[] permission;
    private String key;
    private boolean showTip;
    private PermissionTipInfo tipInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSION)) {
            finish();
            return;
        }

        isRequireCheck = true;
        permission = getIntent().getStringArrayExtra(EXTRA_PERMISSION);
        key = getIntent().getStringExtra(EXTRA_LISTENER_KEY);
        showTip = getIntent().getBooleanExtra(EXTRA_SHOW_TIP, true);

        Serializable ser = getIntent().getSerializableExtra(EXTRA_TIP);
        if (ser == null) {
            tipInfo = PermissionTipInfo.getDefault();
        } else {
            tipInfo = (PermissionTipInfo) ser;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            if (PermissionsUtil.hasPermission(this, permission)) {
                permissionsGranted();
            } else {
                requestPermissions(permission); // 请求权限,回调时会触发onResume
                isRequireCheck = false;
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 请求权限兼容低版本
    private void requestPermissions(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //部分厂商手机系统返回授权成功时，厂商可以拒绝权限，所以要用PermissionChecker二次判断
        if (requestCode == PERMISSION_REQUEST_CODE
                && PermissionsUtil.isGranted(grantResults)
                && PermissionsUtil.hasPermission(this, permissions)) {
            permissionsGranted();
        } else if (showTip) {
            showMissingPermissionDialog();
        } else { //不需要提示用户
            permissionsDenied();
        }
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        new AlertDialog.Builder(PermissionActivity.this)
                .setTitle(tipInfo.getTitle())
                .setMessage(tipInfo.getContent())
                .setNegativeButton(tipInfo.getCancel(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionsDenied();
                    }
                })
                .setPositiveButton(tipInfo.getEnsure(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JumpToSysmtePermissionManager.goToSetting(PermissionActivity.this);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void permissionsDenied() {
        PermissionListener listener = PermissionsUtil.fetchListener(key);
        if (listener != null) {
            listener.permissionDenied(permission);
        }
        finish();
    }

    // 全部权限均已获取
    private void permissionsGranted() {
        PermissionListener listener = PermissionsUtil.fetchListener(key);
        if (listener != null) {
            listener.permissionGranted(permission);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        PermissionsUtil.fetchListener(key);
        super.onDestroy();
    }

}
