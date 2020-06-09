package module.user.time;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.time.SystemPlanTimeItemBean;
import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.time.UploadCustomTimeItemBean;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class CustomTimeActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_custom_time;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void loadData() {

    }
//
//    @BindView(R.id.custom_time_activity_toolbar)
//    ToolBarTitleView toolbar;
//    @BindView(R.id.custom_time_activity_one_chk)
//    ImageView oneChk;
//    @BindView(R.id.custom_time_activity_one_left_text)
//    TextView oneLeftText;
//    @BindView(R.id.custom_time_activity_one_left_layout)
//    LinearLayout oneLeftLayout;
//    @BindView(R.id.custom_time_activity_one_right_text)
//    TextView oneRightText;
//    @BindView(R.id.custom_time_activity_one_right_layout)
//    LinearLayout oneRightLayout;
//    @BindView(R.id.custom_time_activity_two_chk)
//    ImageView twoChk;
//    @BindView(R.id.custom_time_activity_two_left_text)
//    TextView twoLeftText;
//    @BindView(R.id.custom_time_activity_two_left_layout)
//    LinearLayout twoLeftLayout;
//    @BindView(R.id.custom_time_activity_two_right_text)
//    TextView twoRightText;
//    @BindView(R.id.custom_time_activity_two_right_layout)
//    LinearLayout twoRightLayout;
//    @BindView(R.id.custom_time_activity_three_chk)
//    ImageView threeChk;
//    @BindView(R.id.custom_time_activity_three_left_text)
//    TextView threeLeftText;
//    @BindView(R.id.custom_time_activity_three_left_layout)
//    LinearLayout threeLeftLayout;
//    @BindView(R.id.custom_time_activity_three_right_text)
//    TextView threeRightText;
//    @BindView(R.id.custom_time_activity_three_right_layout)
//    LinearLayout threeRightLayout;
//    @BindView(R.id.custom_time_activity_four_chk)
//    ImageView fourChk;
//    @BindView(R.id.custom_time_activity_four_left_text)
//    TextView fourLeftText;
//    @BindView(R.id.custom_time_activity_four_left_layout)
//    LinearLayout fourLeftLayout;
//    @BindView(R.id.custom_time_activity_four_right_text)
//    TextView fourRightText;
//    @BindView(R.id.custom_time_activity_four_right_layout)
//    LinearLayout fourRightLayout;
//    @BindView(R.id.custom_time_activity_one_layout)
//    LinearLayout oneLayout;
//    @BindView(R.id.custom_time_activity_two_layout)
//    LinearLayout twoLayout;
//    @BindView(R.id.custom_time_activity_three_layout)
//    LinearLayout threeLayout;
//    @BindView(R.id.custom_time_activity_four_layout)
//    LinearLayout fourLayout;
//
//    private boolean isOneChk = false;
//    private boolean isTwoChk = false;
//    private boolean isThreeChk = false;
//    private boolean isFourChk = false;
//
//    private OptionsPickerView oneOptions;
//    private OptionsPickerView twoOptions;
//    private OptionsPickerView threeOptions;
//    private OptionsPickerView fourOptions;
//
//    private int oneLeftIndex = 0;
//    private int oneRightIndex = 1;
//
//    private int twoLeftIndex = 0;
//    private int twoRightIndex = 1;
//
//    private int threeLeftIndex = 0;
//    private int threeRightIndex = 1;
//
//    private int fourLeftIndex = 0;
//    private int fourRightIndex = 1;
//
//    public static final int KEY_ACTIVITY_CODE = 1001;
//
//    private SystemPlanTimeItemBean oneBean;
//    private SystemPlanTimeItemBean twoBean;
//    private SystemPlanTimeItemBean threeBean;
//    private SystemPlanTimeItemBean fourBean;
//
//    public static void newIntent(Page page) {
//        page.startActivityForResult(new Intent(page.activity(), CustomTimeActivity.class), KEY_ACTIVITY_CODE);
//    }
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.activity_custom_time;
//    }
//
//    @Override
//    public void initView(Bundle savedInstanceState) {
//        oneLayout.setVisibility(View.GONE);
//        twoLayout.setVisibility(View.GONE);
//        threeLayout.setVisibility(View.GONE);
//        fourLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void initListener() {
//        toolbar.setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        oneOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                if (options1 >= option2) {
//                    showToast("开始时间要大于结束时间");
//                } else {
//                    oneLeftIndex = options1;
//                    oneRightIndex = option2;
//                    oneLeftText.setText(oneBean.getArrTime().get(oneLeftIndex));
//                    oneRightText.setText(oneBean.getArrTime().get(oneRightIndex));
//                }
//            }
//        })
//                .setTitleText("上午")
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
//                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
//                .setContentTextSize(20)//滚轮文字大小
//                .build();
//        twoOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                if (options1 >= option2) {
//                    showToast("开始时间要大于结束时间");
//                } else {
//                    twoLeftIndex = options1;
//                    twoRightIndex = option2;
//                    twoLeftText.setText(twoBean.getArrTime().get(twoLeftIndex));
//                    twoRightText.setText(twoBean.getArrTime().get(twoRightIndex));
//                }
//            }
//        })
//                .setTitleText("下午")
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
//                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
//                .setContentTextSize(20)//滚轮文字大小
//                .build();
//        threeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                if (options1 >= option2) {
//                    showToast("开始时间要大于结束时间");
//                } else {
//                    threeLeftIndex = options1;
//                    threeRightIndex = option2;
//                    threeLeftText.setText(threeBean.getArrTime().get(threeLeftIndex));
//                    threeRightText.setText(threeBean.getArrTime().get(threeRightIndex));
//                }
//            }
//        })
//                .setTitleText("晚上")
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
//                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
//                .setContentTextSize(20)//滚轮文字大小
//                .build();
//        fourOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3, View v) {
//                if (options1 >= option2) {
//                    showToast("开始时间要大于结束时间");
//                } else {
//                    fourLeftIndex = options1;
//                    fourRightIndex = option2;
//                    fourLeftText.setText(fourBean.getArrTime().get(fourLeftIndex));
//                    fourRightText.setText(fourBean.getArrTime().get(fourRightIndex));
//                }
//            }
//        })
//                .setTitleText("凌晨")
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
//                .setCancelColor(getResources().getColor(R.color.theme_gray))//取消按钮文字颜色
//                .setContentTextSize(20)//滚轮文字大小
//                .build();
//    }
//
//    @Override
//    public void loadData() {
//        MyApplication.loadingDefault(activity());
//        HttpApi.app().getSystemPalnTime(this, new HttpCallback<String>() {
//            @Override
//            public void onSuccess(int code, String message, String data) {
//                MyApplication.hideLoading();
//                List<SystemPlanTimeItemBean> list = ConvertUtil.toList(data, SystemPlanTimeItemBean.class);
//                for (SystemPlanTimeItemBean item : list) {
//                    if (item.getTimeType() == 1) {
//                        oneBean = item;
//                    } else if (item.getTimeType() == 2) {
//                        twoBean = item;
//                    } else if (item.getTimeType() == 3) {
//                        threeBean = item;
//                    } else if (item.getTimeType() == 4) {
//                        fourBean = item;
//                    }
//                }
//                setView();
//            }
//
//            @Override
//            public void onFailed(HttpError error) {
//                MyApplication.hideLoading();
//                showToast(error.getErrMessage());
//            }
//        });
//    }
//
//    public void setView() {
//
//        if (oneBean != null && !oneBean.getArrTime().isEmpty()) {
//            oneLeftIndex = oneBean.getArrTime().indexOf(oneBean.getScheduleStartTime());
//            oneLeftIndex = oneLeftIndex >= 0 ? oneLeftIndex : 0;
//
//            oneRightIndex = oneBean.getArrTime().indexOf(oneBean.getScheduleEndTime());
//            oneRightIndex = oneRightIndex >= 0 ? oneRightIndex : (oneBean.getArrTime().size() - 1);
//
//            oneOptions.setNPicker(oneBean.getArrTime(), oneBean.getArrTime(), null);
//            oneOptions.setSelectOptions(oneLeftIndex, oneRightIndex);
//            oneLeftText.setText(oneBean.getArrTime().get(oneLeftIndex));
//            oneRightText.setText(oneBean.getArrTime().get(oneRightIndex));
//            oneLeftText.setTag(oneBean.getPlatformWorkingTimeId());
//            isOneChk = oneBean.isSetWorking();
//            oneChk.setImageResource(isOneChk ? R.mipmap.common_checkbox_select_icon
//                    : R.mipmap.common_checkbox_unselect_icon);
//
//            oneLayout.setVisibility(View.VISIBLE);
//        }
//        if (twoBean != null && !twoBean.getArrTime().isEmpty()) {
//            twoLeftIndex = twoBean.getArrTime().indexOf(twoBean.getScheduleStartTime());
//            twoLeftIndex = twoLeftIndex >= 0 ? twoLeftIndex : 0;
//
//            twoRightIndex = twoBean.getArrTime().indexOf(twoBean.getScheduleEndTime());
//            twoRightIndex = twoRightIndex >= 0 ? twoRightIndex : (twoBean.getArrTime().size() - 1);
//
//            twoOptions.setNPicker(twoBean.getArrTime(), twoBean.getArrTime(), null);
//            twoOptions.setSelectOptions(twoLeftIndex, twoRightIndex);
//            twoLeftText.setText(twoBean.getArrTime().get(twoLeftIndex));
//            twoRightText.setText(twoBean.getArrTime().get(twoRightIndex));
//            twoLeftText.setTag(twoBean.getPlatformWorkingTimeId());
//            isTwoChk = twoBean.isSetWorking();
//            twoChk.setImageResource(isTwoChk ? R.mipmap.common_checkbox_select_icon
//                    : R.mipmap.common_checkbox_unselect_icon);
//
//            twoLayout.setVisibility(View.VISIBLE);
//        }
//        if (threeBean != null && !threeBean.getArrTime().isEmpty()) {
//            threeLeftIndex = threeBean.getArrTime().indexOf(threeBean.getScheduleStartTime());
//            threeLeftIndex = threeLeftIndex >= 0 ? threeLeftIndex : 0;
//
//            threeRightIndex = threeBean.getArrTime().indexOf(threeBean.getScheduleEndTime());
//            threeRightIndex = threeRightIndex >= 0 ? threeRightIndex : (threeBean.getArrTime().size() - 1);
//
//            threeOptions.setNPicker(threeBean.getArrTime(), threeBean.getArrTime(), null);
//            threeOptions.setSelectOptions(threeLeftIndex, threeRightIndex);
//            threeLeftText.setText(threeBean.getArrTime().get(threeLeftIndex));
//            threeRightText.setText(threeBean.getArrTime().get(threeRightIndex));
//            threeLeftText.setTag(threeBean.getPlatformWorkingTimeId());
//            isThreeChk = threeBean.isSetWorking();
//            threeChk.setImageResource(isThreeChk ? R.mipmap.common_checkbox_select_icon
//                    : R.mipmap.common_checkbox_unselect_icon);
//
//            threeLayout.setVisibility(View.VISIBLE);
//        }
//
//        if (fourBean != null && !fourBean.getArrTime().isEmpty()) {
//            fourLeftIndex = fourBean.getArrTime().indexOf(fourBean.getScheduleStartTime());
//            fourLeftIndex = fourLeftIndex >= 0 ? fourLeftIndex : 0;
//
//            fourRightIndex = fourBean.getArrTime().indexOf(fourBean.getScheduleEndTime());
//            fourRightIndex = fourRightIndex >= 0 ? fourRightIndex : (fourBean.getArrTime().size() - 1);
//
//            fourOptions.setNPicker(fourBean.getArrTime(), fourBean.getArrTime(), null);
//            fourOptions.setSelectOptions(fourLeftIndex, fourRightIndex);
//            fourLeftText.setText(fourBean.getArrTime().get(fourLeftIndex));
//            fourRightText.setText(fourBean.getArrTime().get(fourRightIndex));
//            fourLeftText.setTag(fourBean.getPlatformWorkingTimeId());
//            isFourChk = fourBean.isSetWorking();
//            fourChk.setImageResource(isFourChk ? R.mipmap.common_checkbox_select_icon
//                    : R.mipmap.common_checkbox_unselect_icon);
//
//            fourLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnClick({R.id.custom_time_activity_one_chk, R.id.custom_time_activity_one_left_layout, R.id.custom_time_activity_one_right_layout,
//            R.id.custom_time_activity_two_chk, R.id.custom_time_activity_two_left_layout, R.id.custom_time_activity_two_right_layout,
//            R.id.custom_time_activity_three_chk, R.id.custom_time_activity_three_left_layout, R.id.custom_time_activity_three_right_layout,
//            R.id.custom_time_activity_four_chk, R.id.custom_time_activity_four_left_layout, R.id.custom_time_activity_four_right_layout,
//            R.id.custom_time_activity_save_btn})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.custom_time_activity_one_chk:
//                isOneChk = !isOneChk;
//                oneChk.setImageResource(isOneChk ? R.mipmap.common_checkbox_select_icon
//                        : R.mipmap.common_checkbox_unselect_icon);
//                break;
//            case R.id.custom_time_activity_one_left_layout:
//            case R.id.custom_time_activity_one_right_layout:
//                setBackground(1);
//                oneOptions.setSelectOptions(oneLeftIndex, oneRightIndex);
//                oneOptions.show();
//                break;
//            case R.id.custom_time_activity_two_chk:
//                isTwoChk = !isTwoChk;
//                twoChk.setImageResource(isTwoChk ? R.mipmap.common_checkbox_select_icon
//                        : R.mipmap.common_checkbox_unselect_icon);
//                break;
//            case R.id.custom_time_activity_two_left_layout:
//            case R.id.custom_time_activity_two_right_layout:
//                setBackground(2);
//                twoOptions.setSelectOptions(twoLeftIndex, twoRightIndex);
//                twoOptions.show();
//                break;
//            case R.id.custom_time_activity_three_chk:
//                isThreeChk = !isThreeChk;
//                threeChk.setImageResource(isThreeChk ? R.mipmap.common_checkbox_select_icon
//                        : R.mipmap.common_checkbox_unselect_icon);
//                break;
//            case R.id.custom_time_activity_three_left_layout:
//            case R.id.custom_time_activity_three_right_layout:
//                setBackground(3);
//                threeOptions.setSelectOptions(threeLeftIndex, threeRightIndex);
//                threeOptions.show();
//                break;
//            case R.id.custom_time_activity_four_chk:
//                isFourChk = !isFourChk;
//                fourChk.setImageResource(isFourChk ? R.mipmap.common_checkbox_select_icon
//                        : R.mipmap.common_checkbox_unselect_icon);
//                break;
//            case R.id.custom_time_activity_four_left_layout:
//            case R.id.custom_time_activity_four_right_layout:
//                setBackground(4);
//                fourOptions.setSelectOptions(fourLeftIndex, fourRightIndex);
//                fourOptions.show();
//                break;
//            case R.id.custom_time_activity_save_btn:
//                saveInfo();
//                break;
//        }
//    }
//
//    public void setBackground(int type) {
//        oneLeftLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        oneRightLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        twoLeftLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        twoRightLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        threeLeftLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        threeRightLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        fourLeftLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        fourRightLayout.setBackgroundResource(R.drawable.custom_time_item_gary_background_share);
//        switch (type) {
//            case 1:
//                oneLeftLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                oneRightLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                break;
//            case 2:
//                twoLeftLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                twoRightLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                break;
//            case 3:
//                threeLeftLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                threeRightLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                break;
//            case 4:
//                fourLeftLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                fourRightLayout.setBackgroundResource(R.drawable.custom_time_item_theme_background_share);
//                break;
//        }
//    }
//
//    public void saveInfo() {
//        MyApplication.loadingDefault(activity());
//        List<UploadCustomTimeItemBean> list = new ArrayList<>();
//        if (isOneChk) {
//            UploadCustomTimeItemBean oneBean = new UploadCustomTimeItemBean();
//            oneBean.setPlatformWorkingTimeId((Integer) oneLeftText.getTag());
//            oneBean.setScheduleStartTime(oneLeftText.getText().toString());
//            oneBean.setScheduleEndTime(oneRightText.getText().toString());
//            list.add(oneBean);
//        }
//        if (isTwoChk) {
//            UploadCustomTimeItemBean twoBean = new UploadCustomTimeItemBean();
//            twoBean.setPlatformWorkingTimeId((Integer) twoLeftText.getTag());
//            twoBean.setScheduleStartTime(twoLeftText.getText().toString());
//            twoBean.setScheduleEndTime(twoRightText.getText().toString());
//            list.add(twoBean);
//        }
//        if (isThreeChk) {
//            UploadCustomTimeItemBean threeBean = new UploadCustomTimeItemBean();
//            threeBean.setPlatformWorkingTimeId((Integer) threeLeftText.getTag());
//            threeBean.setScheduleStartTime(threeLeftText.getText().toString());
//            threeBean.setScheduleEndTime(threeRightText.getText().toString());
//            list.add(threeBean);
//        }
//        if (isFourChk) {
//            UploadCustomTimeItemBean fourBean = new UploadCustomTimeItemBean();
//            fourBean.setPlatformWorkingTimeId((Integer) fourLeftText.getTag());
//            fourBean.setScheduleStartTime(fourLeftText.getText().toString());
//            fourBean.setScheduleEndTime(fourRightText.getText().toString());
//            list.add(fourBean);
//        }
//        MyApplication.loadingDefault(activity());
//        BaseRequestBean bean = new BaseRequestBean();
//        bean.setBodyStr(ConvertUtil.toJsonString(list));
//        HttpApi.app().savePlanTime(this, bean, new HttpCallback<String>() {
//            @Override
//            public void onSuccess(int code, String message, String data) {
//                MyApplication.hideLoading();
//                showToast(message);
//                setResult(RESULT_OK);
//                finish();
//            }
//
//            @Override
//            public void onFailed(HttpError error) {
//                MyApplication.hideLoading();
//                showToast(error.getErrMessage());
//            }
//        });
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
}
