package im.chat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.framework.utils.ToastUtil;
import com.framework.utils.ViewUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.paopao.paopaostudio.R;

import common.events.RefreshUIEvent;
import common.events.UIBaseEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.param.chat.ChatStatusRequestBean;
import common.repository.share_preference.SPApi;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import im.menu.VideoCallActivity;
import im.menu.VoiceCallActivity;
import module.app.MyApplication;
import module.order.CouncilorOrderDetailActivity;
import module.order.user.UserOrderDetailActivity;
import permission.PermissionListener;
import permission.PermissionTipInfo;
import permission.PermissionsUtil;
import util.TimeUtils;
import util.ToastTools;

/**
 * Created by hpzhan on 2019/12/11.
 */

public class MyEaseChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper, Page {

    private static final int ITEM_TAKE_PICTURE = 1;
    private static final int ITEM_CAMCRE_PICTURE = 2;
    private static final int ITEM_VOICE = 3;
    private static final int ITEM_VIDEO = 4;

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        setChatFragmentHelper(this);
        inputMenu.getPrimaryMenu().getChangeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionTipInfo tip1 = PermissionTipInfo.getTip("读写权限", "录音");
                PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        inputMenu.getPrimaryMenu().onSetModeVoice();
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        ToastTools.init(getContext());
                        ToastTools.showShort("请打开录音、手机文件读写权限，否则无法使用该功能！");
                    }
                }, tip1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
            }
        });
    }

    @Override
    protected void registerExtendMenuItem() {
        if ("系统消息".equals(toChatUsername) || "admin".equals(toChatUsername)) {
            inputMenu.setVisibility(View.GONE);
            return;
        }
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.ease_chat_takepic_selector, ITEM_TAKE_PICTURE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.ease_chat_camcre_selector, ITEM_CAMCRE_PICTURE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.ease_chat_voice_selector, ITEM_VOICE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.ease_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        Log.d("IM-onMessage", message.getBody().toString());
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_TAKE_PICTURE:
                PermissionTipInfo tip1 = PermissionTipInfo.getTip("读写权限");
                PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_CHAT_SELECT_IMAGE));
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        ToastTools.init(getContext());
                        ToastTools.showShort("请先打开手机文件读写权限，否则无法使用该功能！");
                    }
                }, tip1, Manifest.permission.READ_EXTERNAL_STORAGE);
                return true;
            case ITEM_CAMCRE_PICTURE:
                PermissionTipInfo tip2 = PermissionTipInfo.getTip("读写权限", "相机");
                PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_CHAT_SELECT_CAMCRE));
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        ToastTools.init(getContext());
                        ToastTools.showShort("请先打开相机和手机文件读写权限，否则无法使用该功能！");
                    }
                }, tip2, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                return true;
            case ITEM_VOICE:
                getPermission(0);
                return true;
            case ITEM_VIDEO:
                getPermission(1);
                return true;

        }
        return false;
    }

    public void getPermission(int type) {
        MyApplication.loadingDefault(getActivity());
        ChatStatusRequestBean bean = new ChatStatusRequestBean();
        bean.setServiceType(type);
        bean.setUserEasemobId(toChatUsername);
        bean.setConsultantEasemobId(SPApi.user().getUserImAccount());
        HttpApi.app().getChatInfo(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (type == 0) {
                    gotoVoice();
                } else {
                    gotoVideo();
                }
                MyApplication.hideLoading();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void gotoVoice() {
        PermissionTipInfo tip3 = PermissionTipInfo.getTip("读写权限", "相机", "录音");
        PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                            .putExtra("isComingCall", false));
                    inputMenu.hideExtendMenuContainer();
                }
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showToast("请打开相机、录音、手机文件读写权限，否则无法使用该功能！");
            }
        }, tip3, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
    }

    public void gotoVideo() {
        PermissionTipInfo tip4 = PermissionTipInfo.getTip("读写权限", "相机", "录音");
        PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                            .putExtra("isComingCall", false));
                    inputMenu.hideExtendMenuContainer();
                }
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showToast("请打开相机、录音、手机文件读写权限，否则无法使用该功能！");
            }
        }, tip4, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

    @Override
    public void onOrderInfoMessageClick(EMMessage message) {
        if (isDoubleClick()) {
            return;
        }
        try {
            String order = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_ORDER_ORDER_NO);
            int orderType = message.getIntAttribute(EaseConstant.MESSAGE_ATTR_ORDER_ORDER_TYPE);
            if (orderType == 0) {
                UserOrderDetailActivity.newIntent(getContext(), order);
            } else {
                CouncilorOrderDetailActivity.newIntent(getContext(), order, 2);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    private long lastClickTime;

    public boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TimeUtils.DOUBLE_CLICK_TIME) {
            lastClickTime = time;
            return false;
        }
        return true;
    }

    public void sendImgMessage(String imageUrl) {
        sendImageMessage(imageUrl);
    }

    @Override
    public void endSendMessage(EMMessage message) {
        message.setAttribute(EaseConstant.EXTRA_USER_NICKNAME, IMHolder.getInstance().getUserMyName());
        message.setAttribute(EaseConstant.EXTRA_USER_AVATER, IMHolder.getInstance().getUserPhoto());
        super.endSendMessage(message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public Intent getIntent() {
        return getActivity().getIntent();
    }

    @Override
    public ComponentName startService(Intent service) {
        return getActivity().startService(service);
    }

    @Override
    public void showToast(String message) {
        if (!StringUtil.isBlank(message) && !ViewUtil.isFinishedPage(this)) {
            ToastUtil.show(getActivity(), message);
        }
    }

    @Override
    public <T extends View> T $(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }

    @Override
    public View root() {
        return rootView;
    }
}
