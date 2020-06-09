package module.main.counsel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.repository.http.entity.counsel.TimeItemBean;
import ui.MyGridView;

public class SelectTimePopWindow {
    private Page page;
    /**
     * 父View
     */
    protected View contentView;
    /**
     * 浮窗
     */
    protected PopupWindow mInstance;
    @BindView(R.id.select_sort_popwindow_gridView)
    MyGridView gridView;

    private SelectTimeAdapter selectTimeAdapter;

    private OnSelectTimeListener onSelectTimeListener;

    public void setOnSelectTimeListener(OnSelectTimeListener onSelectTimeListener) {
        this.onSelectTimeListener = onSelectTimeListener;
    }

    public SelectTimePopWindow(Page page) {
        this.page = page;
        contentView = LayoutInflater.from(page.context()).inflate(R.layout.select_sort_popwindow_layout, null, false);
        mInstance = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initView();
        initWindow();
    }

    public void initView() {
        ButterKnife.bind(this, contentView);
        selectTimeAdapter = new SelectTimeAdapter(page);
        gridView.setAdapter(selectTimeAdapter);
    }

    protected void initWindow() {
        mInstance.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mInstance.setOutsideTouchable(true);
        mInstance.setTouchable(true);
        mInstance.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onSelectTimeListener != null) {
                    onSelectTimeListener.onDismiss();
                }
            }
        });
    }

    public void setData(List<TimeItemBean> list) {
        selectTimeAdapter.refresh(list);
    }

    public void showPopUpWindow(View parent) {
        mInstance.showAsDropDown(parent, 0, 0);//设置弹框位置
    }

    @OnClick({R.id.select_sort_popwindow_reset_btn,
            R.id.select_sort_popwindow_confirm_btn,
            R.id.select_sort_popwindow_bottom_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_sort_popwindow_reset_btn:
                reset(true);
                break;
            case R.id.select_sort_popwindow_confirm_btn:
                if (onSelectTimeListener != null) {
                    onSelectTimeListener.onSelect(selectTimeAdapter.getIds(), true);
                }
                mInstance.dismiss();
                break;
            case R.id.select_sort_popwindow_bottom_layout:
                mInstance.dismiss();
                break;
        }
    }

    interface OnSelectTimeListener {
        void onSelect(List<Integer> ids, boolean isLoad);

        void onDismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = page.activity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        page.activity().getWindow().setAttributes(lp);
    }

    public void reset(boolean isLoad) {
        selectTimeAdapter.reset();
        if (onSelectTimeListener != null) {
            onSelectTimeListener.onSelect(new ArrayList<>(), isLoad);
        }
    }
}
