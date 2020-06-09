package module.main.center.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.app.LabelItemBean;
import ui.FlowLayout;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class ServiceLabelAdapter extends EasyAdapter<LabelItemBean> {
    public ServiceLabelAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.mycenter_service_info_item_title)
        TextView title;
        @BindView(R.id.mycenter_service_info_item_flow)
        FlowLayout flowLayout;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.mycenter_service_info_item_layout);
        }

        @Override
        public void setData(LabelItemBean data) {
            super.setData(data);
            title.setText(data.getTitle());
            setFlowLayout();
        }

        public void setFlowLayout() {
            flowLayout.removeAllViews();
            for (int i = 0; i < data.getList().size(); i++) {
                String label = data.getList().get(i);
                final TextView tv_tag = (TextView) LayoutInflater.from(page.context()).inflate(R.layout.lable_item_layout, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, ConvertUtil.dip2px(page.context(), 5), ConvertUtil.dip2px(page.context(), 5));
                tv_tag.setLayoutParams(params);
                tv_tag.setText(label);
                flowLayout.addView(tv_tag);
            }
        }

    }
}
