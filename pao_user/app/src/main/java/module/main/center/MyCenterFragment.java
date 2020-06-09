package module.main.center;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.user.UserInfoResponseBean;
import common.repository.share_preference.SPApi;
import module.app.MyApplication;
import module.balance.BalanceActivity;
import util.MathOperationUtil;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class MyCenterFragment extends BaseFragment {
    @BindView(R.id.fragment_mycenter_username)
    TextView username;
    @BindView(R.id.fragment_mycenter_user_signature)
    TextView userSignature;
    @BindView(R.id.fragment_mycenter_user_photo)
    ImageView userPhoto;
    @BindView(R.id.fragment_mycenter_user_yue_text)
    TextView yueText;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mycenter;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void loadData() {
    }

    public void getAccount() {
        HttpApi.app().getUserInfo(this, new HttpCallback<UserInfoResponseBean>() {
            @Override
            public void onSuccess(int code, String message, UserInfoResponseBean data) {
                if (data != null) {
                    yueText.setText("¥" + MathOperationUtil.divStr(data.getAccount(), 100));
                    username.setText(data.getRealName());
                    userSignature.setText(data.getSendWord());
                    GlideImageLoader.loadImageCustomCorner(MyCenterFragment.this, data.getHeadImg(), userPhoto, ConvertUtil.dip2px(context(), 23));
                    return;
                }
                yueText.setText("¥" + MathOperationUtil.divStr(data.getAccount(), 100));
            }

            @Override
            public void onFailed(HttpError error) {
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        username.setText(SPApi.user().getLoginRealName());
        userSignature.setText(SPApi.user().getSendWork());
        GlideImageLoader.loadImageCustomCorner(this, SPApi.user().getUserPhoto(), userPhoto, ConvertUtil.dip2px(context(), 23));
        getAccount();
    }

    @OnClick({R.id.fragment_mycenter_user_layout, R.id.fragment_mycenter_yue_layout,
            R.id.fragment_mycenter_goumai_layout, R.id.fragment_mycenter_dingdan_layout,
            R.id.fragment_mycenter_youhui_layout, R.id.fragment_mycenter_zhuanjia_layout,
            R.id.fragment_mycenter_shoucang_layout,
            R.id.fragment_mycenter_jianyi_layout, R.id.fragment_mycenter_gongsi_layout,
            R.id.fragment_mycenter_lianxi_layout, R.id.fragment_mycenter_shezhi_layout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_mycenter_user_layout:
                UserInfoActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_yue_layout:
                BalanceActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_goumai_layout:
                PurchasePlanActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_dingdan_layout:
                OrderInfoActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_youhui_layout:
                CouponActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_zhuanjia_layout:
                toWebViewActivity(MyApplication.app.getServiceUrlResponseBean().getSetInUrl().getVal());
                break;
            case R.id.fragment_mycenter_shoucang_layout:
                CollectActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_jianyi_layout:
                FeedbackActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_gongsi_layout:
                AboutActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_lianxi_layout:
                ServicePhoneActivity.newIntent(this);
                break;
            case R.id.fragment_mycenter_shezhi_layout:
                SettingActivity.newIntent(this);
                break;
        }
    }
}
