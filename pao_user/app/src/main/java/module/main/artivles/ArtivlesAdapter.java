package module.main.artivles;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaouser.R;

import base.BaseRVAdapter;
import base.BaseRvViewHolder;
import butterknife.BindView;
import common.repository.http.entity.artivles.ArtivlesArtivlesItemBean;
import common.webview.page.WebViewActivity;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class ArtivlesAdapter extends BaseRVAdapter<ArtivlesArtivlesItemBean> {
    public ArtivlesAdapter(Page page) {
        super(page);
    }

    @NonNull
    @Override
    public BaseRvViewHolder<ArtivlesArtivlesItemBean> onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.artivles_item_title)
        TextView title;
        @BindView(R.id.artivles_item_author)
        TextView author;
        @BindView(R.id.artivles_item_num_text)
        TextView numText;
        @BindView(R.id.artivles_item_image)
        ImageView image;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.artivles_item_layout);
        }

        @Override
        public void setData(ArtivlesArtivlesItemBean data) {
            super.setData(data);
            GlideImageLoader.loadImageCustomCorner(page, data.getArticleImg(), image, 0);
            title.setText(data.getArticleVal());
            author.setText(data.getAuthor());
            numText.setText(data.getBrowseVolume());
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                protected void onClick() {
                    WebViewActivity.newIntent(page, data.getArticleLink(), data.getArticleVal(), data.getArticleTitle(),true);
                }
            });
        }
    }
}

