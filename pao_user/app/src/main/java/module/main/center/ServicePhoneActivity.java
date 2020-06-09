package module.main.center;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.entity.app.ServicePhoneResponseBean;
import im.IMHolder;
import module.app.MyApplication;
import permission.PermissionListener;
import permission.PermissionTipInfo;
import permission.PermissionsUtil;
import ui.title.ToolBarTitleView;

public class ServicePhoneActivity extends BaseActivity {

    @BindView(R.id.service_phone_activity_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.service_phone_activity_one_phone)
    TextView onePhone;
    @BindView(R.id.service_phone_activity_one_layout)
    LinearLayout oneLayout;
    @BindView(R.id.service_phone_activity_two_phone)
    TextView twoPhone;
    @BindView(R.id.service_phone_activity_two_layout)
    LinearLayout twoLayout;
    @BindView(R.id.service_phone_activity_three_phone)
    TextView threePhone;
    @BindView(R.id.service_phone_activity_three_layout)
    LinearLayout threeLayout;
    @BindView(R.id.service_phone_activity_four_phone)
    TextView fourPhone;
    @BindView(R.id.service_phone_activity_four_layout)
    LinearLayout fourLayout;
    @BindView(R.id.service_phone_activity_five_phone)
    TextView fivePhone;
    @BindView(R.id.service_phone_activity_five_layout)
    LinearLayout fiveLayout;
    @BindView(R.id.service_phone_activity_six_phone)
    TextView sixPhone;
    @BindView(R.id.service_phone_activity_six_layout)
    LinearLayout sixLayout;
    @BindView(R.id.service_phone_activity_service_one)
    TextView serviceOne;
    @BindView(R.id.service_phone_activity_service_two)
    TextView serviceTwo;
    @BindView(R.id.service_phone_activity_service_three)
    TextView serviceThree;
    @BindView(R.id.service_phone_activity_service_four)
    TextView serviceFour;
    @BindView(R.id.service_phone_activity_service_five)
    TextView serviceFive;
    @BindView(R.id.service_phone_activity_service_six)
    TextView serviceSix;

    public static void newIntent(Page page) {
        Intent intent = new Intent(page.activity(), ServicePhoneActivity.class);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_service_phone;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        try {
            List<ServicePhoneResponseBean.DataBean> phone = MyApplication.app.getServicePhoneResponseBean().getUserCustomers();
            List<ServicePhoneResponseBean.DataBean> service = MyApplication.app.getServicePhoneResponseBean().getUserCustomersNow();
            if (phone.size() > 0) {
                ServicePhoneResponseBean.DataBean bean = phone.get(0);
                oneLayout.setVisibility(View.VISIBLE);
                onePhone.setText(bean.getVal());
                onePhone.setTag(bean.getVal());
                setTextViewLine(onePhone);
            }
            if (phone.size() > 1) {
                ServicePhoneResponseBean.DataBean bean = phone.get(1);
                twoLayout.setVisibility(View.VISIBLE);
                twoPhone.setText(bean.getVal());
                twoPhone.setTag(bean.getVal());
                setTextViewLine(twoPhone);
            }
            if (phone.size() > 2) {
                ServicePhoneResponseBean.DataBean bean = phone.get(2);
                threeLayout.setVisibility(View.VISIBLE);
                threePhone.setText(bean.getVal());
                threePhone.setTag(bean.getVal());
                setTextViewLine(threePhone);
            }
            if (phone.size() > 3) {
                ServicePhoneResponseBean.DataBean bean = phone.get(3);
                fourLayout.setVisibility(View.VISIBLE);
                fourPhone.setText(bean.getVal());
                fourPhone.setTag(bean.getVal());
                setTextViewLine(fourPhone);
            }
            if (phone.size() > 4) {
                ServicePhoneResponseBean.DataBean bean = phone.get(4);
                fiveLayout.setVisibility(View.VISIBLE);
                fivePhone.setText(bean.getVal());
                fivePhone.setTag(bean.getVal());
                setTextViewLine(fivePhone);
            }
            if (phone.size() > 5) {
                ServicePhoneResponseBean.DataBean bean = phone.get(5);
                sixLayout.setVisibility(View.VISIBLE);
                sixPhone.setText(bean.getVal());
                sixPhone.setTag(bean.getVal());
                setTextViewLine(sixPhone);
            }

            if (service.size() > 0) {
                ServicePhoneResponseBean.DataBean bean = service.get(0);
                serviceOne.setVisibility(View.VISIBLE);
                serviceOne.setTag(bean.getVal());
                setTextViewLine(serviceOne);
            }
            if (service.size() > 1) {
                ServicePhoneResponseBean.DataBean bean = service.get(1);
                serviceTwo.setVisibility(View.VISIBLE);
                serviceTwo.setTag(bean.getVal());
                setTextViewLine(serviceTwo);
            }
            if (service.size() > 2) {
                ServicePhoneResponseBean.DataBean bean = service.get(2);
                serviceThree.setVisibility(View.VISIBLE);
                serviceThree.setTag(bean.getVal());
                setTextViewLine(serviceThree);
            }
            if (service.size() > 3) {
                ServicePhoneResponseBean.DataBean bean = service.get(3);
                serviceFour.setVisibility(View.VISIBLE);
                serviceFour.setTag(bean.getVal());
                setTextViewLine(serviceFour);
            }
            if (service.size() > 4) {
                ServicePhoneResponseBean.DataBean bean = service.get(4);
                serviceFive.setVisibility(View.VISIBLE);
                serviceFive.setTag(bean.getVal());
                setTextViewLine(serviceFive);
            }
            if (service.size() > 5) {
                ServicePhoneResponseBean.DataBean bean = service.get(5);
                serviceSix.setVisibility(View.VISIBLE);
                serviceSix.setTag(bean.getVal());
                setTextViewLine(serviceSix);
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.service_phone_activity_one_phone, R.id.service_phone_activity_two_phone, R.id.service_phone_activity_three_phone, R.id.service_phone_activity_four_phone, R.id.service_phone_activity_five_phone, R.id.service_phone_activity_six_phone, R.id.service_phone_activity_service_one, R.id.service_phone_activity_service_two, R.id.service_phone_activity_service_three, R.id.service_phone_activity_service_four, R.id.service_phone_activity_service_five, R.id.service_phone_activity_service_six})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.service_phone_activity_one_phone:
            case R.id.service_phone_activity_two_phone:
            case R.id.service_phone_activity_three_phone:
            case R.id.service_phone_activity_four_phone:
            case R.id.service_phone_activity_five_phone:
            case R.id.service_phone_activity_six_phone:
                callPhone(view.getTag().toString());
                break;
            case R.id.service_phone_activity_service_one:
                gotoChat(view.getTag().toString(), 1);
                break;
            case R.id.service_phone_activity_service_two:
                gotoChat(view.getTag().toString(), 2);
                break;
            case R.id.service_phone_activity_service_three:
                gotoChat(view.getTag().toString(), 3);
                break;
            case R.id.service_phone_activity_service_four:
                gotoChat(view.getTag().toString(), 4);
                break;
            case R.id.service_phone_activity_service_five:
                gotoChat(view.getTag().toString(), 5);
                break;
            case R.id.service_phone_activity_service_six:
                gotoChat(view.getTag().toString(), 6);
                break;
        }
    }

    public void callPhone(String phone) {
        PermissionTipInfo tip1 = PermissionTipInfo.getTip("拨打电话");
        PermissionsUtil.requestPermission(context(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.trim().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showToast("请打开拨打电话权限，否则无法使用该功能！");
            }
        }, tip1, Manifest.permission.CALL_PHONE);
    }

    public void setTextViewLine(TextView tvTest) {
        tvTest.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvTest.getPaint().setAntiAlias(true);//抗锯齿
    }

    public void gotoChat(String userid, int index) {
        IMHolder.getInstance().gotoChat(context(), userid, "在线客服" + index, "");
    }
}
