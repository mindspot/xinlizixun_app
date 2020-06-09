package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class SelectPhotoDialog extends BaseFullScreenDialog {

    @BindView(R.id.cancleLayout)
    LinearLayout cancleLayout;
    private OnOpenClickListener onOpenClickListener;

    public void setOnOpenClickListener(OnOpenClickListener onOpenClickListener) {
        this.onOpenClickListener = onOpenClickListener;
    }

    public SelectPhotoDialog(@NonNull Page page) {
        super(page);
        cancleLayout.setVisibility(View.VISIBLE);

    }

    public SelectPhotoDialog(@NonNull Page page, int show) {
        super(page);
        cancleLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView(View rootView) {
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_photo_dialog_layout;
    }

    @OnClick({R.id.openCamera, R.id.openPhotoAlbum, R.id.cancle})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        if (onOpenClickListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.openCamera:
                onOpenClickListener.OnOpenCarem();
                break;
            case R.id.openPhotoAlbum:
                onOpenClickListener.OnOpenAlbum();
                break;
            case R.id.cancle:
                onOpenClickListener.OnCancle();
                break;
        }
        dismiss();
    }

    public abstract static class OnOpenClickListener {
        public abstract void OnOpenCarem();

        public abstract void OnOpenAlbum();

        public void OnCancle() {
        }
    }
}

