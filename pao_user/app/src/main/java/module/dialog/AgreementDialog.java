package module.dialog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;

import base.BaseFullScreenDialog;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.share_preference.SPApi;
import common.webview.page.WebViewActivity;

/**
 * Created by hpzhan on 2019/4/12.
 */

public class AgreementDialog extends BaseFullScreenDialog {

    @BindView(R.id.agreement_content_text)
    TextView agreementContentText;

    public AgreementDialog(@NonNull Page page) {
        super(page);
    }

    @Override
    protected void initView(View rootView) {
        setCancelable(false);

        agreementContentText.setText("");
        agreementContentText.append("请您务必审慎阅读、充分理解“服务协议”和“隐私政策”各条款，包括但不限于：为了向您提供咨询服务、内容分享等服务，我们需要收集您的设备信息等个人信息。您可阅读");
        agreementContentText.append(getOnCickSpan("《服务协议》", "file:///android_asset/register_agreement.htm"));
        agreementContentText.append("和");
        agreementContentText.append(getOnCickSpan("《隐私政策》", "file:///android_asset/privacy_agreement.htm"));
        agreementContentText.append("了解详细信息。如您同意，请点击“同意”开始接受我们的服务。");
        agreementContentText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 可点击超链接 文字
     */
    public SpannableString getOnCickSpan(String title, String href) {
        SpannableString spannable = new SpannableString(title);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                int colorInt = ContextCompat.getColor(page.context(), R.color.theme_color);
                ds.setColor(colorInt);
                /**
                 * 去掉下划线
                 */
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                if (!StringUtil.isBlank(href)) {
                    Intent intent = new Intent(page.activity(), WebViewActivity.class);
                    intent.putExtra("url", href);
                    page.startActivity(intent);
                }
                /**
                 * 方法重新设置文字背景为透明色
                 */
                agreementContentText.setHighlightColor(page.context().getResources().getColor(android.R.color.transparent));
            }
        }, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public void showMyDialog() {
        if (SPApi.app().getAgreementDialog()) {
            return;
        }
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.agreement_dialog_layout;
    }

    @OnClick({R.id.agreement_dialog_cancle, R.id.agreement_dialog_call_btn})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.agreement_dialog_cancle:
                page.activity().finish();
                break;
            case R.id.agreement_dialog_call_btn:
                SPApi.app().setAgreementDialog();
                break;
        }
        dismiss();
    }
}

