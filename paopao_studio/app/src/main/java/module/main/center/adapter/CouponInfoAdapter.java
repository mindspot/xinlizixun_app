package module.main.center.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.coupon.CouponItemBean;
import ui.CustomClickListener;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class CouponInfoAdapter extends EasyAdapter<CouponItemBean> {
    private int selectPosition = -1;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public CouponInfoAdapter(Page page) {
        super(page);
    }

    private OnItemClickListensr onItemClickListensr;

    public void setOnItemClickListensr(OnItemClickListensr onItemClickListensr) {
        this.onItemClickListensr = onItemClickListensr;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.coupon_info_item_title_text)
        TextView titleText;
        @BindView(R.id.coupon_info_item_time_text)
        TextView timeText;
        @BindView(R.id.coupon_info_item_symbol_text)
        TextView symbolText;
        @BindView(R.id.coupon_info_item_money_text)
        TextView moneyText;
        @BindView(R.id.coupon_info_item_tiaojian_text)
        TextView tiaojianText;
        @BindView(R.id.coupon_info_item_status_img)
        ImageView statusImg;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.coupon_info_item_layout);
        }

        @Override
        public void setData(CouponItemBean data) {
            super.setData(data);
            titleText.setText(data.getGoodsName());
            titleText.setTextColor(Color.parseColor(data.isBeOverdue() ? "#999999" : "#333333"));

            timeText.setText("有效期至" + data.getTermEndDate());

            moneyText.setText(MathOperationUtil.divStr(data.getDeduction(), 100));
            symbolText.setTextColor(Color.parseColor(data.isBeOverdue() ? "#999999" : "#ED4B4B"));
            moneyText.setTextColor(Color.parseColor(data.isBeOverdue() ? "#999999" : "#ED4B4B"));

            tiaojianText.setText(data.getUseNotice());

            statusImg.setImageResource(R.mipmap.coupon_item_status_icon);
            statusImg.setVisibility(data.isBeOverdue() ? View.VISIBLE : View.GONE);

            if (selectPosition == position) {
                statusImg.setVisibility(View.VISIBLE);
                statusImg.setImageResource(R.mipmap.coupon_select_icon);
            }
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    selectPosition = position;
                    if (onItemClickListensr != null) {
                        onItemClickListensr.onClick(data);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface OnItemClickListensr {
        void onClick(CouponItemBean data);
    }
}
