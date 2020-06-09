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

public class SelectSexDialog extends BaseFullScreenDialog {

    private OnSexClickListener onSexClickListener;

    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;

    public void setOnSexClickListener(OnSexClickListener onSexClickListener) {
        this.onSexClickListener = onSexClickListener;
    }

    public SelectSexDialog(@NonNull Page page) {
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
        return R.layout.select_sex_dialog_layout;
    }

    @OnClick({R.id.man, R.id.woman, R.id.cancle})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        if (onSexClickListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.man:
                onSexClickListener.onClick(SEX_MAN);
                break;
            case R.id.woman:
                onSexClickListener.onClick(SEX_WOMAN);
                break;
            case R.id.cancle:
                break;
        }
        dismiss();
    }


    public abstract static class OnSexClickListener {
        public abstract void onClick(int sex);
    }
}

