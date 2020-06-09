package common.webview.page;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paopao.paopaouser.R;

import base.BaseFragment;
import common.events.RefreshUIEvent;
import common.events.UIBaseEvent;
import common.webview.custom.BaiTiaoWebView;
import common.webview.custom.WebViewConfig;
import common.webview.custom.XWebView;
import de.greenrobot.event.EventBus;
import ui.bar.WebViewProgressBar;
import ui.title.ToolBarTitleView;

/**
 * Date: 2017/11/10
 * Email:
 * Description:
 * 发现功能 Webview 页面
 *
 * @author Administrator
 */
public class OtherWebFragment extends BaseFragment implements BaiTiaoWebView.WebViewPage {

    private String fragmentUrl;
    private ToolBarTitleView toolBarTitleView;
    private XWebView xWebView;

    private LinearLayout dialogView;
    private TextView tagContentText;

    public String getFragmentUrl() {
        return fragmentUrl;
    }

    public void setFragmentUrl(String fragmentUrl) {
        this.fragmentUrl = fragmentUrl;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.discover_main_web_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        toolBarTitleView = (ToolBarTitleView) rootView.findViewById(R.id.disconver_title);
        toolBarTitleView.setRightButtonImg(R.mipmap.icon_refresh);
        toolBarTitleView.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = xWebView.getUrl();
                if (TextUtils.isEmpty(url)) {
                    initXWebview();
                } else {
                    xWebView.reload();
                }
            }
        });

        xWebView = (XWebView) rootView.findViewById(R.id.disconver_xWebView);
        initXWebview();
        resetToolbarAfterXWebViewInvokeInitConfig();
        dialogView = (LinearLayout) rootView.findViewById(R.id.disconver_dialog_view);
        tagContentText = (TextView) rootView.findViewById(R.id.disconver_dialog_tagContent_text);
    }

    private void initXWebview() {
        if (xWebView != null && TextUtils.isEmpty(xWebView.getUrl())) {
            xWebView.initConfig(this, createWebViewConfig(), createUiHandler());
            xWebView.loadUrl();
        }
    }

    private WebViewConfig createWebViewConfig() {
        String otherUrl = fragmentUrl;
        return new WebViewConfig.Builder()
                .setUrl(otherUrl)
                .build();
    }

    @NonNull
    private BaiTiaoWebView.UIHandler createUiHandler() {
        return new BaiTiaoWebView.UIHandler() {
            @Override
            public void onPageStarted() {
                toolBarTitleView.hideCloseImg();
//                refreshView.onRefreshComplete();
            }

            @Override
            public void onPageFinished(BaiTiaoWebView view, String url) {
                toolBarTitleView.hideCloseImg();
                if (view.canGoBack()) {
                    toolBarTitleView.getLeft_btn_iv().setVisibility(View.VISIBLE);
                } else {
                    toolBarTitleView.getLeft_btn_iv().setVisibility(View.GONE);
                }
            }
        };
    }

    /**
     * tip: 一定要在xWebView.initConfig()之后调用，作用：覆盖掉XWebView默认设置
     */
    private void resetToolbarAfterXWebViewInvokeInitConfig() {
        toolBarTitleView.setTitle("发现");
        toolBarTitleView.getLeft_btn_iv().setVisibility(View.GONE);
        toolBarTitleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xWebView.goBack();
            }
        });
    }

    @Override
    public void loadData() {
        xWebView.loadUrl();
    }


    public void onBackPressed() {
//        xWebView.onBackPressed4Activity();
    }

    @Override
    public void onResume() {
        super.onResume();
        initXWebview();
        if (xWebView != null) {
            xWebView.onShow();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        xWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        xWebView.onActivityResult(requestCode, resultCode, data);
    }


    //************* WebViewPage *************

    @Override
    public ToolBarTitleView getToolBarTitleView() {
        return toolBarTitleView;
    }

    @Override
    public LinearLayout getDialogView() {
        return dialogView;
    }

    @Override
    public TextView getTvTagContent() {
        return tagContentText;
    }


    @Override
    public WebViewProgressBar getProgressBarHorizontal() {
        toolBarTitleView.showProgress();
        return toolBarTitleView.getProgress_bar_h();
    }

    @Override
    public BaiTiaoWebView getWebView() {
        return xWebView;
    }


    public void onEventMainThread(final RefreshUIEvent event) {
        if (event.getType() == UIBaseEvent.EVENT_LOGOUT) {
            xWebView.loadUrl();
        }
    }


}

