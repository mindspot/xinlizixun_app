package base;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framework.page.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import common.router.CommandRequest;
import util.TimeUtils;

/**
 * @author Administrator
 * @date 2018/10/10
 *
 * Description
 * <p>
 * RecyclerView adapter 简易封装 用来快速替换 EasyAdapter
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRvViewHolder<T>> {
    protected final Page page;
    protected final LayoutInflater inflater;
    protected final List<T> datas;

    public BaseRVAdapter(Page page) {
        this(page, new ArrayList<T>());
    }

    public BaseRVAdapter(Page page, @NonNull List<T> datas) {
        this.page = page;
        inflater = LayoutInflater.from(page.context());
        this.datas = datas;
    }
//    该方法需要子类实现 替换 EasyAdapter 中的 createViewHolder(ViewGroup parent, int position)
//    @Override
//    public EasySimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }


    @Override
    public void onBindViewHolder(BaseRvViewHolder<T> holder, int position) {
        holder.setData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void toJumpStr(String jumpString) {
        new CommandRequest(jumpString).setPage(page).router();
    }

    public abstract class EasySimpleViewHolder extends BaseRvViewHolder<T> {
        public EasySimpleViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(page, inflater.inflate(resId, parent, false));
        }
    }

    //****************** data ******************

    /**
     * change data
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * change data
     */
    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        datas.set(index, data);
        notifyItemChanged(index);
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        datas.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * add one new data
     */
    public void addData(@NonNull T data) {
        datas.add(data);
        notifyItemInserted(datas.size());
    }

    /**
     * add new data to the end of mData
     *
     * @param newData the new data collection
     */
    public void append(@NonNull Collection<? extends T> newData) {
        datas.addAll(newData);
        notifyItemRangeInserted(datas.size() - newData.size(), newData.size());
    }

    public void refresh(List<T> newDatas) {
        datas.clear();
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

    public void remove(T newData) {
        int position = datas.indexOf(newData);
        remove(position);
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(@IntRange(from = 0) int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        if (datas.isEmpty()) {
            return;
        }
        datas.clear();
        notifyDataSetChanged();
    }


    /**
     * 两个item，位置对调
     */
    public void swap(int position1, int position2) {
        Collections.swap(datas, position1, position2);
        notifyItemChanged(position1);
        notifyItemChanged(position2);
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
