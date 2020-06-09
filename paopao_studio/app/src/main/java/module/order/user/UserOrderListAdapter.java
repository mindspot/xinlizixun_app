package module.order.user;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.UseCouncilorItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class UserOrderListAdapter extends EasyAdapter<UseCouncilorItemBean> {

    public UserOrderListAdapter(Page page) {
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
            switch (data.getOrderCode()) {
                case 1://未支付
                    serviceStatus.setText("未支付");
                    serviceStatus.setTextColor(Color.parseColor("#999999"));
                    break;
                case 2://未支付
                    serviceStatus.setText("已取消");
                    serviceStatus.setTextColor(Color.parseColor("#999999"));
                    break;
                case 3://已支付
                    serviceStatus.setText("已付款");
                    serviceStatus.setTextColor(Color.parseColor("#9DDCAF"));
                    break;
                case 4://已支付
                    serviceStatus.setText("已完成");
                    serviceStatus.setTextColor(Color.parseColor("#9DDCAF"));
                    break;
                case 5://已确定
                    serviceStatus.setText("已确定");
                    serviceStatus.setTextColor(Color.parseColor("#666666"));
                    break;
            }
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
