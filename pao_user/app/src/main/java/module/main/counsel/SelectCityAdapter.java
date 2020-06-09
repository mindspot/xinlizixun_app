package module.main.counsel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.counsel.CityItemBean;
import ui.CustomClickListener;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class SelectCityAdapter extends EasyAdapter<CityItemBean> {
    public SelectCityAdapter(Page page) {
        super(page);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private int selectId;

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.select_city_pop_item_view)
        View view;
        @BindView(R.id.select_city_pop_item_cityName)
        TextView cityName;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.select_city_pop_item_layout);
        }

        @Override
        public void setData(CityItemBean data) {
            super.setData(data);
            cityName.setText(data.getAreaName());
            cityName.setTextColor(position == selectId
                    ? getColor(R.color.theme_color) : getColor(R.color.color_999999));
            view.setVisibility(position == selectId ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    selectId = position;
                    notifyDataSetChanged();
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(data);
                    }
                }
            });
        }
    }

    interface OnItemClickListener {
        void onClick(CityItemBean item);
    }
}