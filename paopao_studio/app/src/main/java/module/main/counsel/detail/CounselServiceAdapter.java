package module.main.counsel.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.counsel.detail.WorkInfoBean;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class CounselServiceAdapter extends EasyAdapter<WorkInfoBean> {

    private boolean isTaoUse = true;

    public void setTaoUse(boolean taoUse) {
        if (!isTaoUse) {
            return;
        }
        isTaoUse = taoUse;
    }

    public boolean isTaoUse() {
        return isTaoUse;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CounselServiceAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder<WorkInfoBean> createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.counsel_service_item_money_text)
        TextView moneyText;
        @BindView(R.id.counsel_service_item_time_text)
        TextView timeText;
        @BindView(R.id.counsel_service_item_way_text)
        TextView wayText;
        @BindView(R.id.counsel_service_item_lineview)
        View lineview;
        @BindView(R.id.counsel_service_item_btn)
        TextView itemBtn;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.counsel_service_item_layout);
        }

        @Override
        public void setData(WorkInfoBean data) {
            super.setData(data);
            lineview.setVisibility(data.getGoodsClass() == 6 ?
                    View.VISIBLE : View.GONE);
            moneyText.setText(data.getGoodsName());
            timeText.setText(data.getShareText());
            wayText.setText(data.getMode());
            itemBtn.setText(!isTaoUse ? "套餐支付" : "点击预约");
            itemBtn.setBackgroundResource(data.getGoodsClass() == 6 ?
                    (isTaoUse ? R.drawable.common_theme_background_share : R.drawable.common_gary_background_share)
                    : R.drawable.common_theme_background_share);
        }

        @OnClick(R.id.counsel_service_item_btn)
        public void onViewClicked() {
            if (isDoubleClick()) {
                return;
            }
            if (data.getGoodsClass() == 6 && !isTaoUse) {
                return;
            }
            if (onItemClickListener != null) {
                onItemClickListener.onClick(data);
            }
        }
    }

    interface OnItemClickListener {
        void onClick(WorkInfoBean data);
    }
}