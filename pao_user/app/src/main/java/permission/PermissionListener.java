package permission;

import android.support.annotation.NonNull;

/**
 * Created by dfqin on 2017/1/20.
 */

public interface PermissionListener {

    /**
     * 通过授权
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     */
    void permissionDenied(@NonNull String[] permission);
}