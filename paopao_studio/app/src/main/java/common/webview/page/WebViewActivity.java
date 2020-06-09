package common.webview.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import common.webview.custom.BaiTiaoWebView;
import common.webview.custom.WebViewConfig;
import common.webview.custom.XWebView;
import ui.bar.WebViewProgressBar;
import ui.title.ToolBarTitleView;
import util.ShapeUtils;

/**
 * @author Administrator
 */
public class WebViewActivity extends BaseActivity implements BaiTiaoWebView.WebViewPage {
    protected ToolBarTitleView toolBarTitleView;

    private XWebView xWebView;

    private LinearLayout dialogView;
    private TextView tagContentText;

    private ShapeUtils shapeUtils;
    private String mUrl;
    private String mTitle;
    private String mContent;

    public static void newIntent(Page page, String url) {
        newIntent(page, url, "", "");
    }

    public static void newIntent(Page page, String url, String title, String content) {
        Intent intent = new Intent(page.activity(), WebViewActivity.class);
        intent.putExtra(BaiTiaoWebView.EXTRA_URL, url);
        intent.putExtra("mTitle", title);
        intent.putExtra("mContent", content);
        page.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWebView != null) {
            xWebView.onShow();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.webview_loan_activity;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolBarTitleView = (ToolBarTitleView) findViewById(R.id.title);

        dialogView = (LinearLayout) findViewById(R.id.dialog_view);
        tagContentText = (TextView) findViewById(R.id.tv_tag_content);

        xWebView = (XWebView) findViewById(R.id.web_view);
        xWebView.initConfig(this, createWebViewConfig(), null);

        shapeUtils = new ShapeUtils(this);
    }

    private WebViewConfig createWebViewConfig() {
        mUrl = getIntent().getStringExtra(BaiTiaoWebView.EXTRA_URL);
        boolean isPush = getIntent().getBooleanExtra(BaiTiaoWebView.EXTRA_IS_PUSH, false);
        String jumpToHome = getIntent().getStringExtra(BaiTiaoWebView.EXTRA_JUMP_TO_HOME);
        String authMethod = getIntent().getStringExtra(BaiTiaoWebView.EXTRA_AUTH_METHOD);

        mTitle = getIntent().getStringExtra("mTitle");
        mContent = getIntent().getStringExtra("mContent");
        toolBarTitleView.getRight_btn_iv().setVisibility("分享".equals(mTitle) ? View.VISIBLE : View.GONE);

        return new WebViewConfig.Builder()
                .setUrl(mUrl).setTitle(mTitle)
                .setIsPush(isPush).setJumpToHome(jumpToHome)
                .setAuthMethod(authMethod)
                .build();
    }

    @Override
    public void initListener() {
        toolBarTitleView.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtil.isBlank(mTitle)) {
                    shapeUtils.shareWebURL(mUrl, mTitle, mContent);
                }
            }
        });
    }

    @Override
    public void loadData() {
        xWebView.loadUrl();
    }


    @Override
    public void onBackPressed() {
        xWebView.onBackPressed4Activity();
    }

    @Override
    protected void onDestroy() {
        xWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


}
