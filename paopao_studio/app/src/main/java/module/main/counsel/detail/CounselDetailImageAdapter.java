package module.main.counsel.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.counsel.detail.AboutConsultantBean;
import im.IMHolder;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class CounselDetailImageAdapter extends EasyAdapter<AboutConsultantBean.QualificationItemBean> {

    public CounselDetailImageAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder<AboutConsultantBean.QualificationItemBean> createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.counsel_detail_item_title_text)
        TextView titleText;
        @BindView(R.id.counsel_detail_item_image)
        ImageView image;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.counsel_detail_image_item_layout);
        }

        @Override
        public void setData(AboutConsultantBean.QualificationItemBean data) {
            super.setData(data);
            titleText.setText(data.getQualificationsName());
            image.setVisibility(StringUtil.isBlank(data.getImgUrl()) ? View.GONE : View.VISIBLE);
            GlideImageLoader.loadImageView(page, data.getImgUrl(), image);
            GlideImageLoader.preloadImage(page, data.getImgUrl().replace(data.getImgUrlSize(), ""));
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            image.setOnClickListener(new CustomClickListener() {
                @Override
                protected void onClick() {
                    IMHolder.getInstance().showBigImage(page, null, data.getImgUrl().replace(data.getImgUrlSize(), ""));
                }
            });
        }
    }
}