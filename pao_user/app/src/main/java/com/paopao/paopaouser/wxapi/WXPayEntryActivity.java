package com.paopao.paopaouser.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import common.events.PayResultEvent;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;

/**
 * Created by hpzhan on 2020/3/1.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private static String APP_ID = MyApplication.WX_APP_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public static void gotoPay(Context context, String partnerId, String prepayId, String packageValue,
                               String nonceStr, String timeStamp, String sign) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID);
        if (api.isWXAppInstalled() && !api.isWXAppSupportAPI()) { //判断微信版本是否支持支付
            Toast.makeText(context, "请您先安装微信客户端!", Toast.LENGTH_SHORT).show();
            return;
        }
        PayReq req = new PayReq();
        req.appId = APP_ID;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.packageValue = packageValue;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.sign = sign;
        api.sendReq(req);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            EventBus.getDefault().post(new PayResultEvent(resp.errCode));
        }
        finish();
    }
}