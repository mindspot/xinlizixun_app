package module.dialog;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaouser.R;
import com.paopao.paopaouser.wxapi.WXPayEntryActivity;

import java.util.Map;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.PayResultEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.OrderListItemBean;
import common.repository.http.entity.order.WeChatPayBean;
import common.repository.http.param.order.PayOrderInfoRequestBean;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import util.MathOperationUtil;

public class SelectPayDialog extends BaseFullScreenDialog {

    @BindView(R.id.select_pay_dialog_money_text)
    TextView moneyText;
    @BindView(R.id.select_pay_dialog_balance_layout)
    LinearLayout balanceLayout;
    @BindView(R.id.select_pay_dialog_balance_status_img)
    ImageView balanceStatusImg;
    @BindView(R.id.select_pay_dialog_alipay_status_img)
    ImageView alipayStatusImg;
    @BindView(R.id.select_pay_dialog_wechat_status_img)
    ImageView wechatStatusImg;
    @BindView(R.id.select_pay_dialog_balance_title)
    TextView balanceTitleText;


    private int payType = 2;

    private OrderListItemBean mData;

    private int mAccount;

    public SelectPayDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView(View rootView) {
        balanceLayout.setVisibility(View.GONE);
    }


    public void setAccount(int account) {
        this.mAccount = account;
    }


    public void setData(OrderListItemBean data) {
        mData = data;
        moneyText.setText(MathOperationUtil.divStr(data.getOrderAmount(), 100));
        balanceTitleText.setText("余额支付(剩余:" + MathOperationUtil.divStr(mAccount, 100) + ")");
        if (mAccount >= MyApplication.app.getWorkInfoBean().getSellPrice()) {
            balanceLayout.setVisibility(View.VISIBLE);
            balanceStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
            alipayStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
            wechatStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
            payType = 3;
        } else {
            balanceLayout.setVisibility(View.GONE);
            balanceStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
            alipayStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
            wechatStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
            payType = 2;
        }

        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_pay_dialog_layout;
    }

    @OnClick({R.id.select_pay_dialog_close, R.id.select_pay_dialog_alipay_layout
            , R.id.select_pay_dialog_wechat_layout, R.id.select_pay_dialog_pay_btn,
            R.id.select_pay_dialog_balance_layout})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.select_pay_dialog_close:
                dismiss();
                break;
            case R.id.select_pay_dialog_balance_layout:
                payType = 3;
                balanceStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
                alipayStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                wechatStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                break;
            case R.id.select_pay_dialog_alipay_layout:
                payType = 2;
                balanceStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                alipayStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
                wechatStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                break;
            case R.id.select_pay_dialog_wechat_layout:
                payType = 1;
                balanceStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                alipayStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                wechatStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
                break;
            case R.id.select_pay_dialog_pay_btn:
                getPayInfo();
                break;
        }
    }

    public void getPayInfo() {
        MyApplication.loadingDefault(page.activity());
        PayOrderInfoRequestBean bean = new PayOrderInfoRequestBean();
        bean.setOrderNo(mData.getOrderNo());
        bean.setPayType(payType);
        HttpApi.app().getPayInfo(page, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (payType == 2) {
                    aliPay(data);
                } else if (payType == 1) {
                    wxPay(data);
                } else if (payType == 3) {
                    EventBus.getDefault().post(new PayResultEvent(0));
                }
                dismiss();
                MyApplication.hideLoading();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                page.showToast(error.getErrMessage());
            }
        });
    }

    public void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(page.activity());
                Map<String, String> payResult = alipay.payV2(orderInfo, true);
                Log.i("msp", payResult.toString());
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                final String resultInfo = payResult.get("result");// 同步返回需要验证的信息
                final String resultStatus = payResult.get("resultStatus");
                page.activity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            EventBus.getDefault().post(new PayResultEvent(0));
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            EventBus.getDefault().post(new PayResultEvent(-1));
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            EventBus.getDefault().post(new PayResultEvent(-2));
                        } else {
                            page.showToast(resultInfo);
                        }
                    }
                });
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void wxPay(String payStr) {
        WeChatPayBean bean = ConvertUtil.toObject(payStr, WeChatPayBean.class);
        //https://wxpay.wxutil.com/pub_v2/app/app_pay.php
        WXPayEntryActivity.gotoPay(page.context(), bean.getPartnerId(),
                bean.getPrepayId(), "Sign=WXPay",
                bean.getNonceStr(), bean.getTimeStamp(),
                bean.getSign());
    }
}

