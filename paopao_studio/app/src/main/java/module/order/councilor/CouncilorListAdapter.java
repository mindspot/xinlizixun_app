package module.order.councilor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.order.councilor.CouncilorItemBean;
import im.IMHolder;
import module.order.CouncilorOrderDetailActivity;
import ui.CustomClickListener;
import ui.FlowLayout;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class CouncilorListAdapter extends EasyAdapter<CouncilorItemBean> {

    private boolean isChat = true;

    public void setChat(boolean chat) {
        isChat = chat;
    }

    private int mType;

    public CouncilorListAdapter(Page page, int type) {
        super(page);
        this.mType = type;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.common_councilor_list_photo)
        ImageView photo;
        @BindView(R.id.common_councilor_list_username)
        TextView username;
        @BindView(R.id.common_councilor_list_flow_layout)
        FlowLayout flowLayout;
        @BindView(R.id.common_councilor_list_status)
        TextView status;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.common_councilor_list_item_layout);
        }

        @Override
        public void setData(CouncilorItemBean data) {
            super.setData(data);
            GlideImageLoader.loadGlideImageCustomCorner(page.context(), data.getHeadImg(), photo, 23);
            username.setText(data.getRealName());
            switch (data.getOrderCode()) {
                case 1:
                    status.setText("已申请");
                    status.setTextColor(Color.parseColor("#333333"));
                    break;
                case 2:
                    status.setText("已取消");
                    status.setTextColor(Color.parseColor("#999999"));
                    break;
                case 4:
                    status.setText("已督导");
                    status.setTextColor(Color.parseColor("#9DDCAF"));
                    break;
                case 5:
                    status.setText("已接单");
                    status.setTextColor(Color.parseColor("#9DDCAF"));
                    break;
                case 11:
                    status.setText("已拒绝");
                    status.setTextColor(Color.parseColor("#333333"));
                    break;
            }
            setFlowLayout();
        }

        public void setFlowLayout() {
            flowLayout.removeAllViews();
            for (int i = 0; i < data.getLabelVOs().size(); i++) {
                String label = data.getLabelVOs().get(i);
                final TextView tv_tag = (TextView) LayoutInflater.from(page.context()).inflate(R.layout.lable_item_layout, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, ConvertUtil.dip2px(page.context(), 5), ConvertUtil.dip2px(page.context(), 5));
                tv_tag.setLayoutParams(params);
                tv_tag.setText(label);
                flowLayout.addView(tv_tag);
            }
        }

        @Override
        protected void initLisenter() {
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    if (mType == 1)
                        CouncilorOrderDetailActivity.newIntent(page.context(), data.getOrderNo(), mType, isChat);
                    else if (mType == 2)
                        CouncilorOrderDetailActivity.newIntent(page.context(), data.getOrderNo(), mType, data.getRealName(), data.getHeadImg(), isChat);
                }
            });
        }
    }
}
