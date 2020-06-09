package module.main.center.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.ExtBean;
import common.repository.http.entity.order.OrderListItemBean;
import common.repository.http.param.order.CancleOrderRequestBean;
import im.IMHolder;
import module.app.MyApplication;
import module.dialog.TipDialog;
import ui.CustomClickListener;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class OrderInfoAdapter extends EasyAdapter<OrderListItemBean> {

    private TipDialog tipDialog;

    private OnItemClickListener itemClickListener;

    private int cancleIndex;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderInfoAdapter(Page page) {
        super(page);
        tipDialog = new TipDialog(page);
        tipDialog.setTitle("订单取消");
        tipDialog.setContent("取消两次订单后，当日不可继续下单。是否确认取消订单？");
        tipDialog.setConfirm("确定");
        tipDialog.setCancle("取消");
        tipDialog.setOnItemClickListener(new TipDialog.OnItemClickListener() {
            @Override
            public void confirm() {
                cancleOrder();
            }

            @Override
            public void cancle() {
            }
        });
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.order_info_item_photo_img)
        ImageView photoImg;
        @BindView(R.id.order_info_item_name_text)
        TextView nameText;
        @BindView(R.id.order_info_item_status_text)
        TextView statusText;
        @BindView(R.id.order_info_item_way_text)
        TextView wayText;
        @BindView(R.id.order_info_item_time_text)
        TextView timeText;
        @BindView(R.id.order_info_item_ordertime_text)
        TextView ordertimeText;
        @BindView(R.id.order_info_item_money_text)
        TextView moneyText;
        @BindView(R.id.order_info_item_chat_btn)
        TextView chatBtn;
        @BindView(R.id.order_info_item_cancle_btn)
        TextView cancleBtn;
        @BindView(R.id.order_info_item_pay_btn)
        TextView payBtn;
        @BindView(R.id.btn_layout)
        LinearLayout btnLayout;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.order_info_item_layout);
        }

        @Override
        public void setData(OrderListItemBean data) {
            super.setData(data);
            GlideImageLoader.loadImageCustomCorner(page, data.getHeadImg(), photoImg, ConvertUtil.dip2px(page.context(), 23));
            nameText.setText(data.getRealName());
            String payColor = "#333333";
            String startName = "";
            btnLayout.setVisibility(View.GONE);
            switch (data.getOrderCode()) {//1待支付 2 已经取消 3 已支付 4已完成 5已确定
                case 1:
                    payColor = "#333333";
                    startName = "待支付";
                    btnLayout.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    payColor = "#999999";
                    startName = "已取消";
                    break;
                case 3:
                    payColor = "#9DDCAF";
                    startName = "已支付";
                    break;
                case 4:
                    payColor = "#9DDCAF";
                    startName = "已完成";
                    break;
                case 5:
                    payColor = "#9DDCAF";
                    startName = "已确定";
                    break;
            }
            statusText.setText(startName);
            statusText.setTextColor(Color.parseColor(payColor));

            wayText.setText(data.getGoodsClassName());
            data.setExtBean(ConvertUtil.toObject(data.getExt(), ExtBean.class));
            timeText.setText(data.getExtBean().getConsultantWorkDate() + " " + data.getExtBean().getConsultantWorkTime());
            ordertimeText.setText(data.getOrderTime());
            String money = MathOperationUtil.divStr(data.getOrderAmount(), 100) + "元";
            money += "（优惠券已抵扣" + MathOperationUtil.divStr(data.getDiscountAmount(), 100) + "元）";
            moneyText.setText(money);
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            payBtn.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    if (data.getOrderCode() != 1) {
                        return;
                    }
                    if (itemClickListener != null) {
                        itemClickListener.OnClick(data);
                    }
                }
            });
            chatBtn.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    IMHolder.getInstance().gotoChat(page.context(), data.getEasemobId(), data.getRealName(), data.getHeadImg());
                }
            });
            cancleBtn.setOnClickListener(new CustomClickListener() {
                @Override
                protected void onClick() {
                    cancleIndex = position;
                    tipDialog.showMyDialog();
                }
            });
        }
    }

    public void cancleOrder() {
        MyApplication.loadingDefault(page.activity());
        CancleOrderRequestBean bean = new CancleOrderRequestBean();
        bean.setOrderNo(datas.get(cancleIndex).getOrderNo());
        HttpApi.app().cancleOrder(page, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String mdata) {
                MyApplication.hideLoading();
                if (code == 0) {
                    datas.get(cancleIndex).setOrderCode(2);
                    notifyDataSetChanged();
                    return;
                }
                page.showToast(message);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                page.showToast(error.getErrMessage());
            }
        });
    }

    public interface OnItemClickListener {
        void OnClick(OrderListItemBean data);
    }
}
