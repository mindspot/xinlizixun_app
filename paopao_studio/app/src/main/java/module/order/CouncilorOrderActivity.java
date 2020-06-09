package module.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.detail.PersonalInforBean;
import common.repository.http.entity.order.UseCouncilorItemBean;
import common.repository.http.param.order.PayCouncilorOrderRequestBean;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import module.dialog.CouncilorPayDialog;
import ui.MyListView;
import ui.title.ToolBarTitleView;
import util.MathOperationUtil;

public class CouncilorOrderActivity extends BaseActivity {

    @BindView(R.id.councilot_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.councilot_activity_photo_img)
    ImageView photoImg;
    @BindView(R.id.councilot_activity_username)
    TextView username;
    @BindView(R.id.councilot_activity_listview)
    MyListView listview;
    @BindView(R.id.councilot_activity_select_btn)
    TextView selectBtn;
    @BindView(R.id.councilot_activity_select_way_btn)
    TextView selectWayBtn;
    @BindView(R.id.councilot_activity_select_layout)
    LinearLayout selectLayout;
    @BindView(R.id.councilot_activity_money_text)
    TextView moneyText;
    @BindView(R.id.councilot_activity_old_money_text)
    TextView oldMoneyText;

    private PersonalInforBean userInfo;

    private SelectCouncilorAdapter mAdapter;

    private int mRatio = 5;

    private OptionsPickerView pvOptions;

    private CouncilorPayDialog dialog;

    public static void newIntent(Page page, PersonalInforBean bean) {
        Intent intent = new Intent(page.activity(), CouncilorOrderActivity.class);
        intent.putExtra("userInfo", bean);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_councilor_order;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            userInfo = (PersonalInforBean) getIntent().getSerializableExtra("userInfo");
        }
        GlideImageLoader.loadGlideImageCustomCorner(context(), userInfo.getHeadImg(), photoImg, 23);
        username.setText(userInfo.getRealName());
        mAdapter = new SelectCouncilorAdapter(this);
        listview.setAdapter(mAdapter);
        selectBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        selectWayBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mRatio = options1;
                selectWayBtn.setText(options1 + "%");
                updateView();
            }
        })
                .setTitleText("请选择分佣比例")
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
                .setContentTextSize(24)//滚轮文字大小
                .build();

        dialog = new CouncilorPayDialog(this);
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemRemoveListener(new SelectCouncilorAdapter.onItemRemoveListener() {
            @Override
            public void onRemove() {
                updateView();
            }
        });
        dialog.setOnItemClickListener(new CouncilorPayDialog.OnItemClickListener() {
            @Override
            public void onClick() {
                pay();
            }
        });
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            list.add(i + "%");
        }
        pvOptions.setPicker(list);
    }

    @OnClick({R.id.councilot_activity_pay_text, R.id.councilot_activity_select_way_btn,
            R.id.councilot_activity_select_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.councilot_activity_pay_text:
                if (mAdapter.getCount() == 0) {
                    showToast("请选择要督导的订单");
                    return;
                }
                dialog.showMyDialog();
                break;
            case R.id.councilot_activity_select_way_btn:
                pvOptions.setSelectOptions(mRatio);
                pvOptions.show();
                break;
            case R.id.councilot_activity_select_btn:
                UseCouncilorListActivity.newIntent(this, TextUtils.join(",", mAdapter.getOrderNos()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UseCouncilorListActivity.KEY_ACTIVITY_CODE) {
            UseCouncilorItemBean bean = (UseCouncilorItemBean) data.getSerializableExtra("mData");
            if (bean != null) {
                addData(bean);
            }
        }
    }

    public void addData(UseCouncilorItemBean bean) {
        boolean isRepetition = false;
        for (UseCouncilorItemBean item : mAdapter.getDatas()) {
            if (item.getId() == bean.getId()) {
                isRepetition = true;
            }
        }
        if (!isRepetition) {
            mAdapter.addData(bean);
        }
        updateView();
    }

    public void updateView() {
        selectLayout.setVisibility(mAdapter.getCount() == 3 ? View.GONE : View.VISIBLE);
        if (mAdapter.getCount() == 0) {
            moneyText.setText("");
            oldMoneyText.setText("");
            return;
        }
        int allMoney = 0;
        for (UseCouncilorItemBean item : mAdapter.getDatas()) {
            allMoney += item.getOrderAmount();
        }
        oldMoneyText.setText("原订单总价：¥" + MathOperationUtil.divStr(allMoney, 100));
        String money = MathOperationUtil.divStr(MathOperationUtil.div(MathOperationUtil.mul(allMoney, mRatio), 100), 100);
        moneyText.setText("督导费用：¥" + money);
    }

    public void pay() {
        MyApplication.loadingDefault(activity());
        PayCouncilorOrderRequestBean bean = new PayCouncilorOrderRequestBean();
        bean.setConsultantId(userInfo.getId());
        bean.setProportion(mRatio);
        bean.setOrderNos(mAdapter.getOrderNos());
        HttpApi.app().payCouncilorOrder(this, bean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                EventBus.getDefault().post(new OrderBuyEvent());
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }
}
