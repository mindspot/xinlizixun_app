package module.main.counsel.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.framework.page.Page;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.CounselListResponseBean;
import common.repository.http.param.counsel.SearchCounselListRequestBean;
import common.repository.share_preference.SPApi;
import module.main.common.ExpertAdapter;

public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.search_result_activity_edittext)
    EditText edittext;
    @BindView(R.id.search_result_activity_listview)
    ListView listview;
    @BindView(R.id.search_result_activity_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_image)
    ImageView empty;

    private ExpertAdapter mAdapter;

    private int page = 1;

    public static void newIntent(Page page, String word) {
        Intent intent = new Intent(page.activity(), SearchResultActivity.class);
        intent.putExtra("word", word);
        page.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            String word = getIntent().getStringExtra("word");
            edittext.setText(word);
            gotoSearch(word);
        }
        refreshLayout.setEnableLoadMore(false);
        mAdapter = new ExpertAdapter(this);
        listview.setAdapter(mAdapter);
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
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                getData();
            }
        });
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

    @Override
    public void loadData() {

    }

    public void gotoSearch(String word) {
        SPApi.app().setSearchRecord(word);
        refreshLayout.autoRefresh();
    }

    public void getData() {
        SearchCounselListRequestBean bean = new SearchCounselListRequestBean();
        bean.setPageNum(page);
        bean.setWd(edittext.getText().toString());
        HttpApi.app().searchCounselListInfo(this, bean, new HttpCallback<CounselListResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CounselListResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refreshLayout.finishRefresh();
                    if (data.getList().isEmpty()) {
                        mAdapter.clear();
                        refreshLayout.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    mAdapter.refresh(data.getList());
                } else {
                    refreshLayout.finishLoadMore();
                    mAdapter.append(data.getList());
                }
                refreshLayout.setEnableLoadMore(data.getRecordTotal() != mAdapter.getCount());
            }

            @Override
            public void onFailed(HttpError error) {
                if (page == 1) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
                page--;
                if (page < 1) {
                    page = 1;
                }
                showToast(error.getErrMessage());
            }
        });
    }

    @OnClick({R.id.search_result_activity_back_btn, R.id.search_result_activity_btn})
    public void onViewClicked(View view) {
        if(isDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.search_result_activity_back_btn:
                finish();
                break;
            case R.id.search_result_activity_btn:
                search();
                break;
        }
    }
}
