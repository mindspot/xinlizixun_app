package module.main.counsel;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.counsel.SortItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class SelectSortAdapter extends EasyAdapter<SortItemBean> {
    public SelectSortAdapter(Page page) {
        super(page);
    }

    public void reset() {
        for (SortItemBean item : datas) {
            item.setCheck(false);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getIds() {
        List<Integer> list = new ArrayList<>();
        for (SortItemBean item : datas) {
            if (item.isCheck()) {
                list.add(item.getId());
            }
        }
        return list;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.select_sort_pop_item_text)
        TextView textView;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.select_sort_pop_item_layout);
        }

        @Override
        public void setData(SortItemBean data) {
            super.setData(data);
            textView.setText(data.getVal());
            textView.setTextColor(Color.parseColor(data.isCheck() ? "#9DDCAF" : "#999999"));
            textView.setBackgroundResource(data.isCheck() ? R.drawable.select_sort_pop_select_background_share :
                    R.drawable.select_sort_pop_unselect_background_share);
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    data.setCheck(!data.isCheck());
                    notifyDataSetChanged();
                }
            });
        }
    }
}