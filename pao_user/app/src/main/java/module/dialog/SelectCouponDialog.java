package module.dialog;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;

import java.util.List;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.coupon.CouponItemBean;
import module.main.center.adapter.CouponInfoAdapter;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class SelectCouponDialog extends BaseFullScreenDialog {

    @BindView(R.id.select_coupon_dialog_listview)
    ListView listview;

    CouponInfoAdapter mAdapter;

    private boolean isUse = false;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SelectCouponDialog(@NonNull Page page) {
        super(page);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView(View rootView) {
        mAdapter = new CouponInfoAdapter(page);
        listview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListensr(new CouponInfoAdapter.OnItemClickListensr() {
            @Override
            public void onClick(CouponItemBean data) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(data);
                }
                dismiss();
            }
        });
    }

    public void initData(String money) {
        HttpApi.app().getUseCouponList(page, money, new HttpCallback<String>() {
            @Override
            public void onSuccess(int code, String message, String data) {
                if (StringUtil.isBlank(data)) {
                    return;
                }
                List<CouponItemBean> list = ConvertUtil.toList(data, CouponItemBean.class);
                if (list.isEmpty()) {
                    return;
                }
                mAdapter.refresh(list);
                isUse = true;
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }

    public void showMyDialog() {
        if (!isUse) {
            page.showToast("暂无可用优惠券");
            return;
        }
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.select_coupon_dialog_layout;
    }

    @OnClick(R.id.select_coupon_dialog_cancle)
    public void onViewClicked() {
        if(isDoubleClick()){
            return;
        }
        if (onItemClickListener != null) {
            onItemClickListener.cancle();
        }
        mAdapter.setSelectPosition(-1);
        mAdapter.notifyDataSetChanged();
        dismiss();
    }

    public interface OnItemClickListener {
        void onClick(CouponItemBean item);

        void cancle();
    }
}

