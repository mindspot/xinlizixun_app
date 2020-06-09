package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseFullScreenDialog;
import butterknife.OnClick;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class CouncilorPayDialog extends BaseFullScreenDialog {

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CouncilorPayDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView(View rootView) {
    }

    public void showMyDialog() {
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.councilor_order_pay_dialog_layout;
    }

    @OnClick({R.id.councilor_order_pay_dialog_cancle, R.id.councilor_order_pay_dialog_pay_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.councilor_order_pay_dialog_cancle:
                dismiss();
                break;
            case R.id.councilor_order_pay_dialog_pay_btn:
                if (onItemClickListener != null) {
                    onItemClickListener.onClick();
                }
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onClick();
    }
}

