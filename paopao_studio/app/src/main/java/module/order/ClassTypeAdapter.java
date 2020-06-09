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
import common.repository.http.entity.home.MenuItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class ClassTypeAdapter extends EasyAdapter<MenuItemBean> {

    private ArrayList<String> ids = new ArrayList<>();

    public ArrayList<String> getIds() {
        return ids;
    }

    public void addIds(List<String> ids) {
        if (ids == null) {
            return;
        }
        this.ids.clear();
        this.ids.addAll(ids);
        notifyDataSetChanged();
    }

    public void addOrRemove(String id) {
        if (ids.contains(id)) {
            ids.remove(id);
        } else {
            ids.add(id);
        }
        notifyDataSetChanged();
    }

    public ClassTypeAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.order_userinfo_item_icon)
        ImageView icon;
        @BindView(R.id.order_userinfo_item_name)
        TextView name;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.order_userinfo_item_layout);
        }

        @Override
        public void setData(MenuItemBean data) {
            super.setData(data);
            name.setText(data.getVal());
            icon.setImageResource(ids.contains(String.valueOf(data.getId()))
                    ? R.mipmap.common_checkbox_select_icon :
                    R.mipmap.common_checkbox_unselect_icon);
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    addOrRemove(String.valueOf(data.getId()));
                }
            });
        }
    }
}
