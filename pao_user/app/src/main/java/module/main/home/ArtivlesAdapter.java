package module.main.home;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.home.ArtivlesItemBean;
import common.webview.page.WebViewActivity;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class ArtivlesAdapter extends EasyAdapter<ArtivlesItemBean> {
    public ArtivlesAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {


        @BindView(R.id.home_artivles_item_title)
        TextView title;
        @BindView(R.id.home_artivles_item_content)
        TextView content;
        @BindView(R.id.home_artivles_item_image)
        ImageView image;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.home_artivles_item_layout);
        }

        @Override
        public void setData(ArtivlesItemBean data) {
            super.setData(data);
            GlideImageLoader.loadImageCustomCorner(page, data.getArticleImg(), image, 0);
            title.setText(data.getArticleVal());
            content.setText(data.getArticleTitle());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    WebViewActivity.newIntent(page, data.getArticleLink(), data.getArticleVal(), data.getArticleTitle(),true);
                }
            });
        }
    }
}
