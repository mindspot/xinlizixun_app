package module.main.counsel;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.BaseRVAdapter;
import base.BaseRvViewHolder;
import butterknife.BindView;
import common.repository.http.entity.counsel.QuestionItemBean;
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
            if (position % 3 == 0) {
                iconImg.setImageResource(R.mipmap.ceramic_chip_1);
            } else if (position % 3 == 1) {
                iconImg.setImageResource(R.mipmap.ceramic_chip_2);
            } else {
                iconImg.setImageResource(R.mipmap.ceramic_chip_3);
            }
            titleText.setText(data.getVal());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    WebViewActivity.newIntent(page, String.valueOf(data.getLink()), data.getVal(), "",true);
                }
            });
        }
    }
}