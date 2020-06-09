package module.user.time;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.time.WorkPlanDateItemBean;
import common.repository.http.param.time.SetAllDayRestRequestBean;
import common.repository.http.param.time.UploadWorkPlanRequestBean;
import module.app.MyApplication;
import module.dialog.TipDialog;
import ui.CustomClickListener;
import ui.MyGridView;
import ui.title.ToolBarTitleView;
import util.TimeUtils;

public class WorkPlanActivity extends BaseActivity {


    @BindView(R.id.work_plan_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.work_plan_activity_gridview)
    MyGridView gridview;
    @BindView(R.id.work_plan_activity_rest_img)
    ImageView restImg;
    @BindView(R.id.work_plan_activity_select_date_text)
    TextSwitcher selectDateText;

    private boolean isRest = false;

    private WorkPlanAdapter mAdapter;

    private Map<String, WorkPlanDateItemBean> mDataMap;

    private String mDate = "";

    private OptionsPickerView pvOptions;
    private int selectDateIndex = 0;
    private List<String> dates;

    private TipDialog tipDialog;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.context(), WorkPlanActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_work_plan;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAdapter = new WorkPlanAdapter(this);
        gridview.setAdapter(mAdapter);
        tipDialog = new TipDialog(this);
        tipDialog.setTitle("泡泡工作台");
        tipDialog.setContent("1.\t自定义排班：该页面表格所展示的时间，用户可根据需求手动点击，来修改某时间段是否接单；\n" +
                "2.\t默认排班表：设置后，每天会按照所设置的时间段自动生成排班；\n" +
                "3.\t灰色：该时间段不可被预约；\n" +
                "4.\t白色：该时间段可以被预约；\n" +
                "5.\t已预约：该时间段已经被预约；\n" +
                "注意：每次更改后，点击按钮“保存”才能生效");
        tipDialog.setConfirm("确定");
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightClickListener(new CustomClickListener() {
            @Override
            public void onClick() {
                tipDialog.showMyDialog();
            }
        });
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                selectDateIndex = options1;
                mDate = dates.get(options1);
                WorkPlanDateItemBean bean = mDataMap.get(mDate);
                selectDateText.setText(mDate + " " + bean.getWeek());
                isRest = bean.isStopBooking();
                mAdapter.setRest(isRest);
                mAdapter.refresh(bean.getList());
                restImg.setImageResource(R.mipmap.common_checkbox_unselect_icon);
                isRest = bean.isStopBooking();
                restImg.setImageResource(isRest ? R.mipmap.common_checkbox_select_icon : R.mipmap.common_checkbox_unselect_icon);
            }
        })
                .setTitleText("请选择日期")
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
                .setContentTextSize(24)//滚轮文字大小
                .build();
        selectDateText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView tv = new TextView(WorkPlanActivity.this);
                tv.setTextSize(15);
                tv.setTextColor(Color.parseColor("#ff333333"));
                tv.setGravity(Gravity.CENTER | Gravity.LEFT);
                //设置点击事件
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvOptions.setSelectOptions(selectDateIndex);
                        pvOptions.show();
                    }
                });
                return tv;
            }
        });
    }

    @Override
    public void loadData() {
        mDate = TimeUtils.getNewDate();
        MyApplication.loadingDefault(activity());
        HttpApi.app().getWorkDateTimeInfo(this, mDate, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                mDataMap = JSON.parseObject(data, new TypeReference<LinkedHashMap<String, WorkPlanDateItemBean>>() {
                });
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
        WorkPlanDateItemBean bean = mDataMap.get(mDate);
        selectDateText.setText(mDate + " " + bean.getWeek());
        isRest = bean.isStopBooking();
        restImg.setImageResource(isRest ? R.mipmap.common_checkbox_select_icon : R.mipmap.common_checkbox_unselect_icon);
        mAdapter.setRest(isRest);
        mAdapter.refresh(bean.getList());
        dates = new ArrayList<>();
        for (String key : mDataMap.keySet()) {
            dates.add(key);
        }
        pvOptions.setPicker(dates);
    }

    @OnClick({R.id.work_plan_activity_custom_time_btn, R.id.work_plan_activity_select_date_text, R.id.work_plan_activity_select_date_img, R.id.work_plan_activity_rest_btn, R.id.work_plan_activity_rest_img, R.id.work_plan_activity_save_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.work_plan_activity_custom_time_btn:
                NewCustomTimeActivity.newIntent(this);
                break;
            case R.id.work_plan_activity_select_date_text:
            case R.id.work_plan_activity_select_date_img:
                pvOptions.setSelectOptions(selectDateIndex);
                pvOptions.show();
                break;
            case R.id.work_plan_activity_rest_btn:
            case R.id.work_plan_activity_rest_img:
                switchReset();
                break;
            case R.id.work_plan_activity_save_btn:
                saveInfo();
                break;
        }
    }

    public void switchReset() {
        MyApplication.loadingDefault(activity());
        SetAllDayRestRequestBean restRequestBean = new SetAllDayRestRequestBean();
        restRequestBean.setDate(mDate);
        restRequestBean.setTimeType(!isRest ? 2 : 0);
        HttpApi.app().setAllDayRest(this, restRequestBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                isRest = !isRest;
                restImg.setImageResource(isRest ? R.mipmap.common_checkbox_select_icon : R.mipmap.common_checkbox_unselect_icon);
                mAdapter.setRest(isRest);
                mDataMap.get(mDate).setStopBooking(isRest);
                showToast(message);
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    public void saveInfo() {
        if (StringUtil.isBlank(mAdapter.getSetInfo())) {
            showToast("没有可保存信息！");
            return;
        }
        MyApplication.loadingDefault(activity());
        UploadWorkPlanRequestBean requestBean = new UploadWorkPlanRequestBean();
        requestBean.setDate(mDate);
        requestBean.setBody(mAdapter.getSetInfo());
        HttpApi.app().saveTimePlan(this, requestBean, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                MyApplication.hideLoading();
                showToast(message);
                finish();
            }

            @Override
            public void onFailed(HttpError error) {
                MyApplication.hideLoading();
                showToast(error.getErrMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == NewCustomTimeActivity.KEY_ACTIVITY_CODE) {
            loadData();
        }
    }
}
