package common.router;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.framework.page.Page;
import com.socks.library.KLog;

/**
 * 页面路由
 *
 * @author Administrator
 * @date 2017/7/12
 */
public class CommandRouter {


    //****************** 处理ViewRouter的请求 ******************

    @NonNull
    public static CommandEntity convert(String cmdContent) {
        return CommandRegistry.convert(cmdContent);
    }


    //****************** 处理onActivityResult ******************

    public static void detached(@NonNull Page page) {
        CommandCallbackHandler.detachedPage(page);
    }

    public static boolean onActivityResult(@NonNull Page page, int requestCode, int resultCode, Intent data) {
        return CommandCallbackHandler.onActivityResult(page, requestCode, resultCode, data);
    }

    //****************** 处理onActivityResult ******************
    public static boolean onRequestPermissionsResult(@NonNull Page page, int requestCode, String[] permissions, int[] grantResults) {
        return CommandCallbackHandler.onRequestPermissionsResult(page, requestCode, permissions, grantResults);
    }

    //****************** 处理短信跳转与推送跳转 ******************

    /**
     * 推送跳转的信息
     */
    private static String jumpData = null;

    public static void setJumpData(String jumpDataStr) {
        jumpData = jumpDataStr;
        if (jumpData == null) {
            KLog.d("jumpData is null...");
        } else {
            KLog.d("jumpData=" + jumpData);
        }
    }

    public static void jumpByPushIfNeed(Page page) {
        if (jumpData == null) {
            // 没有推送跳转
            return;
        }

        String convertData = jumpData;
        // 清除推送跳转的数据
        jumpData = null;

        new CommandRequest(convertData).setPage(page).router();
    }

}
