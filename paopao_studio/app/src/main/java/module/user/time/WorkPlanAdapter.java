package module.user.time;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.paopao.paopaostudio.R;

import base.EasyAdapter;
import base.EasyViewHolder;
import butterknife.BindView;
import common.repository.http.entity.time.WorkPlanTimeItemBean;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class WorkPlanAdapter extends EasyAdapter<WorkPlanTimeItemBean> {
    private boolean isRest = false;

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean rest) {
        isRest = rest;
        notifyDataSetChanged();
    }

    public WorkPlanAdapter(Page page) {
        super(page);
    }

    @Override
    protected EasyViewHolder.AdapterViewHolder createViewHolder(ViewGroup parent, int position) {
        return new ItemHolder(parent);
    }

    class ItemHolder extends EasySimpleViewHolder {

        @BindView(R.id.work_plan_item_item_time_text)
        TextView timeText;
        @BindView(R.id.work_plan_item_item_status_text)
        TextView statusText;
        @BindView(R.id.work_plan_item_item_layout)
        LinearLayout itemLayout;
        @BindView(R.id.work_plan_item_item_bottom_line)
        View bottomLine;
        @BindView(R.id.work_plan_item_item_right_line)
        View rightLine;

        public ItemHolder(ViewGroup parent) {
            super(parent, R.layout.work_plan_item_layout);
        }

        @Override
        public void setData(WorkPlanTimeItemBean data) {
            super.setData(data);
            statusText.setVisibility(data.getTimeType() == WorkPlanTimeItemBean.KEY_STATUS_NOT_USE ? View.VISIBLE : View.GONE);
            bottomLine.setVisibility(View.VISIBLE);
            rightLine.setVisibility(position % 3 == 2 ? View.GONE : View.VISIBLE);
            itemLayout.setBackgroundResource(R.color.transparent);
            timeText.setText(data.getConsultantWorkStartTime() + "-" + data.getConsultantWorkEndTime());
            //画线逻辑
            if (getCount() % 3 == 0) {
                if (getCount() == position + 1 || getCount() == position + 2 || getCount() == position + 3)
                    bottomLine.setVisibility(View.GONE);
            } else if (getCount() % 3 == 2) {
                if (getCount() == position + 1 || getCount() == position + 2)
                    bottomLine.setVisibility(View.GONE);
            } else if (getCount() % 3 == 1) {
                if (getCount() == position + 1)
                    bottomLine.setVisibility(View.GONE);
            }
            //背景
            if (isRest || data.getTimeType() == WorkPlanTimeItemBean.KEY_STATUS_NOT_USE
                    || data.getTimeType() == WorkPlanTimeItemBean.KEY_STATUS_REST) {
                if (position == 0) {//第一个
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_left_top_background_share);
                } else if (position == 2) {//第一行最后一个
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_right_top_background_share);
                } else if (getCount() % 3 == 0 && position + 1 == getCount()) {
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_right_bottom_background_share);
                } else if (getCount() % 3 == 0 && position + 3 == getCount()) {
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_left_bottom_background_share);
                } else if (getCount() % 3 == 2 && getCount() == position + 2) {
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_left_bottom_background_share);
                } else if (getCount() % 3 == 1 && getCount() == position + 1) {
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_left_bottom_background_share);
                } else {
                    itemLayout.setBackgroundResource(R.drawable.work_plan_time_item_center_background_share);
                }
            }
        }

        @Override
        protected void initLisenter() {
            super.initLisenter();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRest) {
                        return;
                    }
                    if (data.getTimeType() == WorkPlanTimeItemBean.KEY_STATUS_USE) {
                        data.setTimeType(WorkPlanTimeItemBean.KEY_STATUS_REST);
                    } else if (data.getTimeType() == WorkPlanTimeItemBean.KEY_STATUS_REST) {
                        data.setTimeType(WorkPlanTimeItemBean.KEY_STATUS_USE);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    public String getSetInfo() {
        if (datas.isEmpty()) {
            return "";
        }
        return ConvertUtil.toJsonString(datas);
    }
}
