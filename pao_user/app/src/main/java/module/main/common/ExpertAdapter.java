package module.main.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.counsel.CounselListItemBean;
import im.IMHolder;
import module.main.counsel.detail.CounselDetailActivity;
import ui.FlowLayout;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/19.
 */

public class ExpertAdapter extends EasyAdapter<CounselListItemBean> {
    public ExpertAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.expert_info_item_photo_img)
        ImageView photoImg;
        @BindView(R.id.expert_info_item_name_text)
        TextView nameText;
        @BindView(R.id.expert_info_item_address_text)
        TextView addressText;
        @BindView(R.id.expert_info_item_content_text)
        TextView contentText;
        @BindView(R.id.expert_info_item_flow_layout)
        FlowLayout flowLayout;
        @BindView(R.id.expert_info_item_money_text)
        TextView moneyText;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.expert_info_item_layout);
        }

        @Override
        public void setData(CounselListItemBean data) {
            super.setData(data);
            String photo = data.getHeadImg();
            if (!StringUtil.isBlank(photo)) {
                if (photo.indexOf("?") != -1) {
                    photo += "&photo=header";
                } else {
                    photo += "?photo=header";
                }
            }
            GlideImageLoader.loadImageCustomCorner(page, photo, photoImg, 23);
            nameText.setText(data.getRealName());
            String address = data.getProvName() + " " + data.getCityName();
            addressText.setText(StringUtil.isBlank(address) ? data.getAddrDetail() : address);
            contentText.setText(data.getDisplayContent());
            setFlowLayout();
            moneyText.setText(MathOperationUtil.divStr(data.getConsultationFee(), 100));
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
        }

        public void setFlowLayout() {
            flowLayout.removeAllViews();
            for (int i = 0; i < data.getConsultantLabelVOs().size(); i++) {
                CounselListItemBean.LabelBean labelBean = data.getConsultantLabelVOs().get(i);
                final TextView tv_tag = (TextView) LayoutInflater.from(page.context()).inflate(R.layout.lable_item_layout, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, ConvertUtil.dip2px(page.context(), 10), ConvertUtil.dip2px(page.context(), 10));
                tv_tag.setLayoutParams(params);
                tv_tag.setText(labelBean.getLabelVal());
                flowLayout.addView(tv_tag);
            }
        }

        @OnClick({R.id.expert_info_item_btn, R.id.expert_info_item_root})
        public void onViewClicked(View view) {
            if (isDoubleClick()) {
                return;
            }
            switch (view.getId()) {
                case R.id.expert_info_item_btn:
                    IMHolder.getInstance().gotoChat(page.context(), data.getEasemobId(), data.getRealName(), data.getHeadImg());
                    break;
                case R.id.expert_info_item_root:
                    CounselDetailActivity.newIntent(page, data.getId());
                    break;
            }
        }
    }
}
