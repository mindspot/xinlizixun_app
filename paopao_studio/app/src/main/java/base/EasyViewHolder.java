package base;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.framework.page.Page;

import butterknife.ButterKnife;
import util.TimeUtils;

/**
 * 替换掉BaseViewHolder
 *
 * @author Administrator
 */
public abstract class EasyViewHolder<Root extends View, Data> {

    protected final Page page;
    protected Root root;
    protected Data data;

    /**
     * 有些需要延迟inflater root view 的情况
     */
    public EasyViewHolder(Page page) {
        this.page = page;
    }

    public EasyViewHolder(Page page, Root root) {
        ButterKnife.bind(this, root);
        this.page = page;
        init(root);
    }

    protected void init(Root root) {
        this.root = root;
        ButterKnife.bind(this, root);
        initView();
        initLisenter();
    }

    protected void initView() {
    }

    protected <T> T $(@IdRes int id) {
        return (T) root.findViewById(id);
    }

    protected void initLisenter() {
    }


    public void setData(Data data) {
        this.data = data;
    }

    public Root getRoot() {
        return root;
    }

    public void setVisibility(int visibility) {
        root.setVisibility(visibility);
    }


    /**
     * 给adapter使用
     *
     * @param <T> 数据类型
     */
    public static class AdapterViewHolder<T> extends EasyViewHolder<View, T> {
        private EasyAdapter<T> adapter;
        protected int position;

        public AdapterViewHolder(final EasyAdapter<T> adapter, ViewGroup parent, @LayoutRes int itemLayoutId) {
            super(adapter.page, adapter.inflater.inflate(itemLayoutId, parent, false));
            this.adapter = adapter;

            if (adapter.getSelectEvent() != null) {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.getSelectEvent().selected(data, position);
                    }
                });
            }
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();

        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private long lastClickTime;

    public boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime > TimeUtils.DOUBLE_CLICK_TIME) {
            lastClickTime = time;
            return false;
        }
        return true;
    }
}