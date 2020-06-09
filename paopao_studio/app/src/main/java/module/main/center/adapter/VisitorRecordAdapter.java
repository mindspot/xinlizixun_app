package module.main.center.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.app.VisitorRecardItemBean;
import im.IMHolder;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class VisitorRecordAdapter extends EasyAdapter<VisitorRecardItemBean> {

    public VisitorRecordAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.visitor_record_item_image)
        ImageView image;
        @BindView(R.id.visitor_record_item_nickname)
        TextView nickname;
        @BindView(R.id.visitor_record_item_status)
        TextView status;
        @BindView(R.id.visitor_record_item_date)
        TextView date;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.visitor_record_item_layout);
        }

        @Override
        public void setData(VisitorRecardItemBean data) {
            super.setData(data);
            GlideImageLoader.loadGlideImageCustomCorner(page.context(), data.getHeadImg(), image, 30);
            nickname.setText(data.getRealName());
            status.setText(data.getIsOrder() == 1 ? "有" : "无");
            date.setText(data.getVisitorTime());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                protected void onClick() {
                    IMHolder.getInstance().gotoChat(page.context(), data.getEasemobId(), data.getRealName(), data.getHeadImg());
                }
            });
        }
    }
}
