package module.main.home;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.events.TabEvent;
import common.repository.http.entity.home.MenuItemBean;
import de.greenrobot.event.EventBus;
import module.main.MainActivity;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class MenuAdapter extends EasyAdapter<MenuItemBean> {
    public MenuAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.home_menu_item_icon)
        ImageView iconView;
        @BindView(R.id.home_menu_item_name)
        TextView name;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.home_menu_item_layout);
        }

        @Override
        public void setData(MenuItemBean data) {
            super.setData(data);
            GlideImageLoader.loadImageCustomCorner(page, data.getIcon(), iconView, 0);
            name.setText(data.getVal());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                protected void onClick() {
                    EventBus.getDefault().post(new TabEvent(TabEvent.TYPE_CHANGE_TAB, MainActivity.TAB_INDEX_ZIXUN, data.getId(), 0));
                }
            });
        }
    }
}
