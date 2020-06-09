package module.order;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.UseCouncilorItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class UseCouncilorListAdapter extends EasyAdapter<UseCouncilorItemBean> {

    public UseCouncilorListAdapter(Page page) {
        super(page);
    }

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.councilor_list_item_photo)
        ImageView photo;
        @BindView(R.id.councilor_list_item_username)
        TextView username;
        @BindView(R.id.councilor_list_item_service_type)
        TextView serviceType;
        @BindView(R.id.councilor_list_item_service_time)
        TextView serviceTime;
        @BindView(R.id.councilor_list_item_service_status)
        TextView serviceStatus;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.councilor_list_item_layout);
        }

        @Override
        public void setData(UseCouncilorItemBean data) {
            super.setData(data);
            GlideImageLoader.loadGlideImageCustomCorner(page.context(), data.getHeadImg(), photo, 23);
            username.setText(data.getRealName());
            serviceType.setText(data.getGoodsClassName());
            serviceTime.setText(data.getExt());
        }

        @Override
        protected void initLisenter() {
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    if (onItemListener != null) {
                        onItemListener.OnClick(data);
                    }
                }
            });
        }
    }

    public interface OnItemListener {
        void OnClick(UseCouncilorItemBean data);
    }
}
