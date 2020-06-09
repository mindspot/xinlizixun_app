package module.order;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.WorkTimeItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class OrderChooseTimeAdapter extends EasyAdapter<WorkTimeItemBean> {

    public OrderChooseTimeAdapter(Page page) {
        super(page);
    }

    private int mPosition = -1;

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.order_select_time_item_no_text)
        TextView noText;
        @BindView(R.id.order_select_time_item_text)
        TextView text;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.order_select_time_item_layout);
        }

        @Override
        public void setData(WorkTimeItemBean data) {
            super.setData(data);
            if (data.getType() == 2) {
                noText.setVisibility(View.INVISIBLE);
                text.setVisibility(View.GONE);
                return;
            }
            noText.setVisibility(data.getType() == 1 ? View.VISIBLE : View.GONE);
            text.setVisibility(data.getType() != 1 ? View.VISIBLE : View.GONE);
            if (data.getType() == 0) {
                text.setTextColor(Color.parseColor(position == mPosition ? "#ffffff" : "#9DDCAF"));
                text.setBackgroundResource(position == mPosition ? R.drawable.common_theme_background_share :
                        R.drawable.common_theme_border_background_share);
            }
            text.setText(data.getConsultantWorkStartTime() + "\n—\n" + data.getConsultantWorkEndTime());
        }

        @Override
        protected void initLisenter() {
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    if (data.getType() == 0) {
                        mPosition = position;
                        notifyDataSetChanged();
                        if (onItemListener != null) {
                            onItemListener.OnClick(data);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemListener {
        void OnClick(WorkTimeItemBean data);
    }
}
