package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.order.OrderListItemBean;
import util.MathOperationUtil;

public class SelectPayDialog extends BaseFullScreenDialog {


    @BindView(R.id.select_pay_dialog_money_text)
    TextView moneyText;
    @BindView(R.id.select_pay_dialog_balance_layout)
    LinearLayout balanceLayout;
    @BindView(R.id.select_pay_dialog_alipay_status_img)
    ImageView alipayStatusImg;
    @BindView(R.id.select_pay_dialog_wechat_status_img)
    ImageView wechatStatusImg;

    private boolean isAliPay = true;

    private OrderListItemBean mData;

    public SelectPayDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initView(View rootView) {
        balanceLayout.setVisibility(View.GONE);
    }

    public void setData(OrderListItemBean data) {
        mData = data;
        moneyText.setText(MathOperationUtil.divStr(data.getOrderAmount(), 100));
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_pay_dialog_layout;
    }

    @OnClick({R.id.select_pay_dialog_close, R.id.select_pay_dialog_alipay_layout
            , R.id.select_pay_dialog_wechat_layout, R.id.select_pay_dialog_pay_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.select_pay_dialog_close:
                dismiss();
                break;
            case R.id.select_pay_dialog_alipay_layout:
                isAliPay = true;
                alipayStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
                wechatStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                break;
            case R.id.select_pay_dialog_wechat_layout:
                isAliPay = false;
                alipayStatusImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                wechatStatusImg.setImageResource(R.mipmap.common_radio_select_icon);
                break;
            case R.id.select_pay_dialog_pay_btn:
                getPayInfo();
                break;
        }
    }

    public void getPayInfo() {

    }
}

