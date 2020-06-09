package permission;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.Arrays;


public class PermissionTipInfo implements Serializable {
    private static final String DEFAULT_TITLE = "帮助";
    private static final String DEFAULT_CONTENT = "当前应用缺少必要权限。\n \n 请点击 \"设置\"-\"权限\"-打开所需权限。";
    private static final String DEFAULT_CANCEL = "取消";
    private static final String DEFAULT_ENSURE = "设置";

    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private String cancel;  //取消按钮文本
    private String ensure;  //确定按钮文本


    public static PermissionTipInfo getDefault() {
        return new PermissionTipInfo(DEFAULT_TITLE, DEFAULT_CONTENT, DEFAULT_CANCEL, DEFAULT_ENSURE);
    }

    public static PermissionTipInfo getTip(@NonNull String... permissions) {
        String content = String.format("当前应用缺少%s权限。\n \n 请点击 \"设置\"-\"权限\"-打开所需权限。", Arrays.toString(permissions));
        return new PermissionTipInfo(DEFAULT_TITLE, content, DEFAULT_CANCEL, DEFAULT_ENSURE);
    }

    public PermissionTipInfo(@Nullable String title, @Nullable String content, @Nullable String cancel, @Nullable String ensure) {
        this.title = title;
        this.content = content;
        this.cancel = cancel;
        this.ensure = ensure;
    }

    public String getTitle() {
        return TextUtils.isEmpty(title) ? DEFAULT_TITLE : title;
    }

    public String getContent() {
        return TextUtils.isEmpty(content) ? DEFAULT_CONTENT : content;
    }

    public String getCancel() {
        return TextUtils.isEmpty(cancel) ? DEFAULT_CANCEL : cancel;
    }

    public String getEnsure() {
        return TextUtils.isEmpty(ensure) ? DEFAULT_ENSURE : ensure;
    }
}