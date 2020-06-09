package module.main.home;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.BaseRVAdapter;
import base.BaseRvViewHolder;
import butterknife.BindView;
import common.repository.http.entity.home.TestItemBean;
import common.webview.custom.BaiTiaoWebView;
import common.webview.page.WebViewActivity;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2019/4/10.
 */

public class TestListAdapter extends BaseRVAdapter<TestItemBean> {

    public TestListAdapter(Page page) {
        super(page);
    }

    @Override
    public BaseRvViewHolder<TestItemBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CsItemHolder(parent);
    }

    class CsItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.home_test_item_image)
        ImageView image;

        public CsItemHolder(ViewGroup parent) {
            super(parent, R.layout.home_test_item_layout);
        }

        @Override
        public void setData(TestItemBean data) {
            super.setData(data);
            GlideImageLoader.loadImageCustomCorner(page, data.getVal(), image, 15);
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