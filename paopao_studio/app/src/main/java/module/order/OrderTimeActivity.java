package module.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderBuyEvent;
import common.repository.bean.OrderTimeBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.order.WorkTimeInfoItemBean;
import common.repository.http.entity.order.WorkTimeItemBean;
import de.greenrobot.event.EventBus;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;
import util.TimeUtils;

public class OrderTimeActivity extends BaseActivity {

    @BindView(R.id.order_time_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.order_time_activity_week_one_text)
    TextView weekOneText;
    @BindView(R.id.order_time_activity_week_two_text)
    TextView weekTwoText;
    @BindView(R.id.order_time_activity_week_three_text)
    TextView weekThreeText;
    @BindView(R.id.order_time_activity_week_four_text)
    TextView weekFourText;
    @BindView(R.id.order_time_activity_week_five_text)
    TextView weekFiveText;
    @BindView(R.id.order_time_activity_week_six_text)
    TextView weekSixText;
    @BindView(R.id.order_time_activity_day_one_text)
    TextView dayOneText;
    @BindView(R.id.order_time_activity_day_two_text)
    TextView dayTwoText;
    @BindView(R.id.order_time_activity_day_three_text)
    TextView dayThreeText;
    @BindView(R.id.order_time_activity_day_four_text)
    TextView dayFourText;
    @BindView(R.id.order_time_activity_day_five_text)
    TextView dayFiveText;
    @BindView(R.id.order_time_activity_day_six_text)
    TextView daySixText;
    @BindView(R.id.order_time_activity_day_seven_text)
    TextView daySevenText;
    @BindView(R.id.order_time_activity_gridview)
    GridView gridview;
    @BindView(R.id.order_time_activity_tip_layout)
    LinearLayout tipLayout;
    @BindView(R.id.order_time_activity_time_text)
    TextView timeText;
    @BindView(R.id.order_time_activity_way_text)
    TextView wayText;
    @BindView(R.id.order_time_activity_next_layout)
    LinearLayout nextLayout;
    @BindView(R.id.order_time_activity_week_seven_text)
    TextView weekSevenText;
    @BindView(R.id.order_time_activity_month_one_text)
    TextView monthOneText;
    @BindView(R.id.order_time_activity_month_two_text)
    TextView monthTwoText;
    @BindView(R.id.order_time_activity_three_text)
    TextView monthThreeText;
    @BindView(R.id.order_time_activity_month_four_text)
    TextView monthFourText;
    @BindView(R.id.order_time_activity_month_five_text)
    TextView monthFiveText;
    @BindView(R.id.order_time_activity_month_six_text)
    TextView monthSixText;
    @BindView(R.id.order_time_activity_month_seven_text)
    TextView monthSevenText;

    private OrderTimeBean orderBean;

    private OrderChooseTimeAdapter mAdapter;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), OrderTimeActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_order_time;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mAdapter = new OrderChooseTimeAdapter(this);
        gridview.setAdapter(mAdapter);
        initView();
    }

    public void initView() {
        tipLayout.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.GONE);

        wayText.setText(MyApplication.app.getWorkInfoBean().getMode());
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAdapter.setOnItemListener(new OrderChooseTimeAdapter.OnItemListener() {
            @Override
            public void OnClick(WorkTimeItemBean bean) {
                orderBean = new OrderTimeBean();
                String[] dates = bean.getDateStatus().split("-");
                orderBean.setTime(bean.getConsultantWorkStartTime() + "-" + bean.getConsultantWorkEndTime());
                orderBean.setDay(dates[2]);
                orderBean.setMonth(dates[1]);
                orderBean.setYear(dates[0]);
                timeText.setText(orderBean.getMonth() + "月" + orderBean.getDay() + "日 " + orderBean.getTime());
                MyApplication.app.setOrderTimeBean(orderBean);
                tipLayout.setVisibility(View.GONE);
                nextLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void loadData() {
        getData();
    }

    public void getData() {
        MyApplication.loadingDefault(activity());
        OrderTimeBean bean = TimeUtils.getDay(0);
        HttpApi.app().getTime(this, MyApplication.app.getPersonalInforBean().getId(),
                bean.getYear() + "-" + bean.getMonth() + "-" + bean.getDay(), new HttpCallback<String>() {
                    @Override
                    public void onSuccess(int code, String message, String data) {
                        MyApplication.hideLoading();
                        setView(data);
                    }

                    @Override
                    public void onFailed(HttpError error) {
                        showToast(error.getErrMessage());
                        MyApplication.hideLoading();
                    }
                });
    }

    public void setView(String data) {
        Map<String, WorkTimeInfoItemBean> map = JSON.parseObject(data, new TypeReference<LinkedHashMap<String, WorkTimeInfoItemBean>>() {
        });
        List<WorkTimeItemBean> list = new ArrayList<>();
        int index = 1;
        int j;
        for (int i = 0; i < index; i++) {
            j = 0;
            for (Map.Entry<String, WorkTimeInfoItemBean> entry : map.entrySet()) {
                WorkTimeInfoItemBean bean = map.get(entry.getKey());
                if (i == 0) {
                    String[] dates = entry.getKey().split("-");
                    String month = Integer.parseInt(dates[1]) + "月";
                    switch (j) {
                        case 0:
                            weekOneText.setText("今天");
                            dayOneText.setText(dates[2]);
                            monthOneText.setText(month);
                            break;
                        case 1:
                            weekTwoText.setText(bean.getWeek());
                            dayTwoText.setText(dates[2]);
                            monthTwoText.setText(month);
                            monthTwoText.setVisibility(month.equals(monthOneText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                        case 2:
                            weekThreeText.setText(bean.getWeek());
                            dayThreeText.setText(dates[2]);
                            monthThreeText.setText(month);
                            monthThreeText.setVisibility(month.equals(monthTwoText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                        case 3:
                            weekFourText.setText(bean.getWeek());
                            dayFourText.setText(dates[2]);
                            monthFourText.setText(month);
                            monthFourText.setVisibility(month.equals(monthThreeText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                        case 4:
                            weekFiveText.setText(bean.getWeek());
                            dayFiveText.setText(dates[2]);
                            monthFiveText.setText(month);
                            monthFiveText.setVisibility(month.equals(monthFourText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                        case 5:
                            weekSixText.setText(bean.getWeek());
                            daySixText.setText(dates[2]);
                            monthSixText.setText(month);
                            monthSixText.setVisibility(month.equals(monthFiveText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                        case 6:
                            weekSevenText.setText(bean.getWeek());
                            daySevenText.setText(dates[2]);
                            monthSevenText.setText(month);
                            monthSevenText.setVisibility(month.equals(monthSixText.getText().toString())
                                    ? View.INVISIBLE : View.VISIBLE);
                            break;
                    }
                    if (bean.getList() != null && bean.getList().size() > index) {
                        index = bean.getList().size();
                    }
                }
                WorkTimeItemBean itemBean = new WorkTimeItemBean();
                if (bean.getList() == null || bean.getList().isEmpty()) {
                    itemBean.setType(i == 0 ? 1 : 2);
                } else if (bean.getList().size() > i) {
                    itemBean = bean.getList().get(i);
                } else {
                    itemBean.setType(2);
                }
                list.add(itemBean);
                j++;
            }
        }
        mAdapter.refresh(list);
    }

    @OnClick(R.id.order_time_activity_next_btn)
    public void onViewClicked() {
        if (isDoubleClick()) {
            return;
        }
        OrderUserInfoActivity.newIntent(this);
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
