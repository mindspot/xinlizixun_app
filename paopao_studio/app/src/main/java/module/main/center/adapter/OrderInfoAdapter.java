package module.main.center.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.ExtBean;
import common.repository.http.entity.order.OrderListItemBean;
import im.IMHolder;
import ui.CustomClickListener;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class OrderInfoAdapter extends EasyAdapter<OrderListItemBean> {

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderInfoAdapter(Page page) {
        super(page);
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
            switch (data.getOrderCode()) {//1待支付 2 已经取消 3 已支付 4已完成 5已确定
                case 1:
                    payColor = "#333333";
                    startName = "待支付";
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
            if (data.getDiscountAmount() != 0) {
                money = MathOperationUtil.divStr(data.getOrderAmount(), 100) + "元";
                money += "（优惠券已抵扣" + MathOperationUtil.divStr(data.getDiscountAmount(), 100) + "元）";
            }
            moneyText.setText(money);
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
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
        }
    }

    public interface OnItemClickListener {
        void OnClick(OrderListItemBean data);
    }
}
