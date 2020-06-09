package base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.framework.page.Page;

import java.util.ArrayList;
import java.util.List;

import common.router.CommandRequest;
import util.TimeUtils;

/**
 * 替换掉MyBaseAdapter与BaseEasyAdapter两个adapter
 *
 * @author Administrator
 * @date 2018/1/15
 */
public abstract class EasyAdapter<T> extends BaseAdapter {
    protected final Page page;
    protected final LayoutInflater inflater;
    protected final List<T> datas;

    public EasyAdapter(Page page) {
        this(page, new ArrayList<T>());
    }

    public EasyAdapter(Page page, @NonNull List<T> datas) {
        this.page = page;
        inflater = LayoutInflater.from(page.context());
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //****************** view holder ******************

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EasyViewHolder.AdapterViewHolder<T> viewHolder;
        T data = datas.get(position);
        if (convertView == null) {
            viewHolder = createViewHolder(parent, position);
            convertView = viewHolder.getRoot();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EasyViewHolder.AdapterViewHolder<T>) convertView.getTag();
        }

        viewHolder.setPosition(position);
        viewHolder.setData(data);
        return convertView;
    }

    /**
     * 获取ViewHolder
     *
     * @param parent 父布局
     * @return ViewHolder
     */
    protected abstract EasyViewHolder.AdapterViewHolder<T> createViewHolder(ViewGroup parent, int position);

    /**
     * 进一步封装减少通用代码
     */
    public abstract class EasySimpleViewHolder extends EasyViewHolder.AdapterViewHolder<T> {
        public EasySimpleViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(EasyAdapter.this, parent, resId);
        }
    }

    //****************** data ******************

    public void addData(T newData) {
        datas.add(newData);
        notifyDataSetChanged();
    }

    public void append(List<T> newDatas) {
        if (newDatas != null && !newDatas.isEmpty()) {
            datas.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

    public void refresh(List<T> newDatas) {
        datas.clear();
        append(newDatas);
    }

    public boolean remove(T newData) {
        boolean success = datas.remove(newData);
        if (success) {
            notifyDataSetChanged();
        }
        return success;
    }

    public boolean removeAll(List<T> newDatas) {
        boolean success = datas.removeAll(newDatas);
        if (success) {
            notifyDataSetChanged();
        }
        return success;
    }

    public void remove(int position) {
        datas.remove(position);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    public void clear() {
        if (datas.isEmpty()) {
            return;
        }
        datas.clear();
        notifyDataSetChanged();
    }


    //****************** item click ******************

    protected OnItemSelectEvent<T> selectEvent;

    public void setOnItemSelectEvent(OnItemSelectEvent<T> event) {
        this.selectEvent = event;
    }

    public OnItemSelectEvent<T> getSelectEvent() {
        return selectEvent;
    }

    public interface OnItemSelectEvent<T> {
        void selected(T bean, int pos);
    }


    //****************** empty view ******************

    private View emptyView;

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void showEmptyIfNeed(AdapterView adapterView) {
        if (!datas.isEmpty()) {
            return;
        }
        if (adapterView.getEmptyView() == null) {
            adapterView.setEmptyView(emptyView);
        }
    }

    public void toJumpStr(String jumpString) {
        new CommandRequest(jumpString).setPage(page).router();
    }

    public int getColor(int id) {
        return page.activity().getResources().getColor(id);
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
