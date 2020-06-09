package module.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.repository.bean.OrderTimeBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.LabelItemBean;
import common.repository.http.entity.order.OrderUserInfoResponseBean;
import common.repository.http.param.order.ConsultantOrderRequestBean;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import module.main.center.OrderInfoActivity;
import ui.MyGridView;
import ui.title.ToolBarTitleView;

public class OrderUserInfoActivity extends BaseActivity {

    @BindView(R.id.order_time_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.order_userinfo_activity_radio_yes_img)
    ImageView radioYesImg;
    @BindView(R.id.order_userinfo_activity_radio_yes_text)
    TextView radioYesText;
    @BindView(R.id.order_userinfo_activity_radio_no_img)
    ImageView radioNoImg;
    @BindView(R.id.order_userinfo_activity_radio_no_text)
    TextView radioNoText;
    @BindView(R.id.order_userinfo_activity_gridview)
    MyGridView gridview;
    @BindView(R.id.order_userinfo_activity_detail_edit)
    EditText detailEdit;
    @BindView(R.id.order_userinfo_activity_name_edit)
    EditText nameEdit;
    @BindView(R.id.order_userinfo_activity_sex_man_img)
    ImageView sexManImg;
    @BindView(R.id.order_userinfo_activity_sex_man_text)
    TextView sexManText;
    @BindView(R.id.order_userinfo_activity_sex_woman_img)
    ImageView sexWomanImg;
    @BindView(R.id.order_userinfo_activity_sex_woman_text)
    TextView sexWomanText;
    @BindView(R.id.order_userinfo_activity_age_edit)
    EditText ageEdit;
    @BindView(R.id.order_userinfo_activity_radio_yes_layout)
    LinearLayout radioYesLayout;
    @BindView(R.id.order_userinfo_activity_radio_no_layout)
    LinearLayout radioNoLayout;
    @BindView(R.id.order_userinfo_activity_sex_man_layout)
    LinearLayout sexManLayout;
    @BindView(R.id.order_userinfo_activity_sex_woman_layout)
    LinearLayout sexWomanLayout;
    @BindView(R.id.order_userinfo_activity_next_btn)
    TextView nextBtn;

    private int oneRadio = -1;

    private int sexRadio = -1;

    private ClassTypeAdapter mAdapter;

    private OrderUserInfoResponseBean mData;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), OrderUserInfoActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_user_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mAdapter = new ClassTypeAdapter(this);
        gridview.setAdapter(mAdapter);
        nextBtn.setText(MyApplication.app.isUseTao() ? "套餐支付" : "下一步");
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        HttpApi.app().getOrderUserInfo(this, new HttpCallback<OrderUserInfoResponseBean>() {
            @Override
            public void onSuccess(int code, String message, OrderUserInfoResponseBean data) {
                MyApplication.hideLoading();
                mData = data;
                setView();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void setView() {
        LabelItemBean itemBean1 = mData.getConsulteds().get(0);
        radioYesLayout.setTag(itemBean1.getValue());
        radioYesText.setText(itemBean1.getName());

        LabelItemBean itemBean2 = mData.getConsulteds().get(1);
        radioNoLayout.setTag(itemBean2.getValue());
        radioNoText.setText(itemBean2.getName());

        mAdapter.refresh(mData.getClassification());

        LabelItemBean itemBean3 = mData.getConsultationSex().get(0);
        sexManLayout.setTag(itemBean3.getValue());
        sexManText.setText(itemBean3.getName());

        LabelItemBean itemBean4 = mData.getConsultationSex().get(1);
        sexWomanLayout.setTag(itemBean4.getValue());
        sexWomanText.setText(itemBean4.getName());

        if (mData.getLastCase() != null) {
            if (mData.getLastCase().getConsultantType() != null) {
                List<String> list = ConvertUtil.toList(mData.getLastCase().getConsultantType(), String.class);
                mData.getLastCase().setConsultantTypes(list);
            }
            if (mData.getLastCase().getIsConsultant() == (int) radioYesLayout.getTag()) {
                radioYesImg.setImageResource(R.mipmap.common_radio_select_icon);
                oneRadio = (int) radioYesLayout.getTag();
            } else if (mData.getLastCase().getIsConsultant() == (int) radioNoLayout.getTag()) {
                radioNoImg.setImageResource(R.mipmap.common_radio_select_icon);
                oneRadio = (int) radioNoLayout.getTag();
            }
            mAdapter.addIds(mData.getLastCase().getConsultantTypes());

            detailEdit.setText(mData.getLastCase().getDetailedDescription());
            nameEdit.setText(mData.getLastCase().getOperName());
            if (mData.getLastCase().getIsConsultant() == (int) sexManLayout.getTag()) {
                sexManImg.setImageResource(R.mipmap.common_radio_select_icon);
                sexRadio = (int) sexManLayout.getTag();
            } else if (mData.getLastCase().getIsConsultant() == (int) sexWomanLayout.getTag()) {
                sexWomanImg.setImageResource(R.mipmap.common_radio_select_icon);
                sexRadio = (int) sexWomanLayout.getTag();
            }

            ageEdit.setText(mData.getLastCase().getAge() + "");
        }
    }


    @OnClick({R.id.order_userinfo_activity_radio_yes_layout, R.id.order_userinfo_activity_radio_no_layout, R.id.order_userinfo_activity_sex_man_layout, R.id.order_userinfo_activity_sex_woman_layout, R.id.order_userinfo_activity_next_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.order_userinfo_activity_radio_yes_layout:
                radioYesImg.setImageResource(R.mipmap.common_radio_select_icon);
                radioNoImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                oneRadio = (int) view.getTag();
                break;
            case R.id.order_userinfo_activity_radio_no_layout:
                radioNoImg.setImageResource(R.mipmap.common_radio_select_icon);
                radioYesImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                oneRadio = (int) view.getTag();
                break;
            case R.id.order_userinfo_activity_sex_man_layout:
                sexManImg.setImageResource(R.mipmap.common_radio_select_icon);
                sexWomanImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                sexRadio = (int) view.getTag();
                break;
            case R.id.order_userinfo_activity_sex_woman_layout:
                sexWomanImg.setImageResource(R.mipmap.common_radio_select_icon);
                sexManImg.setImageResource(R.mipmap.common_radio_unselect_icon);
                sexRadio = (int) view.getTag();
                break;
            case R.id.order_userinfo_activity_next_btn:
                nextStep();
                break;
        }
    }

    public void nextStep() {
        if (oneRadio == -1
                || mAdapter.getIds().isEmpty()
                || StringUtil.isBlank(detailEdit.getText().toString())
                || StringUtil.isBlank(nameEdit.getText().toString())
                || sexRadio == -1
                || StringUtil.isBlank(ageEdit.getText().toString())) {
            showToast("信息未完善！");
            return;
        }
        if (MyApplication.app.isUseTao()) {
            MyApplication.loadingDefault(activity());
            ConsultantOrderRequestBean bean = new ConsultantOrderRequestBean();
            OrderTimeBean timeBean = MyApplication.app.getOrderTimeBean();
            bean.setConsultantWorkDate(timeBean.getYear() + "-" + timeBean.getMonth() + "-" + timeBean.getDay());
            bean.setConsultantWorkTime(timeBean.getTime());

            bean.setIsConsultant(String.valueOf(oneRadio));
            bean.setConsultantType(mAdapter.getIds());
            bean.setDetailedDescription(detailEdit.getText().toString());
            bean.setOperName(nameEdit.getText().toString());
            bean.setSex(String.valueOf(sexRadio));
            bean.setAge(ageEdit.getText().toString());

            bean.setOrderNo(MyApplication.app.getWorkInfoBean().getOrderNo());

            HttpApi.app().orderBuyTao(this, bean, new HttpCallback<String>() {
                @Override
                public void onSuccess(int code, String message, String data) {
                    MyApplication.hideLoading();
                    OrderInfoActivity.newIntent(OrderUserInfoActivity.this);
                    EventBus.getDefault().post(new OrderBuyEvent());
                    finish();
                }

                @Override
                public void onFailed(HttpError error) {
                    showToast(error.getErrMessage());
                    MyApplication.hideLoading();
                }
            });
        } else {
            OrderBuyActivity.newIntent(this, oneRadio, mAdapter.getIds(), detailEdit.getText().toString(),
                    nameEdit.getText().toString(), sexRadio, ageEdit.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onEventMainThread(OrderBuyEvent event) {
        finish();
    }
}
