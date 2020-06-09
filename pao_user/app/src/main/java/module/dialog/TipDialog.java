package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class TipDialog extends BaseFullScreenDialog {

    @BindView(R.id.tip_dialog_title)
    TextView title;
    @BindView(R.id.tip_dialog_content_text)
    TextView contentText;
    @BindView(R.id.service_dialog_cancle)
    TextView cancle;
    @BindView(R.id.service_dialog_call_btn)
    TextView confirmBtn;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TipDialog(@NonNull Page page) {
        super(page);
    }

    @Override
    protected void initView(View rootView) {
        title.setVisibility(View.GONE);
        contentText.setVisibility(View.GONE);
        cancle.setVisibility(View.GONE);
        confirmBtn.setVisibility(View.GONE);
    }

    public void setTitle(String titleStr) {
        title.setVisibility(View.VISIBLE);
        title.setText(titleStr);
    }

    public void setContent(String cntentStr) {
        contentText.setVisibility(View.VISIBLE);
        contentText.setText(cntentStr);
    }

    public void setCancle(String cancleStr) {
        cancle.setVisibility(View.VISIBLE);
        cancle.setText(cancleStr);
    }

    public void setConfirm(String confirmStr) {
        confirmBtn.setVisibility(View.VISIBLE);
        confirmBtn.setText(confirmStr);
    }

    public void showMyDialog() {
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tip_dialog_layout;
    }

    @OnClick({R.id.service_dialog_cancle, R.id.service_dialog_call_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.service_dialog_cancle:
                if (onItemClickListener != null) {
                    onItemClickListener.cancle();
                }
                break;
            case R.id.service_dialog_call_btn:
                if (onItemClickListener != null) {
                    onItemClickListener.confirm();
                }
                break;
        }
        dismiss();
    }

    public interface OnItemClickListener {
        void confirm();

        void cancle();
    }
}

