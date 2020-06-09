package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.BaseFullScreenDialog;
import butterknife.OnClick;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class SelectMarriageDialog extends BaseFullScreenDialog {

    private OnSelectClickListener onSelectClickListener;

    public static final int STATUS_ONE = 1;
    public static final int STATUS_TWO = 2;
    public static final int STATUS_THREE = 3;

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public SelectMarriageDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView(View rootView) {
    }

    public void setData() {
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_marriage_dialog_layout;
    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.cancle})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        if (onSelectClickListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.one:
                onSelectClickListener.onClick(STATUS_ONE, "未婚");
                break;
            case R.id.two:
                onSelectClickListener.onClick(STATUS_TWO, "已婚");
                break;
            case R.id.three:
                onSelectClickListener.onClick(STATUS_THREE, "隐私");
                break;
            case R.id.cancle:
                break;
        }
        dismiss();
    }


    public abstract static class OnSelectClickListener {
        public abstract void onClick(int index, String value);
    }
}

