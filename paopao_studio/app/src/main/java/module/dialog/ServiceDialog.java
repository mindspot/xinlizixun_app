package module.dialog;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;
import module.app.MyApplication;
import permission.PermissionListener;
import permission.PermissionTipInfo;
import permission.PermissionsUtil;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class ServiceDialog extends BaseFullScreenDialog {

    @BindView(R.id.service_dialog_phone)
    TextView serviceDialogPhone;
    @BindView(R.id.service_dialog_time)
    TextView serviceDialogTime;

    String phone;
    String time;

    public ServiceDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initView(View rootView) {
        try {
            if (MyApplication.app.getServicePhoneResponseBean() != null &&
                    MyApplication.app.getServicePhoneResponseBean().getConsultantCustomers() != null
                    && !MyApplication.app.getServicePhoneResponseBean().getConsultantCustomers().isEmpty()) {
                phone = MyApplication.app.getServicePhoneResponseBean().getConsultantCustomers().get(0).getVal();
                time = MyApplication.app.getServicePhoneResponseBean().getCustomerServicePeriod().getVal();
            }
            serviceDialogPhone.setText("服务电话：" + phone.replace(" ", "-"));
            serviceDialogTime.setText("服务时间：" + time);
        } catch (Exception ex) {
        }
    }

    public void showMyDialog() {
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.service_dialog_layout;
    }

    @OnClick({R.id.service_dialog_cancle, R.id.service_dialog_call_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.service_dialog_cancle:
                break;
            case R.id.service_dialog_call_btn:
                PermissionTipInfo tip1 = PermissionTipInfo.getTip("拨打电话");
                PermissionsUtil.requestPermission(getContext(), new PermissionListener() {
                    @Override
                    public void permissionGranted(@NonNull String[] permission) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.trim().toString()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        page.startActivity(intent);
                    }

                    @Override
                    public void permissionDenied(@NonNull String[] permission) {
                        page.showToast("请打开拨打电话权限，否则无法使用该功能！");
                    }
                }, tip1, Manifest.permission.CALL_PHONE);
                break;
        }
        dismiss();
    }
}

