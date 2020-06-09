package im.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.framework.http.bean.HttpError;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.sqlite.UserTabUtil;
import com.paopao.paopaostudio.R;

import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.List;

import base.BaseSelectPhotoActivity;
import butterknife.BindView;
import common.events.RefreshUIEvent;
import common.events.UIBaseEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.councilor.CouncilorItemBean;
import common.repository.http.entity.order.councilor.NowUserOrderDetailItemBean;
import common.repository.http.param.councilor.NowCouncilorOrderRequestBean;
import de.greenrobot.event.EventBus;
import im.IMHolder;
import module.app.MyApplication;
import module.order.CouncilorOrderDetailActivity;
import module.order.UseCouncilorListActivity;
import module.order.councilor.MyCouncilorOrderActivity;
import module.order.user.UserOrderDetailActivity;
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

    private int is_consultant;//1客户跟咨询师订单 0 咨询师 跟督导订单

    private boolean isLoadData = true;

    public static void newIntent(Context context, String userid) {
        newIntent(context, userid, true);
    }

    public static void newIntent(Context context, String userid, boolean isLoadData) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userid", userid);
        intent.putExtra("isLoadData", isLoadData);
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
            isLoadData = intent.getBooleanExtra("isLoadData", isLoadData);
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

        toolbar.getRight_btn_iv().setVisibility(View.GONE);

        if ("系统消息".equals(mUserid) || "admin".equals(mUserid) || "kf2".equals(mUserid) || mUserid.indexOf("kf") != -1) {
            isLoadData = false;
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
                if (is_consultant == 1) {
                    if (MyApplication.app.getCouncilorList() == null || MyApplication.app.getCouncilorList().isEmpty()) {
                        return;
                    }
                    if (MyApplication.app.getCouncilorList().size() == 1) {
                        UserOrderDetailActivity.newIntent(ChatActivity.this, MyApplication.app.getCouncilorList().get(0).getOrderInfo().getOrderNo(), false);
                    } else {
                        UseCouncilorListActivity.newIntentOrderList(ChatActivity.this);
                    }
                } else if (is_consultant == 0) {
                    if (MyApplication.app.getCouncilorItemList() == null || MyApplication.app.getCouncilorItemList().isEmpty()) {
                        return;
                    }
                    if (MyApplication.app.getCouncilorItemList().size() == 1) {//督导详情
                        CouncilorOrderDetailActivity.newIntent(ChatActivity.this,
                                MyApplication.app.getCouncilorItemList().get(0).getOrderNo(), 2, false);
                    } else {//督导列表
                        MyCouncilorOrderActivity.newIntent(ChatActivity.this);
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {
        if (!isLoadData) {
            return;
        }
        MyApplication.loadingDefault(activity());
        NowCouncilorOrderRequestBean bean = new NowCouncilorOrderRequestBean();
        bean.setEasemobsId(mUserid);
        HttpApi.app().getUserNowCouncilorOrder(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                if (StringUtil.isBlank(data)) {
                    toolbar.getRight_btn_iv().setVisibility(View.GONE);
                    return;
                }
                JSONObject jsonObject = JSONObject.parseObject(data);
                is_consultant = jsonObject.getIntValue("isConsultant");//1客户跟咨询师订单 0 咨询师 跟督导订单
                if (is_consultant == 0) {
                    List<CouncilorItemBean> list = ConvertUtil.toList(
                            jsonObject.getString("easemobOrderList"), CouncilorItemBean.class);
                    if (list == null || list.isEmpty()) {
                        toolbar.getRight_btn_iv().setVisibility(View.GONE);
                        return;
                    }
                    MyApplication.app.setCouncilorItemList(list);
                    toolbar.getRight_btn_iv().setVisibility(View.VISIBLE);
                } else if (is_consultant == 1) {
                    List<NowUserOrderDetailItemBean> list = ConvertUtil.toList(
                            jsonObject.getString("easemobOrderList"), NowUserOrderDetailItemBean.class);
                    if (list == null || list.isEmpty()) {
                        toolbar.getRight_btn_iv().setVisibility(View.GONE);
                        return;
                    }
                    MyApplication.app.setCouncilorList(list);
                    toolbar.getRight_btn_iv().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
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
}
