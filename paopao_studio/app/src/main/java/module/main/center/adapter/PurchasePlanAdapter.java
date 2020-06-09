package module.main.center.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.SetMealItemBean;
import im.chat.ChatActivity;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class PurchasePlanAdapter extends EasyAdapter<SetMealItemBean> {
    public PurchasePlanAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.purchase_plan_item_title)
        TextView titleText;
        @BindView(R.id.purchase_plan_item_yue)
        TextView yueText;
        @BindView(R.id.purchase_plan_item_datetime)
        TextView datetime;
        @BindView(R.id.purchase_plan_item_background_layout)
        LinearLayout backgroundLayout;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.purchase_plan_item_layout);
        }

        @Override
        public void setData(SetMealItemBean data) {
            super.setData(data);
            if (position % 3 == 0) {
                backgroundLayout.setBackgroundResource(R.mipmap.purchase_plan_one_background);
            } else if (position % 3 == 1) {
                backgroundLayout.setBackgroundResource(R.mipmap.purchase_plan_two_background);
            } else {
                backgroundLayout.setBackgroundResource(R.mipmap.purchase_plan_three_background);
            }
            titleText.setText(data.getUserName() + "购买的套餐卡");
            yueText.setText("剩余" + data.getConsultationNumber() + "次");
            datetime.setText("有效期：" + data.getBuyDate() + " - " + data.getTermEndDate());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.newIntent(page.context(), data.getEasemobUserId());
                }
            });
        }
    }
}
