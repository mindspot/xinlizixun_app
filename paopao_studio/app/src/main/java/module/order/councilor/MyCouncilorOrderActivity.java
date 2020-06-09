package module.order.councilor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.framework.page.Page;
import com.paopao.paopaostudio.R;

import base.BaseActivity;
import butterknife.BindView;
import module.app.MyApplication;
import ui.title.ToolBarTitleView;

public class MyCouncilorOrderActivity extends BaseActivity {

    @BindView(R.id.my_councilor_order_toolbar)
    ToolBarTitleView toolbar;
    @BindView(R.id.my_councilor_order_listView)
    ListView listView;

    private CouncilorListAdapter mAdapter;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), MyCouncilorOrderActivity.class));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_councilor_order;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (MyApplication.app.getCouncilorItemList() == null || MyApplication.app.getCouncilorItemList().isEmpty()) {
            finish();
            return;
        }
        mAdapter = new CouncilorListAdapter(this, 2);
        mAdapter.setChat(false);
        listView.setAdapter(mAdapter);
        mAdapter.refresh(MyApplication.app.getCouncilorItemList());
    }

    @Override
    public void initListener() {
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadData() {

    }

}
