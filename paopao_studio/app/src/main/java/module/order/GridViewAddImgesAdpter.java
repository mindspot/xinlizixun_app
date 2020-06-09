package module.order;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.framework.page.Page;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.bean.ImageItemBean;

/**
 * com.bm.falvzixun.adapter.GridViewAddImgAdpter
 *
 * @author yuandl on 2015/12/24.
 *         添加上传图片适配器
 */
public class GridViewAddImgesAdpter extends EasyAdapter<ImageItemBean> {

    public GridViewAddImgesAdpter(Page page) {
        super(page);
        addNormal();
    }

    public void addNormal() {
        datas.add(new ImageItemBean(1));
    }

    private OnItemImageListener onItemImageListener;

    public void setOnItemImageListener(OnItemImageListener onItemImageListener) {
        this.onItemImageListener = onItemImageListener;
    }

    public void addData(ImageItemBean file) {
        datas.add(datas.size() - 1, file);
        if (datas.size() > 3) {
            datas.remove(datas.size() - 1);
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (datas.size() >= 3 && datas.get(2).getType() != 1) {
            addNormal();
        }
        datas.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        if (datas.isEmpty()) {
            return;
        }
        datas.clear();
        addNormal();
        notifyDataSetChanged();
    }

    @Override
    public ImageItemBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder<ImageItemBean> createViewHolder(ViewGroup parent, int position) {
        switch (getItemViewType(position)) {
            case 0:
                return new ImageHolder(parent);
            case 1:
                return new AddViewHolder(parent);
            case 2:
                return new ImageTwoHolder(parent);
            default:
                return new AddViewHolder(parent);
        }
    }

    class ImageHolder extends EasyViewHolder.AdapterViewHolder<ImageItemBean> {

        @BindView(R.id.iv_image)
        ImageView ivImage;

        private ImageHolder(ViewGroup parent) {
            super(GridViewAddImgesAdpter.this, parent, R.layout.item_published_grid_layout);
        }

        @Override
        public void setData(ImageItemBean data) {
            super.setData(data);
            Glide.with(page.context())
                    .load(data.getFile())
                    .apply(new RequestOptions().priority(Priority.HIGH))
                    .into(ivImage);
        }

        @OnClick({R.id.iv_image, R.id.bt_del})
        public void onViewClicked(View view) {
            if (onItemImageListener == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.iv_image:
                    onItemImageListener.onShowBigImg(data);
                    break;
                case R.id.bt_del:
                    removeData(position);
                    onItemImageListener.onDelete(position);
                    break;
            }
        }
    }


    class ImageTwoHolder extends EasyViewHolder.AdapterViewHolder<ImageItemBean> {

        @BindView(R.id.iv_image)
        ImageView ivImage;

        private ImageTwoHolder(ViewGroup parent) {
            super(GridViewAddImgesAdpter.this, parent, R.layout.item_published_grid_layout);
        }

        @Override
        public void setData(ImageItemBean data) {
            super.setData(data);
            Glide.with(page.context())
                    .load(data.getPath())
                    .apply(new RequestOptions().priority(Priority.HIGH))
                    .into(ivImage);
            GlideImageLoader.preloadImage(page, data.getPath().replace(data.getImgSize(), ""));
        }

        @OnClick({R.id.iv_image, R.id.bt_del})
        public void onViewClicked(View view) {
            if (isDoubleClick()) {
                return;
            }
            if (onItemImageListener == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.iv_image:
                    onItemImageListener.onShowBigImg(data);
                    break;
                case R.id.bt_del:
                    removeData(position);
                    onItemImageListener.onDelete(position);
                    break;
            }
        }
    }

    class AddViewHolder extends EasyViewHolder.AdapterViewHolder<ImageItemBean> {

        private AddViewHolder(ViewGroup parent) {
            super(GridViewAddImgesAdpter.this, parent, R.layout.item_published_add_grid_layout);
        }

        @Override
        public void setData(ImageItemBean data) {
            super.setData(data);

        }

        @OnClick(R.id.iv_image)
        public void onViewClicked() {
            if (onItemImageListener != null) {
                onItemImageListener.onAddImg();
            }
        }
    }

    public interface OnItemImageListener {
        void onDelete(int position);

        void onShowBigImg(ImageItemBean file);

        void onAddImg();
    }

}
