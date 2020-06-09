package base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.page.Page;

import butterknife.ButterKnife;
import common.router.CommandRequest;
import util.TimeUtils;

/**
 * @author Administrator
 * @date 2018/11/8
 *
 * Description
 */

public class BaseRvViewHolder<T> extends RecyclerView.ViewHolder {
    protected T data;
    protected int position;
    protected View root;
    protected Page pageContext;

    public BaseRvViewHolder(Page page, View view) {
        super(view);
        ButterKnife.bind(this, itemView);
        root = itemView;
        this.pageContext = page;
        initView();
        initLisenter();
    }

    public View inflate(ViewGroup parent, @LayoutRes int resId) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    public void setData(T data) {
        position = getLayoutPosition();
        this.data = data;
    }

    protected void initView() {
    }

    protected void initLisenter() {
    }

    protected static View inflateRv(Page page, ViewGroup parent, @LayoutRes int resId) {
        return LayoutInflater.from(page.context()).inflate(resId, parent, false);
    }

    public void toJumpStr(String jumpString) {
        new CommandRequest(jumpString).setPage(pageContext).router();
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
