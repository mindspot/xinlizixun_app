package im;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.framework.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;

import java.io.File;
import java.util.List;

import common.events.ServiceStopEvent;
import common.events.ServiceWillStopEvent;
import common.events.TabEvent;
import common.events.UploadHWPushTokenEvent;
import common.repository.share_preference.SPApi;
import de.greenrobot.event.EventBus;
import im.chat.ChatActivity;
import module.main.MainActivity;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: dd_sungu
 * @Package com.sungu.huanxin
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2019.12.11 上午 11:21
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2019
 */
public class IMHolder {
    private static final String TAG = "IM-" + IMHolder.class.getSimpleName();

    private static IMHolder imHolder;

    int i = 0;

    private boolean isLoginEM = false;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    public void setLoginEM(boolean loginEM) {
        i = 0;
        isLoginEM = loginEM;
    }

    public static IMHolder getInstance() {
        if (imHolder == null)
            imHolder = new IMHolder();
        return imHolder;
    }

    public boolean isLoginEM() {
        return isLoginEM;
    }

    public void toLoginIM(String userid, String pwd) {
        if (StringUtil.isBlank(userid)) {
            return;
        }
        if (i > 3) {
            i = 0;
            return;
        }
        if (isLoginEM) {
            return;
        }
        //传入在应用（appkey）下注册的IM用户的账号和密码，用于登录环信服务器
        EMClient.getInstance().login(userid, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d(TAG, "环信登录成功:" + userid);
                isLoginEM = true;
                EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_UPLOAD_TAB_MSG_NUM, MainActivity.TAB_INDEX_MSG));
                EventBus.getDefault().post(new UploadHWPushTokenEvent());
                EaseUI.getInstance().setLoginUserId(userid);
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "环信登录进度：" + progress + " 状态：" + status);
            }

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "环信登录失败：" + code + " message：" + message + " " + userid);
                i++;
                toLoginIM(userid, pwd);
            }
        });
    }

    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    public String getUserMyName() {
        String name = SPApi.user().getLoginRealName();
        String phone = SPApi.user().getUserPhone();
        if (StringUtil.isBlank(name) && !StringUtil.isBlank(phone)) {
            name = StringUtil.changeMobile(phone);
        }
        return name;
    }

    public String getUserPhoto() {
        return SPApi.user().getUserPhoto();
    }

    // 封装了一下
    public void setUser(String username, String nickname, String avater) {
        UserTabUtil.uploadUser(username, nickname, avater);
    }

    public String getUserNickname(String username) {
        return UserTabUtil.getUserNickname(username);
    }

    public String getUserPhoto(String username) {
        return UserTabUtil.getUserImage(username);
    }

    // 封装了一下
    public void gotoChat(Context context, String username, String nickname, String avater) {
        gotoChat(context, username, nickname, avater, true);
    }

    // 封装了一下
    public void gotoChat(Context context, String username, String nickname, String avater, boolean isLoadData) {
        if (SPApi.user().getUserImAccount().equals(username)) {
            ToastUtil.show(context, "不能跟自己私聊！");
            return;
        }
        UserTabUtil.uploadUser(username, nickname, avater);
        ChatActivity.newIntent(context, username, isLoadData);
    }

    private final String SERVICE_STOP = "1";
    private final String SERVICE_5_MINUTE = "2";

    public void disposeMessage(List<EMMessage> list) {
        for (EMMessage msg : list) {
            String code = msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_MSG_CODE, "");
            if (msg.getType() == EMMessage.Type.TXT && !StringUtil.isBlank(code) && (isVideoCalling || isVoiceCalling)) {
                EMTextMessageBody txtBody = (EMTextMessageBody) msg.getBody();
                Log.d("disposeMessage", "code:" + code + " msg:" + txtBody);
                if (SERVICE_STOP.equals(code)) {
                    EventBus.getDefault().post(new ServiceStopEvent(txtBody.getMessage()));
                } else if (SERVICE_5_MINUTE.equals(code)) {
                    EventBus.getDefault().post(new ServiceWillStopEvent(txtBody.getMessage()));
                }
            }
        }
    }

    public void showBigImage(Page page, File file, String url) {
        Intent intent = new Intent(page.activity(), EaseShowBigImageActivity.class);
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
            page.startActivity(intent);
        } else if (!StringUtil.isBlank(url)) {
            intent.putExtra("imagePath", url);
            page.startActivity(intent);
        }
    }
}
