package module.main.counsel.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.share_preference.SPApi;
import ui.CustomClickListener;
import ui.FlowLayout;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_activity_back_btn)
    ImageView searchActivityBackBtn;
    @BindView(R.id.search_activity_edittext)
    EditText edittext;
    @BindView(R.id.search_activity_history_flowlayout)
    FlowLayout historyFlowlayout;
    @BindView(R.id.search_activity_hot_flowlayout)
    FlowLayout hotFlowlayout;

    private List<String> list;

    public static void newIntent(Page page) {
        page.startActivity(new Intent(page.activity(), SearchActivity.class));
    }

    @Override

    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        list = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(SPApi.app().getSearchRecord());
        setHistory();
    }

    public void setHistory() {
        historyFlowlayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final TextView tv_tag = (TextView) LayoutInflater.from(context()).inflate(R.layout.search_label_item_layout, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, ConvertUtil.dip2px(context(), 10), ConvertUtil.dip2px(context(), 10));
            tv_tag.setLayoutParams(params);
            tv_tag.setText(list.get(i));
            historyFlowlayout.addView(tv_tag);
            tv_tag.setOnClickListener(new CustomClickListener() {
                @Override
                public void onClick() {
                    gotoSearch(tv_tag.getText().toString());
                }
            });
        }
    }

    public void setHot() {
        hotFlowlayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            final TextView tv_tag = (TextView) LayoutInflater.from(context()).inflate(R.layout.search_label_item_layout, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, ConvertUtil.dip2px(context(), 10), ConvertUtil.dip2px(context(), 10));
            tv_tag.setLayoutParams(params);
            tv_tag.setText(list.get(i));
            historyFlowlayout.addView(tv_tag);
            tv_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoSearch(tv_tag.getText().toString());
                }
            });
        }
    }

    @Override
    public void initListener() {
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void loadData() {

    }

    public void search() {
        /*隐藏软键盘*/
        InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        String text = edittext.getText().toString();
        if (StringUtil.isBlank(text)) {
            showToast("请输入关键词");
            return;
        }
        imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0); //强制隐藏键盘
        //添加自己的方法
        gotoSearch(text);
    }

    public void gotoSearch(String word) {
        SearchResultActivity.newIntent(this, word);
        SPApi.app().setSearchRecord(word);
    }

    @OnClick({R.id.search_activity_back_btn, R.id.search_activity_search_btn, R.id.search_activity_clear_btn})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.search_activity_back_btn:
                finish();
                break;
            case R.id.search_activity_search_btn:
                search();
                break;
            case R.id.search_activity_clear_btn:
                SPApi.app().clearSearchRecotd();
                historyFlowlayout.removeAllViews();
                list.clear();
                break;
        }
    }
}
