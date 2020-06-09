package module.order;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.order.councilor.CouncilorOrderDetailResponseBean;
import im.IMHolder;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class OrderCaseListAdapter extends EasyAdapter<CouncilorOrderDetailResponseBean.SupervisorOrderDetailBean> {

    public OrderCaseListAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder<CouncilorOrderDetailResponseBean.SupervisorOrderDetailBean> createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.order_case_item_case_title)
        TextView caseTitle;
        @BindView(R.id.order_case_item_case_arrows)
        ImageView caseArrows;
        @BindView(R.id.order_case_item_time)
        TextView caseItemTime;
        @BindView(R.id.order_case_item_userinfo)
        TextView userinfo;
        @BindView(R.id.order_case_item_yes_or_no)
        TextView yesOrNo;
        @BindView(R.id.order_case_item_question)
        TextView question;
        @BindView(R.id.order_case_item_describe)
        TextView describe;
        @BindView(R.id.order_case_item_counselor_time)
        TextView counselorTime;
        @BindView(R.id.order_case_item_counselor_username)
        TextView counselorUsername;
        @BindView(R.id.order_case_item_counselor_describe)
        TextView counselorDescribe;
        @BindView(R.id.order_case_item_counselor_one_image)
        ImageView counselorOneImage;
        @BindView(R.id.order_case_item_counselor_two_image)
        ImageView counselorTwoImage;
        @BindView(R.id.order_case_item_counselor_three_image)
        ImageView counselorThreeImage;
        @BindView(R.id.order_case_item_counselor_image_layout)
        LinearLayout counselorImageLayout;
        @BindView(R.id.order_case_item_detail_layout)
        LinearLayout detailLayout;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.order_case_item_layout);
        }

        @Override
        public void setData(CouncilorOrderDetailResponseBean.SupervisorOrderDetailBean data) {
            super.setData(data);
            detailLayout.setVisibility(data.isOpen() ? View.VISIBLE : View.GONE);
            caseTitle.setText("客户" + (position + 1) + ":案例详情");
            caseArrows.setImageResource(data.isOpen() ? R.mipmap.common_up_arrows_icon : R.mipmap.common_down_arrows_icon);
            CouncilorOrderDetailResponseBean.MemberCaseBean memberCase = data.getMemberCase();
            if (memberCase == null) {
                return;
            }
            caseItemTime.setText(data.getOrderInfo().split(" ")[0]);
            caseTitle.setText("客户 " + memberCase.getOperName() + ":案例详情");
            userinfo.setText("姓名：" + memberCase.getOperName() + "  性别：" + (memberCase.getSex() == 0 ? "男" : "女") + "  年龄：" + memberCase.getAge());
            yesOrNo.setText("是否接受过咨询：" + (memberCase.isConsultant() ? "是" : "否"));
            String questionStr = "";
            for (CouncilorOrderDetailResponseBean.CommonItem item : memberCase.getCommonVOs()) {
                questionStr += item.getVal() + ",";
            }
            questionStr = questionStr.substring(0, questionStr.lastIndexOf(","));
            question.setText("咨询问题：" + questionStr);
            describe.setText("详情描述：" + memberCase.getDetailedDescription());

            counselorImageLayout.setVisibility(View.VISIBLE);
            counselorOneImage.setVisibility(View.GONE);
            counselorTwoImage.setVisibility(View.GONE);
            counselorThreeImage.setVisibility(View.GONE);
            counselorTime.setText("");
            counselorUsername.setText("");
            counselorDescribe.setText("");

            if (!data.isDiagnosis()) {
                return;
            }
            counselorTime.setText(memberCase.getConsultationTime());
            counselorUsername.setText("咨询师：" + memberCase.getConsultatName());
            if (memberCase.getConsultantOrderDiagnosisVO() != null)
                counselorDescribe.setText(memberCase.getConsultantOrderDiagnosisVO().getContent());
            if (memberCase.getConsultantOrderDiagnosisVOs().isEmpty()) {
                counselorImageLayout.setVisibility(View.GONE);
                return;
            }
            if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 1) {
                counselorOneImage.setVisibility(View.VISIBLE);
                CouncilorOrderDetailResponseBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(0);
                GlideImageLoader.loadImageView(page, bean.getContent(), counselorOneImage);
            }
            if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 2) {
                counselorTwoImage.setVisibility(View.VISIBLE);
                CouncilorOrderDetailResponseBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(1);
                GlideImageLoader.loadImageView(page, bean.getContent(), counselorTwoImage);
            }
            if (memberCase.getConsultantOrderDiagnosisVOs().size() >= 3) {
                counselorThreeImage.setVisibility(View.VISIBLE);
                CouncilorOrderDetailResponseBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(2);
                GlideImageLoader.loadImageView(page, bean.getContent(), counselorThreeImage);
            }
        }

        @OnClick({R.id.order_case_item_case_layout,
                R.id.order_case_item_counselor_one_image, R.id.order_case_item_counselor_two_image,
                R.id.order_case_item_counselor_three_image})
        public void onViewClicked(View view) {
            if (isDoubleClick()) {
                return;
            }
            switch (view.getId()) {
                case R.id.order_case_item_case_layout:
                    data.setOpen(!data.isOpen());
                    caseArrows.setImageResource(data.isOpen() ? R.mipmap.common_up_arrows_icon : R.mipmap.common_down_arrows_icon);
                    detailLayout.setVisibility(data.isOpen() ? View.VISIBLE : View.GONE);
                    break;
                case R.id.order_case_item_counselor_one_image:
                    showBigImage(0);
                    break;
                case R.id.order_case_item_counselor_two_image:
                    showBigImage(1);
                    break;
                case R.id.order_case_item_counselor_three_image:
                    showBigImage(2);
                    break;
            }
        }

        public void showBigImage(int index) {
            CouncilorOrderDetailResponseBean.MemberCaseBean memberCase = data.getMemberCase();
            if (memberCase.getConsultantOrderDiagnosisVOs().isEmpty()) {
                return;
            }
            if (memberCase.getConsultantOrderDiagnosisVOs().size() >= index) {
                CouncilorOrderDetailResponseBean.ConsultantOrderDiagnosisBean bean = memberCase.getConsultantOrderDiagnosisVOs().get(index);
                IMHolder.getInstance().showBigImage(page, null, bean.getContent().replace(bean.getImgeSize(), ""));
            }
        }
    }

}