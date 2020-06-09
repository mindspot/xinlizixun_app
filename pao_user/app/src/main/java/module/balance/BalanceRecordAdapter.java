package module.balance;

import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.balance.RecardItemBean;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class BalanceRecordAdapter extends EasyAdapter<RecardItemBean> {

    public BalanceRecordAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {


        @BindView(R.id.withdraw_record_item_date)
        TextView date;
        @BindView(R.id.withdraw_record_item_money)
        TextView money;
        @BindView(R.id.withdraw_record_item_status)
        TextView status;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.withdraw_record_item_layout);
        }

        @Override
        public void setData(RecardItemBean data) {
            super.setData(data);
            date.setText(data.getCashTime());
            money.setText(MathOperationUtil.divStr(data.getAmount(), 100));
            switch (data.getCardType()) {
                case 0:
                    status.setText("审核中");
                    break;
                case 1:
                    status.setText("驳回");
                    break;
                case 2:
                    status.setText("通过");
                    break;
            }

        }
    }
}
