package im.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.framework.http.bean.HttpError;
import com.framework.utils.StringUtil;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.paopao.paopaouser.R;

import org.devio.takephoto.model.TResult;

import java.io.File;

import base.BaseSelectPhotoActivity;
import butterknife.BindView;
import common.events.RefreshUIEvent;
import common.events.UIBaseEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.param.user.ChatUserStatusRequestBean;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import module.app.MyApplication;
import ui.CustomClickListener;
import ui.title.ToolBarTitleView;
import util.AndroidBug5497Workaround;
import util.TakePhotoHelper;
import util.ToastTools;

public class ChatActivity extends BaseSelectPhotoActivity {

    @BindView(R.id.chat_activity_toolbar)
    ToolBarTitleView toolbar;

    private String mUserid;
    private MyEaseChatFragment myEaseChatFragment;

    private TakePhotoHelper takePhotoHelper;

    private boolean isLineStatus = true;

    public static void newIntent(Context context, String userid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userid", userid);
        context.startActivity(intent);
    }

    public static void newIntent(Context context, String userid, String nickname, String avater) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userid", userid);
        context.startActivity(intent);
        UserTabUtil.uploadUser(userid, nickname, avater);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(this);
        Intent intent = getIntent();
        if (intent != null) {
            mUserid = intent.getStringExtra("userid");
            String pushUserid = intent.getStringExtra("pushUserid");
            if (!StringUtil.isBlank(pushUserid)) {
                mUserid = pushUserid;
            }
        }
        if (mUserid == null || "".equals(mUserid)) {
            ToastTools.init(this);
            ToastTools.showShort("数据异常！");
            finish();
            return;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        takePhotoHelper = new TakePhotoHelper(this, getTakePhoto());
        takePhotoHelper.setShear(false);
        toolbar.setTitle(IMHolder.getInstance().getUserNickname(mUserid));
        //use EaseChatFratFragment
        myEaseChatFragment = new MyEaseChatFragment();
        //pass parameters to chat fragment
        Bundle bundle = new Bundle();
        bundle.putString(EaseConstant.EXTRA_USER_ID, String.valueOf(mUserid));
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        myEaseChatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.chat_activity_container, myEaseChatFragment).commit();

        if (mUserid.indexOf("ps") != -1) {
            getLineStatus();
        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightClickListener(new CustomClickListener() {
            @Override
            protected void onClick() {
                if (!isLineStatus) {
                    remindLine();
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        showToast(msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        final File photoFile = takePhotoHelper.getCompressImageUrl(result.getImage());
        if (myEaseChatFragment != null && photoFile != null) {
            myEaseChatFragment.sendImgMessage(photoFile.getAbsolutePath());
        }
    }

    public void onEventMainThread(RefreshUIEvent event) {
        if (event.getType() == UIBaseEvent.EVENT_CHAT_SELECT_IMAGE)
            takePhotoHelper.openAlbum();
        else if (event.getType() == UIBaseEvent.EVENT_CHAT_SELECT_CAMCRE)
            takePhotoHelper.openCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.app.setNowUserId(mUserid);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.app.setNowUserId("");
    }

    public void getLineStatus() {
        ChatUserStatusRequestBean bean = new ChatUserStatusRequestBean();
        bean.setEasemobId(mUserid);
        HttpApi.app().getChatUserLineStatus(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (!"online".equals(data)) {
                    toolbar.setRightButtonText("提醒上线");
                    toolbar.getRight_btn_tv().setTextColor(activity().getResources().getColor(R.color.theme_color));
                    isLineStatus = false;
                } else {
                    toolbar.setRightButtonText("");
                    isLineStatus = true;
                }
            }

            @Override
            public void onFailed(HttpError error) {

            }
        });
    }

    public void remindLine() {
        MyApplication.loadingDefault(activity());
        ChatUserStatusRequestBean bean = new ChatUserStatusRequestBean();
        bean.setEasemobId(mUserid);
        HttpApi.app().remindChatUserLine(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                if (code == 0) {
                    showToast("提醒成功");
                    return;
                }
                showToast(message);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }
}
