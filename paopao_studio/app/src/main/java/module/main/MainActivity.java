package module.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.NetUtils;
import com.paopao.paopaostudio.BuildConfig;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import base.UserCenter;
import butterknife.BindView;
import common.events.HttpEvent;
import common.events.OrderMsgNumEvent;
import common.events.TabEvent;
import common.events.UploadHWPushTokenEvent;
import common.repository.bean.TabEntity;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.UpdateAppResponseBean;
import common.repository.http.entity.order.OrderMsgNumResponseBean;
import common.repository.http.param.app.UploadAddressRequestBean;
import de.greenrobot.event.EventBus;
import im.HMSPushHelper;
import im.IMHolder;
import im.chatlist.MyEaseConversationFragment;
import module.app.MyApplication;
import module.dialog.TipDialog;
import module.main.center.MyCenterFragment;
import module.main.counsel.CounselFragment;
import module.main.order.OrderFragment;
import module.update.UpdateView;
import permission.PermissionListener;
import permission.PermissionTipInfo;
import permission.PermissionsUtil;
import util.NotificationHelper;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"订单", "消息", "督导", "我的"};
    private int[] mIconUnselectIds = {R.mipmap.order
            , R.mipmap.information, R.mipmap.supervisor, R.mipmap.mine};
    private int[] mIconSelectIds = {R.mipmap.order_click
            , R.mipmap.information_click, R.mipmap.supervisor_click, R.mipmap.mine_click};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private Fragment lastFragment;
    private Map<Integer, Fragment> fragmentMap = new HashMap<>(4);

    public static final int TAB_INDEX_ORDER = 0;
    public static final int TAB_INDEX_MSG = 1;
    public static final int TAB_INDEX_DUDAO = 2;
    public static final int TAB_INDEX_MY = 3;

    public static final int GPS_REQUEST_CODE = 10011;
    private NotificationHelper notificationHelper;

    private int uploadMsgSize = 1;

    private UpdateAppResponseBean updateApkBean;

    private TipDialog tipDialog;

    @Override
    protected void onResume() {
        super.onResume();
        getOrderMsgNum();
        if (updateApkBean != null) {
            if (updateApkBean.getForceUpdate() != 1) {
                return;
            }
            if (tipDialog != null && tipDialog.isShowing()) {
                return;
            }
            compareVersion(updateApkBean);
        } else {
            tabLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (updateApkBean == null)
                        getUpdateApp();
                }
            }, 5000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), MainActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        tabLayout.setCurrentTab(0);
        changeFragment(0);

        if (UserCenter.instance().getLoginStatus()) {
            IMHolder.getInstance().toLoginIM(UserCenter.instance().getUserInfo().getEasemobId(), "0000");
            //注册一个监听连接状态的listener
            EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        }

        notificationHelper = new NotificationHelper(this);
    }

    @Override
    public void initListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                changeFragment(i);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage msg : list) {
                    if (!msg.getFrom().equals(MyApplication.app.getNowUserId())) {
                        EMTextMessageBody txtBody = (EMTextMessageBody) msg.getBody();
                        String nickName = msg.getStringAttribute(EaseConstant.EXTRA_USER_NICKNAME, "");
                        notificationHelper.show(nickName + ":" + txtBody.getMessage(), msg.getFrom());
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

        getLocation();
        //getAddressInfo();
        getPermission();
    }

    public void getPermission() {
        PermissionTipInfo tip1 = PermissionTipInfo.getTip("读写权限", "录音", "相机");
        PermissionsUtil.requestPermission(context(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showToast("请打开录音、手机文件读写权限，否则某些功能无法正常使用！");
            }
        }, tip1, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
    }

    public void changeFragment(int tabId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }

        Fragment currentFragment;
        if (!fragmentMap.containsKey(TAB_INDEX_MSG)) {
            currentFragment = createFragment(TAB_INDEX_MSG);
            transaction.add(R.id.fl_change, currentFragment);
            fragmentMap.put(TAB_INDEX_MSG, currentFragment);
        }
        if (fragmentMap.containsKey(tabId)) {
            currentFragment = fragmentMap.get(tabId);
            transaction.show(currentFragment);
            if (tabId == TAB_INDEX_ORDER) {
                getOrderMsgNum();
            }
        } else {
            currentFragment = createFragment(tabId);
            transaction.add(R.id.fl_change, currentFragment);
            fragmentMap.put(tabId, currentFragment);
        }
        transaction.commitAllowingStateLoss();

        lastFragment = currentFragment;
        if (lastFragment instanceof FragmentPage) {
            ((FragmentPage) lastFragment).onShow();
        }
    }

    private Fragment createFragment(int tabId) {
        switch (tabId) {
            case TAB_INDEX_MSG:
                return new MyEaseConversationFragment();
            case TAB_INDEX_ORDER:
                return new OrderFragment();
            case TAB_INDEX_DUDAO:
                return new CounselFragment();
            case TAB_INDEX_MY:
                return new MyCenterFragment();
            default:
                return null;
        }

    }

    @Override
    public void loadData() {
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(context(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEventMainThread(HttpEvent event) {
        if (event.getCode() == HttpEvent.HTTP_ERROR_NEED_GET_TOKEN) {
            UserCenter.instance().toLogin(this);
        }
    }

    public void onEventMainThread(TabEvent event) {
        if (event.getType() == TabEvent.TYPE_CHANGE_TAB) {
            changeTab(event.getTabId());
        } else if (event.getType() == TabEvent.TYPE_UPLOAD_TAB_MSG_NUM) {
            setHotMsg(event.getTabId(), EMClient.getInstance().chatManager().getUnreadMessageCount());
        }
    }

    public void onEventMainThread(UploadHWPushTokenEvent event) {
        // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this);
        uploadMessage();
    }

    public void uploadMessage() {
        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_UPLOAD_TAB_MSG_NUM, MainActivity.TAB_INDEX_MSG));
                uploadMsgSize++;
                if (uploadMsgSize < 4) {
                    uploadMessage();
                }
            }
        }, uploadMsgSize * 500);
    }

    public void setHotMsg(int tabIndex, int msgNum) {
        if (msgNum == 0) {
            tabLayout.hideMsg(tabIndex);
            return;
        }
        tabLayout.showMsg(tabIndex, msgNum);
        tabLayout.getMsgView(tabIndex).setBackgroundColor(getResources().getColor(R.color.theme_color));
    }

    public void changeTab(int tabIndex) {
        tabLayout.setCurrentTab(tabIndex);
        changeFragment(tabIndex);
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Log.d("IM-STATUS", "显示帐号已经被移除");
                        if (IMHolder.getInstance().isLoginEM()) {
                            showToast("账号异常，请重新登录");
                            UserCenter.instance().logout(MainActivity.this);
                        }
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Log.d("IM-STATUS", "显示帐号在其他设备登录");
                        if (IMHolder.getInstance().isLoginEM()) {
                            showToast("帐号在其他设备登录，请重新登录");
                            UserCenter.instance().logout(MainActivity.this);
                        }
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            Log.d("IM-STATUS", "连接不到聊天服务器");
                        } else {
                            //当前网络不可用，请检查网络设置
                            Log.d("IM-STATUS", "当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }

    public void getOrderMsgNum() {
        HttpApi.app().getOrderMsgNum(this, new HttpCallback<OrderMsgNumResponseBean>() {
            @Override
            public void onSuccess(int code, String message, OrderMsgNumResponseBean data) {
                int num = 0;
                if (data != null) {
                    num = data.getUnConsultationOrderNum() + data.getUnConsultantSupervisorOrderNum() + data.getUnSupervisorOrderNum();
                }
                setHotMsg(TAB_INDEX_ORDER, num);
                EventBus.getDefault().post(new OrderMsgNumEvent(data));
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }

    public void getLocation() {
        PermissionTipInfo tip = PermissionTipInfo.getTip("位置服务");
        PermissionsUtil.requestPermission(context(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                openGPSSEtting();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showToast("打开位置服务，保证所有服务正常使用...");
            }
        }, tip, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            MyApplication.app.getLocation();
        } else {
//            new AlertDialog.Builder(this).setTitle("打开位置服务")
//                    .setMessage("打开位置服务，保证所有服务正常使用...")
//                    //  取消选项
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            getLocation();
//                            // 关闭dialog
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    //  确认选项
//                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //跳转到手机原生设置页面
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivityForResult(intent, GPS_REQUEST_CODE);
//                        }
//                    })
//                    .setCancelable(false)
//                    .show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            if (checkGpsIsOpen()) {
                MyApplication.app.getLocation();
            }
        }
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private boolean isUploadAddress = false;

    public void getAddressInfo() {
        if (isUploadAddress) {
            return;
        }
        HttpApi.app().getAddressInfo(this, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                uploadAddressInfo(data);
                isUploadAddress = true;
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }

    public void uploadAddressInfo(String address) {
        UploadAddressRequestBean bean = new UploadAddressRequestBean();
        HttpApi.app().uploadAddressInfo(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
            }

            @Override
            public void onFailed(HttpError error) {
                isUploadAddress = false;
            }
        });
    }

    public void getUpdateApp() {
        HttpApi.app().getUpdateAppInfo(this, new HttpCallback<UpdateAppResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UpdateAppResponseBean data) {
                if (code == 0 && data != null) {
                    compareVersion(data);
                }
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }

    public void compareVersion(UpdateAppResponseBean data) {
        updateApkBean = data;
        if (BuildConfig.VERSION_NAME.equals(data.getAppVersion())) {
            return;
        }
        try {
            int oldVer = Integer.parseInt(BuildConfig.VERSION_NAME.replace(".", ""));
            int newVer = Integer.parseInt(data.getAppVersion().replace(".", ""));
            if (oldVer > newVer) {
                return;
            }
            tipDialog = new TipDialog(MainActivity.this);
            tipDialog.setTitle("版本更新");
            tipDialog.setConfirm("立即更新");
            tipDialog.setContent(data.getContent());
            tipDialog.setCancelable(false);
            if (data.getForceUpdate() != 1) {
                tipDialog.setCancle("暂不更新");
                tipDialog.setCancelable(true);
            }
            tipDialog.setOnItemClickListener(new TipDialog.OnItemClickListener() {
                @Override
                public void confirm() {
                    uploadApk(data.getDownloadAddress(), data.getForceUpdate() == 1);
                }

                @Override
                public void cancle() {
                }
            });
            tipDialog.showMyDialog();
        } catch (Exception ex) {
        }
    }

    public void uploadApk(String url, boolean isForceUpdate) {
        UpdateView updateView = new UpdateView(this, new UpdateView.UpdateListener() {
            @Override
            public void onUpdateFailed() {
                if (isForceUpdate) {
                    showToast("更新失败，请重新尝试");
                    activity().finish();
                } else {
                    showToast("更新失败");
                }
            }

            @Override
            public void onUpdateComplete() {
                showToast("已更新完成");
            }
        });
        updateView.downloadNewVersionApk(url, "latest.apk");
    }
}
