package module.main.counsel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.repository.http.entity.counsel.ComprehensiveBean;
import ui.DoubleSlideSeekBar;
import util.MathOperationUtil;

public class SelectFiltratePopWindow {
    @BindView(R.id.select_filtrate_popwindow_min_text)
    TextView minText;
    @BindView(R.id.select_filtrate_popwindow_max_text)
    TextView maxText;
    @BindView(R.id.select_filtrate_popwindow_seekbar)
    DoubleSlideSeekBar seekbar;
    @BindView(R.id.select_filtrate_popwindow_sex_gridview)
    GridView sexGridview;
    @BindView(R.id.select_filtrate_popwindow_age_gridview)
    GridView ageGridview;
    @BindView(R.id.select_filtrate_popwindow_money_layout)
    LinearLayout moneyLayout;
    private Page page;
    /**
     * 父View
     */
    protected View contentView;
    /**
     * 浮窗
     */
    protected PopupWindow mInstance;

    private ComprehensiveBean mData;

    private SelectSexAdapter sexAdapter;
    private SelectAgeAdapter ageAdapter;

    private int mMin;
    private int mMax;

    private OnSelectFiltrateListener onSelectFiltrateListener;

    public void setOnSelectFiltrateListener(OnSelectFiltrateListener onSelectFiltrateListener) {
        this.onSelectFiltrateListener = onSelectFiltrateListener;
    }

    public SelectFiltratePopWindow(Page page) {
        this.page = page;
        contentView = LayoutInflater.from(page.context()).inflate(R.layout.select_filtrate_popwindow_layout, null, false);
        mInstance = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initView();
        initWindow();
    }

    public void initView() {
        ButterKnife.bind(this, contentView);
        sexAdapter = new SelectSexAdapter(page);
        sexGridview.setAdapter(sexAdapter);
        ageAdapter = new SelectAgeAdapter(page);
        ageGridview.setAdapter(ageAdapter);
    }

    protected void initWindow() {
        mInstance.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mInstance.setOutsideTouchable(true);
        mInstance.setTouchable(true);
        mInstance.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onSelectFiltrateListener != null) {
                    onSelectFiltrateListener.onDismiss();
                }
            }
        });
        seekbar.setOnRangeListener(new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(float low, float big) {
                mMin = (int) low;
                mMax = (int) big;
                minText.setText(String.valueOf(mMin));
                maxText.setText(String.valueOf(mMax));
            }
        });
    }

    public void setData(ComprehensiveBean data) {
        this.mData = data;
        moneyLayout.setVisibility(View.GONE);
        if (data.getMaxPrice() > 0) {
            minText.setText(MathOperationUtil.divStr(data.getMinPrice(), 100));
            maxText.setText(MathOperationUtil.divStr(data.getMaxPrice(), 100));
            seekbar.setValue(data.getMinPrice(), data.getMaxPrice());
            mMin = data.getMinPrice();
            mMax = data.getMaxPrice();
            moneyLayout.setVisibility(View.GONE);
        }
        sexAdapter.refresh(data.getConsultationSex());
        ageAdapter.refresh(data.getConsultationAge());
    }

    public void showPopUpWindow(View parent) {
        mInstance.showAsDropDown(parent, 0, 0);//设置弹框位置
    }

    @OnClick({R.id.select_filtrate_popwindow_reset_btn, R.id.select_filtrate_popwindow_confirm_btn, R.id.select_filtrate_popwindow_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_filtrate_popwindow_reset_btn:
                reset(true);
                break;
            case R.id.select_filtrate_popwindow_confirm_btn:
                if (onSelectFiltrateListener != null) {
                    onSelectFiltrateListener.onSelect(mMin, mMax, sexAdapter.getIds(), ageAdapter.getIds(), true);
                }
                mInstance.dismiss();
                break;
            case R.id.select_filtrate_popwindow_view:
                mInstance.dismiss();
                break;
        }
    }

    interface OnSelectFiltrateListener {
        void onSelect(int min, int max, List<Integer> sexIds, List<Integer> ageIds, boolean isLoad);

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
        if (mData.getMaxPrice() > 0)
            seekbar.resetValue(mData.getMinPrice(), mData.getMaxPrice());
        mMin = mData.getMinPrice();
        mMax = mData.getMaxPrice();
        minText.setText(MathOperationUtil.divStr(mMin, 100));
        maxText.setText(MathOperationUtil.divStr(mMax, 100));
        sexAdapter.reset();
        ageAdapter.reset();
        if (onSelectFiltrateListener != null) {
            onSelectFiltrateListener.onSelect(mMin, mMax, sexAdapter.getIds(), ageAdapter.getIds(), isLoad);
        }
    }
}
