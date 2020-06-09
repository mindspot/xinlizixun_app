package common.webview.custom;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;

import com.framework.component.ui.dailog.ActionSheetDialog;
import com.framework.page.Page;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import base.UserCenter;
import common.router.RequestCodeType;
import permission.PermissionListener;
import permission.PermissionTipInfo;
import permission.PermissionsUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 2018/3/19.
 * 网易客服
 */

public class WangYiServiceCall {
    private final Page page;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageArray;

    private static final String PATH = Environment.getExternalStorageDirectory() + "/DCIM";
    private String imageName;

    public WangYiServiceCall(Page page) {
        this.page = page;
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "image/*");
    }

    // For Android >=3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (acceptType.equals("image/*")) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                return;
            }
            mUploadMessage = uploadMsg;
            selectImage();
        } else {
            onReceiveValue();
        }
    }

    // For Android  >= 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // For Android  >= 5.0
    public void onShowFileChooser(ValueCallback<Uri[]> filePathCallback) {
        if (mUploadMessageArray != null) {
            mUploadMessageArray.onReceiveValue(null);
        }
        mUploadMessageArray = filePathCallback;
        openWriteFile();
    }

    private void openWriteFile() {
        PermissionTipInfo tip1 = PermissionTipInfo.getTip("读写权限");
        PermissionsUtil.requestPermission(page.context(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                selectImage();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                onReceiveValue();
                page.showToast("请先打开手机文件读写权限，否则无法使用该功能！");
            }
        }, tip1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void selectImage() {
        new ActionSheetDialog(page.activity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        PermissionTipInfo tip2 = PermissionTipInfo.getTip("读写权限", "相机");
                        PermissionsUtil.requestPermission(page.context(), new PermissionListener() {
                            @Override
                            public void permissionGranted(@NonNull String[] permission) {
                                openCamera();
                            }

                            @Override
                            public void permissionDenied(@NonNull String[] permission) {
                                onReceiveValue();
                                page.showToast("请先打开相机和手机文件读写权限，否则无法使用该功能！");
                            }
                        }, tip2, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                    }
                })
                .addSheetItem("从相册中选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        openAlbum();
                    }
                })
                .setNegativeButton("取消", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReceiveValue();
                    }
                }).show();
    }

    private void openAlbum() {
        if (!hasSDcard()) {
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //使用以上这种模式，并添加以上两句
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        page.startActivityForResult(intent, RequestCodeType.WEBVIEW_OPEN_FILE_CHOOSER_CHOOSE);
    }

    private boolean hasSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!flag) {
            page.showToast("请插入手机存储卡再使用本功能");
            onReceiveValue();
        }
        return flag;
    }

    private void openCamera() {
        if (!hasSDcard()) {
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageName = System.currentTimeMillis() + ".png";
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PATH, imageName)));
        page.startActivityForResult(intent, RequestCodeType.WEBVIEW_OPEN_FILE_CHOOSER_CAMERA);
    }

    private void handleFile(File file) {
        if (file.isFile()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (null == mUploadMessageArray) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                Uri[] uriArray = new Uri[]{uri};
                mUploadMessageArray.onReceiveValue(uriArray);
                mUploadMessageArray = null;
            } else {
                if (null == mUploadMessage) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
            }
        } else {
            onReceiveValue();
        }

    }

    void onReceiveValue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mUploadMessageArray != null) {
                mUploadMessageArray.onReceiveValue(null);
                mUploadMessageArray = null;
            }
        } else {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            onReceiveValue();
            return;
        }
        switch (requestCode) {
            case RequestCodeType.WEBVIEW_OPEN_FILE_CHOOSER_CAMERA:
                if (TextUtils.isEmpty(imageName)) {
                    CrashReport.postCatchedException(new Throwable("imageName is null, user" + UserCenter.instance().getUid()));
                    break;
                }
                File fileCamera = new File(PATH, imageName);
                handleFile(fileCamera);
                break;
            case RequestCodeType.WEBVIEW_OPEN_FILE_CHOOSER_CHOOSE:
                Uri uri = intent.getData();
                String absolutePath = getAbsolutePath(page.activity(), uri);
                File fileAlbum = new File(absolutePath);
                handleFile(fileAlbum);
                break;
            default:
                break;
        }
    }

    public String getAbsolutePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
