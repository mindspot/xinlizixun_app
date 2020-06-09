package module.order;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;
import com.paopao.paopaouser.wxapi.WXPayEntryActivity;

import java.util.ArrayList;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.events.PayResultEvent;
import common.repository.bean.OrderTimeBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.coupon.CouponItemBean;
import common.repository.http.entity.order.ConsultantOrderResponseBean;
import common.repository.http.entity.order.OrderExplainResponseBean;
import common.repository.http.entity.order.WeChatPayBean;
import common.repository.http.param.order.BuyGoodItemBean;
import common.repository.http.param.order.ConsultantOrderRequestBean;
import common.repository.http.param.order.PayOrderInfoRequestBean;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import module.dialog.SelectCouponDialog;
import module.main.center.OrderInfoActivity;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

public class OrderBuyActivity extends BaseActivity {

    @BindView(R.id.order_buy_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.order_buy_activity_photo_img)
    ImageView photoImg;
    @BindView(R.id.order_buy_activity_name_text)
    TextView nameText;
    @BindView(R.id.order_buy_activity_way_text)
    TextView wayText;
    @BindView(R.id.order_buy_activity_time_text)
    TextView timeText;
    @BindView(R.id.order_buy_activity_money_text)
    TextView moneyText;
    @BindView(R.id.order_buy_activity_coupon_text)
    TextView couponText;
    @BindView(R.id.order_buy_activity_balance_radio_img)
    ImageView balanceRadioImg;
    @BindView(R.id.order_buy_activity_alipay_radio_img)
    ImageView alipayRadioImg;
    @BindView(R.id.order_buy_activity_wechat_radio_img)
    ImageView wechatRadioImg;
    @BindView(R.id.order_buy_activity_detail_text)
    TextView detailText;
    @BindView(R.id.order_buy_activity_pay_money_text)
    TextView payMoneyText;
    @BindView(R.id.order_buy_activity_coupon_money_text)
    TextView couponMoneyText;
    @BindView(R.id.order_buy_activity_balance_layout)
    LinearLayout balanceLayout;
    @BindView(R.id.order_buy_activity_balance_title)
    TextView balanceTitleText;


    private int mCousultant;
    private ArrayList<String> mConsultantTypes;
    private String mDetail;
    private String mName;
    private int mSex;
    private String mAge;
    private String mMoney;

    private int payWay = 3;
    private int mCouponId = 0;

    private SelectCouponDialog couponDialog;

    private ConsultantOrderResponseBean mData;

    private int mAccount;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public static void newIntent(Page page, int consultant, ArrayList<String> consultantTypes,
                                 String detail, String name, int sex, String age) {
        Intent intent = new Intent(page.activity(), OrderBuyActivity.class);
        intent.putExtra("mCousultant", consultant);
        intent.putStringArrayListExtra("mConsultantTypes", consultantTypes);
        intent.putExtra("mDetail", detail);
        intent.putExtra("mName", name);
        intent.putExtra("mSex", sex);
        intent.putExtra("mAge", age);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_buy;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        couponText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        couponText.getPaint().setAntiAlias(true);//抗锯齿
        if (getIntent() != null) {
            mCousultant = getIntent().getIntExtra("mCousultant", 0);
            mConsultantTypes = getIntent().getStringArrayListExtra("mConsultantTypes");
            mDetail = getIntent().getStringExtra("mDetail");
            mName = getIntent().getStringExtra("mName");
            mSex = getIntent().getIntExtra("mSex", 0);
            mAge = getIntent().getStringExtra("mAge");
        }
        couponDialog = new SelectCouponDialog(this);
        initView();
    }

    public void initView() {
        GlideImageLoader.loadImageCustomCorner(this, MyApplication.app.getPersonalInforBean().getHeadImg(),
                photoImg, ConvertUtil.dip2px(context(), 23));
        nameText.setText(MyApplication.app.getPersonalInforBean().getRealName());
        wayText.setText(MyApplication.app.getWorkInfoBean().getMode());
        OrderTimeBean bean = MyApplication.app.getOrderTimeBean();
        timeText.setText(bean.getMonth() + "月" + bean.getDay() + "日 " + bean.getTime());
        mMoney = MathOperationUtil.divStr(MyApplication.app.getWorkInfoBean().getSellPrice(), 100);
        moneyText.setText(mMoney + "");
        payMoneyText.setText(mMoney + "");
        couponDialog.initData(mMoney);
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        couponDialog.setOnItemClickListener(new SelectCouponDialog.OnItemClickListener() {
            @Override
            public void onClick(CouponItemBean item) {
                mCouponId = item.getGoodsId();
                couponText.setText(item.getGoodsName());
                payMoneyText.setText(MathOperationUtil.subStr(Double.parseDouble(mMoney), MathOperationUtil.div(item.getDeduction(), 100)));
                couponMoneyText.setText("已优惠" + MathOperationUtil.divStr(item.getDeduction(), 100) + "元");
                couponMoneyText.setVisibility(View.VISIBLE);

                if (mAccount >= (MyApplication.app.getWorkInfoBean().getSellPrice() - item.getDeduction())) {
                    balanceLayout.setVisibility(View.VISIBLE);
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 3;
                } else {
                    balanceLayout.setVisibility(View.GONE);
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 2;
                }
            }

            @Override
            public void cancle() {
                mCouponId = 0;
                couponText.setText("请选择优惠券");
                payMoneyText.setText(String.valueOf(mMoney));
                couponMoneyText.setText("");
                couponMoneyText.setVisibility(View.GONE);
                if (mAccount >= MyApplication.app.getWorkInfoBean().getSellPrice()) {
                    balanceLayout.setVisibility(View.VISIBLE);
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 3;
                } else {
                    balanceLayout.setVisibility(View.GONE);
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 2;
                }
            }
        });
    }

    @Override
    public void loadData() {
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getOrderExplain(this, new HttpCallback<OrderExplainResponseBean>() {
            @Override
            public void onSuccess(int code, String message, OrderExplainResponseBean data) {
                MyApplication.hideLoading();
                detailText.setText(data.getDetailedDescription().getContent());
                mAccount = data.getAccount();
                if (mAccount >= MyApplication.app.getWorkInfoBean().getSellPrice()) {
                    balanceLayout.setVisibility(View.VISIBLE);
                    balanceTitleText.setText("余额支付(剩余:" + MathOperationUtil.divStr(mAccount, 100) + ")");
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 3;
                } else {
                    balanceLayout.setVisibility(View.GONE);
                    balanceRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    alipayRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                    wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                    payWay = 2;
                }
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
            }
        });
    }

    @OnClick({R.id.order_buy_activity_coupon_layout, R.id.order_buy_activity_alipay_layout,
            R.id.order_buy_activity_wechat_layout, R.id.order_buy_activity_pay_btn,
            R.id.order_buy_activity_balance_layout})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.order_buy_activity_coupon_layout:
                couponDialog.showMyDialog();
                break;
            case R.id.order_buy_activity_balance_layout:
                balanceRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                alipayRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                payWay = 3;
                break;
            case R.id.order_buy_activity_alipay_layout:
                balanceRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                alipayRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                wechatRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                payWay = 2;
                break;
            case R.id.order_buy_activity_wechat_layout:
                balanceRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                alipayRadioImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                wechatRadioImg.setImageResource(R.mipmap.common_radio_select_icon);
                payWay = 1;
                break;
            case R.id.order_buy_activity_pay_btn:
                orderBuy();
                break;
        }
    }

    public void orderBuy() {
        if (mData != null) {
            getPayInfo();
            return;
        }
        MyApplication.loadingDefault(activity());
        ConsultantOrderRequestBean bean = new ConsultantOrderRequestBean();
        OrderTimeBean timeBean = MyApplication.app.getOrderTimeBean();
        bean.setConsultantWorkDate(timeBean.getYear() + "-" + timeBean.getMonth() + "-" + timeBean.getDay());
        bean.setConsultantWorkTime(timeBean.getTime());

        bean.setIsConsultant(String.valueOf(mCousultant));
        bean.setConsultantType(mConsultantTypes);
        bean.setDetailedDescription(mDetail);
        bean.setOperName(mName);
        bean.setSex(String.valueOf(mSex));
        bean.setAge(mAge);
        bean.setPayType(String.valueOf(payWay));
        bean.setCouponId(String.valueOf(mCouponId));

        BuyGoodItemBean goodBean = new BuyGoodItemBean();
        goodBean.setGoodsClass(String.valueOf(MyApplication.app.getWorkInfoBean().getGoodsClass()));
        goodBean.setGoodsId(String.valueOf(MyApplication.app.getWorkInfoBean().getId()));
        goodBean.setShopId(String.valueOf(MyApplication.app.getPersonalInforBean().getId()));
        ArrayList<BuyGoodItemBean> list = new ArrayList<>();
        list.add(goodBean);
        bean.setBuyGoods(list);
        //套餐
        if (MyApplication.app.getWorkInfoBean().getGoodsClass() == 6) {
            HttpApi.app().buyTaoConsultantOrder(this, bean, callback);
        } else {//普通订单
            HttpApi.app().buyConsultantOrder(this, bean, callback);
        }
    }

    HttpCallback<ConsultantOrderResponseBean> callback = new HttpCallback<ConsultantOrderResponseBean>() {
        @Override
        public void onSuccess(int code, String message, ConsultantOrderResponseBean data) {
            MyApplication.hideLoading();
            mData = data;
            toPay();
        }

        @Override
        public void onFailed(HttpError error) {
            showToast(error.getErrMessage());
            MyApplication.hideLoading();
        }
    };

    public void toPay() {
        if (mData == null) {
            return;
        }
        if (payWay == 2) {
            aliPay(mData.getPrepayment());
        } else if (payWay == 1) {
            wxPay(mData.getPrepayment());
        } else if (payWay == 3) {
            getPayInfo();
        }
    }

    public void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderBuyActivity.this);
                Map<String, String> payResult = alipay.payV2(orderInfo, true);
                Log.i("msp", payResult.toString());
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                final String resultInfo = payResult.get("result");// 同步返回需要验证的信息
                final String resultStatus = payResult.get("resultStatus");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.equals(resultStatus, "9000")) {
                            EventBus.getDefault().post(new PayResultEvent(0));
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            showToast("支付失败");
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            showToast("取消支付");
                        } else {
                            showToast(resultInfo);
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
        WXPayEntryActivity.gotoPay(this, bean.getPartnerId(),
                bean.getPrepayId(), "Sign=WXPay",
                bean.getNonceStr(), bean.getTimeStamp(),
                bean.getSign());
    }

    public void paySuccess() {
        showToast("支付成功");
        MyApplication.hideLoading();
        OrderInfoActivity.newIntent(this);
        EventBus.getDefault().post(new OrderBuyEvent());
        finish();
    }

    public void onEventMainThread(PayResultEvent event) {
        switch (event.getType()) {
            case 0:
                paySuccess();
                break;
            case -1:
                showToast("支付失败");
                break;
            case -2:
                showToast("取消支付");
                break;
        }
    }

    public void getPayInfo() {
        MyApplication.loadingDefault(activity());
        PayOrderInfoRequestBean bean = new PayOrderInfoRequestBean();
        bean.setOrderNo(mData.getConsultationServerOrder().getOrderNo());
        bean.setPayType(payWay);
        HttpApi.app().getPayInfo(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (payWay == 2) {
                    aliPay(data);
                } else if (payWay == 1) {
                    wxPay(data);
                } else {
                    OrderInfoActivity.newIntent(OrderBuyActivity.this);
                    EventBus.getDefault().post(new OrderBuyEvent());
                    finish();
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
}
