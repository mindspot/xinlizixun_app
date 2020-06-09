package module.main.counsel.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import base.BaseRVAdapter;
import base.BaseRvViewHolder;
import butterknife.BindView;
import common.repository.bean.IntroduceItemBean;
import ui.FlowLayout;
import ui.MyListView;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class IntroduceAdapter extends BaseRVAdapter<IntroduceItemBean> {

    public IntroduceAdapter(Page page) {
        super(page);
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public BaseRvViewHolder<IntroduceItemBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ItemHolder(parent);
            case 2:
                return new ItemHolder2(parent);
            case 3:
                return new ItemHolder3(parent);
            default:
                return new ItemHolder(parent);
        }
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.counsel_detail_item_title_text)
        TextView titleText;
        @BindView(R.id.counsel_detail_item_content_text)
        TextView contentText;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.counsel_detail_item_layout);
        }

        @Override
        public void setData(IntroduceItemBean data) {
            super.setData(data);
            titleText.setText(data.getTitle());
            contentText.setText(data.getContent());
        }
    }

    class ItemHolder2 extends EasySimpleViewHolder {

        @BindView(R.id.counsel_detail_item_title_text)
        TextView titleText;
        @BindView(R.id.listview)
        MyListView listview;

        private CounselDetailImageAdapter mAdapter;

        public ItemHolder2(ViewGroup parent) {
            super(parent, R.layout.counsel_detail_item2_layout);
            mAdapter = new CounselDetailImageAdapter(page);
            listview.setAdapter(mAdapter);
        }

        @Override
        public void setData(IntroduceItemBean data) {
            super.setData(data);
            titleText.setText(data.getTitle());
            mAdapter.refresh(data.getQualifications());
        }
    }

    class ItemHolder3 extends EasySimpleViewHolder {

        @BindView(R.id.counsel_detail_item_title_text)
        TextView titleText;
        @BindView(R.id.counsel_detail_item_content_text)
        FlowLayout contentText;

        public ItemHolder3(ViewGroup parent) {
            super(parent, R.layout.counsel_detail_item3_layout);
        }

        @Override
        public void setData(IntroduceItemBean data) {
            super.setData(data);
            titleText.setText(data.getTitle());
            contentText.removeAllViews();
            for (String label : data.getLabels()) {
                final TextView tv_tag = (TextView) LayoutInflater.from(page.context()).inflate(R.layout.lable_item_layout, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, ConvertUtil.dip2px(page.context(), 10), ConvertUtil.dip2px(page.context(), 10));
                tv_tag.setLayoutParams(params);
                tv_tag.setText(label);
                contentText.addView(tv_tag);
            }
        }
    }
}