package base;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.framework.utils.ToastUtil;
import com.framework.utils.ViewUtil;
import com.paopao.paopaostudio.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import common.repository.http.HttpApi;
import common.router.CommandRouter;
import common.webview.custom.BaiTiaoWebView;
import common.webview.page.WebViewActivity;
import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import me.jessyan.autosize.internal.CustomAdapt;
import module.app.MyApplication;
import util.TimeUtils;

/**
 * Fragment的基类
 *
 * @author Administrator
 */
public abstract class BaseFragment extends Fragment implements Page.FragmentPage, CustomAdapt {

    protected View rootView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView(savedInstanceState);
        initListener();
        initTitle();
        loadData();
        return rootView;
    }

    @Override
    public void onShow() {

    }

    protected abstract int getContentViewId();

    protected abstract void initView(Bundle savedInstanceState);

    protected void initListener() {
    }

    public void initTitle() {
    }

    protected abstract void loadData();


    // ************** life cycle **************

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        if (getActivity().getCurrentFocus() != null) {
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        CommandRouter.detached(this);
        HttpApi.cancelRequest(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CommandRouter.onActivityResult(this, requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // ************** page **************

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public Activity activity() {
        return getActivity();
    }

    @Override
    public Intent getIntent() {
        return getActivity().getIntent();
    }

    @Override
    public ComponentName startService(Intent service) {
        return getActivity().startService(service);
    }

    @Override
    public void showToast(String message) {
        if (!StringUtil.isBlank(message) && !ViewUtil.isFinishedPage(this)) {
            ToastUtil.show(getActivity(), message);
        }
    }

    @Override
    public <T extends View> T $(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }

    @Override
    public View root() {
        return rootView;
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 375;
    }

    public StatusLayoutManager statusLayoutManager;

    public void initDefultStatusLayout(View contentLayout, OnStatusChildClickListener onStatusChildClickListener) {
        statusLayoutManager = new StatusLayoutManager.Builder(contentLayout)
                // 自定义布局
                .setLoadingLayout(R.layout.custom_statusview_loading_layout)
                .setEmptyLayout(R.layout.custom_statusview_empty_layout)
                .setErrorLayout(R.layout.custom_statusview_error_layout)
                .setEmptyClickViewID(R.id.emptyClickLayout)
                .setErrorClickViewID(R.id.errorClickLayout)
                .setOnStatusChildClickListener(onStatusChildClickListener)
                .build();
    }

    public void initCustomStatusLayout(View contentLayout, int loadingLayout, int emptyLayout, int errorLayout,
                                       int emptyClickView, int errorClickView, OnStatusChildClickListener onStatusChildClickListener) {
        StatusLayoutManager.Builder build = new StatusLayoutManager.Builder(contentLayout);
        // 自定义布局
        build.setLoadingLayout(loadingLayout);
        build.setEmptyLayout(emptyLayout);
        build.setErrorLayout(errorLayout);
        build.setEmptyClickViewID(emptyClickView);
        build.setErrorClickViewID(errorClickView);
        build.setOnStatusChildClickListener(onStatusChildClickListener);
        statusLayoutManager = build.build();
    }

    public void showLoadLayout() {
        MyApplication.loadingDefault(activity());
        if (statusLayoutManager != null) {
            statusLayoutManager.showLoadingLayout();
        }
    }

    public void showEmptyLayout() {
        MyApplication.hideLoading();
        if (statusLayoutManager != null) {
            statusLayoutManager.showEmptyLayout();
        }
    }

    public void showErrorLayout() {
        MyApplication.hideLoading();
        if (statusLayoutManager != null) {
            statusLayoutManager.showErrorLayout();
        }
    }

    public void showSuccessLayout() {
        MyApplication.hideLoading();
        if (statusLayoutManager != null) {
            statusLayoutManager.showSuccessLayout();
        }
    }

    public void toWebViewActivity(String url) {
        Intent intent = new Intent(activity(), WebViewActivity.class);
        intent.putExtra(BaiTiaoWebView.EXTRA_URL, url);
        activity().startActivity(intent);
    }

    private long lastClickTime;

    public boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TimeUtils.DOUBLE_CLICK_TIME) {
            lastClickTime = time;
            return false;
        }
        return true;
    }
}
