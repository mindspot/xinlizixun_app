package module.main.counsel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.CityItemBean;
import module.app.MyApplication;

public class SelectCityPopWindow {
    @BindView(R.id.select_city_popwindow_listview1)
    ListView listview1;
    @BindView(R.id.select_city_popwindow_listview2)
    ListView listview2;
    private Page page;
    /**
     * 父View
     */
    protected View contentView;
    /**
     * 浮窗
     */
    protected PopupWindow mInstance;

    private List<CityItemBean> mDatas;
    private SelectCityAdapter adapter1;
    private SelectCityAdapter adapter2;

    private CityItemBean province, city;

    private OnSelectCityListener onSelectCityListener;

    private boolean isSuccess = false;

    public void setOnSelectCityListener(OnSelectCityListener onSelectCityListener) {
        this.onSelectCityListener = onSelectCityListener;
    }

    public SelectCityPopWindow(Page page) {
        this.page = page;
        contentView = LayoutInflater.from(page.context()).inflate(R.layout.select_city_popwindow_layout, null, false);
        mInstance = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        initView();
        initWindow();
        initListener();
        getData(false);
    }

    private void initView() {
        ButterKnife.bind(this, contentView);
        adapter1 = new SelectCityAdapter(page);
        listview1.setAdapter(adapter1);
        adapter2 = new SelectCityAdapter(page);
        listview2.setAdapter(adapter2);
    }

    private void initWindow() {
        mInstance.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mInstance.setOutsideTouchable(true);
        mInstance.setTouchable(true);
        mInstance.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onSelectCityListener != null) {
                    onSelectCityListener.onDismiss();
                }
            }
        });
    }

    private void initListener() {
        adapter1.setOnItemClickListener(new SelectCityAdapter.OnItemClickListener() {
            @Override
            public void onClick(CityItemBean item) {
                adapter2.setSelectId(-1);
                adapter2.refresh(item.getDictAreas());
                province = item;
            }
        });
        adapter2.setOnItemClickListener(new SelectCityAdapter.OnItemClickListener() {
            @Override
            public void onClick(CityItemBean item) {
                city = item;
                if (onSelectCityListener != null) {
                    onSelectCityListener.onSelect(province, city);
                }
            }
        });
    }

    public void showPopUpWindow(View parent) {
        if (!isSuccess) {
            getData(true);
        }
        mInstance.showAsDropDown(parent, 0, 0);//设置弹框位置
    }

    @OnClick({R.id.select_city_popwindow_cancle_btn, R.id.select_city_popwindow_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_city_popwindow_cancle_btn:
                adapter1.setSelectId(-1);
                adapter1.refresh(mDatas);
                adapter2.setSelectId(-1);
                adapter2.clear();
                province = null;
                city = null;
                if (onSelectCityListener != null) {
                    onSelectCityListener.onSelect(province, city);
                }
                break;
        }
        mInstance.dismiss();
    }

    interface OnSelectCityListener {
        void onSelect(CityItemBean province, CityItemBean city);

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

    public void getData(boolean isShow) {
        if (isShow) {
            MyApplication.loadingDefault(page.activity());
        }
        HttpApi.app().getCityInfos(page, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (isShow) {
                    MyApplication.hideLoading();
                }
                isSuccess = true;
                List<CityItemBean> list = ConvertUtil.toList(data, CityItemBean.class);
                adapter1.refresh(list);
            }

            @Override
            public void onFailed(HttpError error) {
                isSuccess = false;
                if (isShow) {
                    MyApplication.hideLoading();
                    page.showToast(error.getErrMessage());
                }
            }
        });
    }
}
