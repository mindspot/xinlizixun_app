package module.order;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.UseCouncilorItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class SelectCouncilorAdapter extends EasyAdapter<UseCouncilorItemBean> {

    private onItemRemoveListener onItemRemoveListener;

    public void setOnItemRemoveListener(SelectCouncilorAdapter.onItemRemoveListener onItemRemoveListener) {
        this.onItemRemoveListener = onItemRemoveListener;
    }

    public SelectCouncilorAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder<UseCouncilorItemBean> createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.select_councilor_detail_title)
        TextView title;
        @BindView(R.id.select_councilor_detail_remove_img)
        ImageView removeImg;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.select_councilor_detail_item_layout);
        }

        @Override
        public void setData(UseCouncilorItemBean data) {
            super.setData(data);
            title.setText(data.getRealName() + "的案例");
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            removeImg.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    remove(position);
                    notifyDataSetChanged();
                    if (onItemRemoveListener != null) {
                        onItemRemoveListener.onRemove();
                    }
                }
            });
        }
    }

    public interface onItemRemoveListener {
        void onRemove();
    }

    public List<String> getOrderNos() {
        List<String> list = new ArrayList<>();
        for (UseCouncilorItemBean data : datas) {
            list.add(data.getOrderNo());
        }
        return list;
    }
}