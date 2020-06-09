package module.main.counsel;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.BaseRVAdapter;
import base.BaseRvViewHolder;
import butterknife.BindView;
import common.repository.http.entity.counsel.QuestionItemBean;
import common.webview.custom.BaiTiaoWebView;
import common.webview.page.WebViewActivity;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class QuestionAdapter extends BaseRVAdapter<QuestionItemBean> {

    public QuestionAdapter(Page page) {
        super(page);
    }

    @Override
    public BaseRvViewHolder<QuestionItemBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.counsel_question_item_title_text)
        TextView titleText;
        @BindView(R.id.counsel_question_item_icon_img)
        ImageView iconImg;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.counsel_question_item_layout);
        }

        @Override
        public void setData(QuestionItemBean data) {
            super.setData(data);
            titleText.setText(data.getVal());
            GlideImageLoader.loadImageCustomCorner(page, data.getIcon(), iconImg, ConvertUtil.dip2px(page.context(), 8));
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(page.activity(), WebViewActivity.class);
                    intent.putExtra(BaiTiaoWebView.EXTRA_URL, String.valueOf(data.getLink()));
                    page.startActivity(intent);
                }
            });
        }
    }
}