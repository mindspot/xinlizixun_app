package module.main.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.paopao.paopaostudio.R;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import common.events.OrderMsgNumEvent;
import de.greenrobot.event.EventBus;
import module.main.center.PurchasePlanActivity;
import module.order.councilor.MyCouncilorListActivity;
import module.order.councilor.UserCouncilorListActivity;
import module.order.user.UserOrderActivity;
import ui.title.ToolBarTitleView;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package module.main.order
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.18 上午 10:58
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class OrderFragment extends BaseFragment {
    @BindView(R.id.order_fragment_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.order_fragment_userlist_msg_number)
    TextView userlistMsgNumber;
    @BindView(R.id.order_fragment_usermeal_msg_number)
    TextView userMealMsgNumber;
    @BindView(R.id.order_fragment_mylaunch_msg_number)
    TextView myLaunchMsgNumber;
    @BindView(R.id.order_fragment_myget_msg_number)
    TextView myGetMsgNumber;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void loadData() {
    }

    @OnClick({R.id.order_fragment_userlist_layout, R.id.order_fragment_usermeal_layout, R.id.order_fragment_mylaunch_layout, R.id.order_fragment_myget_layout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.order_fragment_userlist_layout:
                UserOrderActivity.newIntent(this);
                break;
            case R.id.order_fragment_usermeal_layout:
                PurchasePlanActivity.newIntent(this);
                break;
            case R.id.order_fragment_mylaunch_layout:
                MyCouncilorListActivity.newIntent(this);
                break;
            case R.id.order_fragment_myget_layout:
                UserCouncilorListActivity.newIntent(this);
                break;
        }
    }

    public void onEventMainThread(OrderMsgNumEvent event) {
        if(event.getBean()==null){
            userlistMsgNumber.setVisibility(View.GONE);
            myLaunchMsgNumber.setVisibility(View.GONE);
            myGetMsgNumber.setVisibility(View.GONE);
            return;
        }
        userlistMsgNumber.setVisibility(event.getBean().getUnConsultationOrderNum() == 0 ? View.GONE : View.VISIBLE);
        userlistMsgNumber.setText(event.getBean().getUnConsultationOrderNum() + "");

        myLaunchMsgNumber.setVisibility(event.getBean().getUnConsultantSupervisorOrderNum() == 0 ? View.GONE : View.VISIBLE);
        myLaunchMsgNumber.setText(event.getBean().getUnConsultantSupervisorOrderNum() + "");

        myGetMsgNumber.setVisibility(event.getBean().getUnSupervisorOrderNum() == 0 ? View.GONE : View.VISIBLE);
        myGetMsgNumber.setText(event.getBean().getUnSupervisorOrderNum() + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
