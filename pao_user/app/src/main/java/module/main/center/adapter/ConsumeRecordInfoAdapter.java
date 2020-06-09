package module.main.center.adapter;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.ConsumeRecordItemBean;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class ConsumeRecordInfoAdapter extends EasyAdapter<ConsumeRecordItemBean> {

    public ConsumeRecordInfoAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.consume_record_item_title_text)
        TextView titleText;
        @BindView(R.id.consume_record_item_symbol_text)
        TextView symbolText;
        @BindView(R.id.consume_record_item_money_text)
        TextView moneyText;
        @BindView(R.id.consume_record_item_time_text)
        TextView timeText;
        @BindView(R.id.consume_record_item_yue_text)
        TextView yueText;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.consume_record_item_layout);
        }

        @Override
        public void setData(ConsumeRecordItemBean data) {
            super.setData(data);
            titleText.setText(data.getPointTypeName());
            moneyText.setText(MathOperationUtil.divStr(data.getPoint(), 100));
            if (data.getBalanceBefore() > data.getBalance()) {
                symbolText.setText("-");
                symbolText.setTextColor(Color.parseColor("#666666"));
                moneyText.setTextColor(Color.parseColor("#666666"));
            } else {
                symbolText.setText("+");
                symbolText.setTextColor(page.activity().getResources().getColor(R.color.theme_color));
                moneyText.setTextColor(page.activity().getResources().getColor(R.color.theme_color));
            }
            timeText.setText(data.getCreateDt());
            yueText.setText("余额:" + MathOperationUtil.divStr(data.getBalance(), 100));
        }
    }
}
