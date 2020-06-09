package module.main.counsel;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.framework.http.bean.HttpError;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.counsel.CityItemBean;
import common.repository.http.entity.counsel.CounselHeaderResponseBean;
import common.repository.http.entity.counsel.CounselListResponseBean;
import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.counsel.CounselListRequestBean;
import module.main.common.ExpertAdapter;
import module.main.counsel.search.SearchActivity;

public class CounselFragment extends BaseFragment {

    @BindView(R.id.fragment_counsel_recycler)
    RecyclerView recycler;
    @BindView(R.id.fragment_counsel_listView)
    ListView listView;
    @BindView(R.id.fragment_counsel_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fragment_counsel_question_layout)
    LinearLayout questionLayout;
    @BindView(R.id.fragment_counsel_sort_title_text)
    TextView sortTitleText;
    @BindView(R.id.fragment_counsel_sort_arrows_icon)
    ImageView sortArrowsIcon;
    @BindView(R.id.fragment_counsel_city_title_text)
    TextView cityTitleText;
    @BindView(R.id.fragment_counsel_city_arrows_icon)
    ImageView cityArrowsIcon;
    @BindView(R.id.fragment_counsel_time_title_text)
    TextView timeTitleText;
    @BindView(R.id.fragment_counsel_time_arrrows_icon)
    ImageView timeArrrowsIcon;
    @BindView(R.id.fragment_counsel_filtrate_title_text)
    TextView filtrateTitleText;
    @BindView(R.id.fragment_counsel_filtrate_icon)
    ImageView filtrateIcon;
    @BindView(R.id.fragment_counsel_line)
    View lineView;
    @BindView(R.id.empty_image)
    ImageView empty;

    private CounselHeaderResponseBean mHeaderBean;

    private boolean isHeaderLoadSuccess = false;

    private int page = 1;

    private QuestionAdapter questionAdapter;

    private SelectSortPopWindow sortPopWindow;
    private List<Integer> mSortIds;

    private SelectCityPopWindow cityPopWindow;
    private CityItemBean mProvince, mCity;

    private SelectTimePopWindow timePopWindow;
    private List<Integer> mTimeIds;

    private SelectFiltratePopWindow filtratePopWindow;
    private int mMin;
    private int mMax;
    private List<Integer> mSexIds;
    private List<Integer> mAgeIds;

    private ExpertAdapter expertAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_counsel;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        refreshLayout.setEnableLoadMore(false);
        sortPopWindow = new SelectSortPopWindow(this);
        cityPopWindow = new SelectCityPopWindow(this);
        timePopWindow = new SelectTimePopWindow(this);
        filtratePopWindow = new SelectFiltratePopWindow(this);

        expertAdapter = new ExpertAdapter(this);
        listView.setAdapter(expertAdapter);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page++;
                getData();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (!isHeaderLoadSuccess) {
                    getHeaderBean();
                    return;
                }
                sortPopWindow.reset(false);
                filtratePopWindow.reset(false);
                page = 1;
                getData();
            }
        });
        sortPopWindow.setOnSelectSortListener(new SelectSortPopWindow.OnSelectSortListener() {
            @Override
            public void onSelect(List<Integer> ids, boolean isLoad) {
                mSortIds = ids;
                if (!isLoad) {
                    return;
                }
                page = 1;
                getData();
            }

            @Override
            public void onDismiss() {
                sortTitleText.setTextColor(getResources().getColor(R.color.color_666666));
                sortArrowsIcon.setImageResource(R.mipmap.counsel_down_arrows_icon);
            }
        });
        cityPopWindow.setOnSelectCityListener(new SelectCityPopWindow.OnSelectCityListener() {
            @Override
            public void onSelect(CityItemBean province, CityItemBean city) {
                if (province != null) {
                    mProvince = new CityItemBean();
                    mProvince.setAreaCode(province.getAreaCode());
                    mProvince.setAreaName(province.getAreaName());
                    mProvince.setId(province.getId());
                    mProvince.setParentId(province.getParentId());
                } else {
                    mProvince = null;
                }
                if (city != null) {
                    mCity = new CityItemBean();
                    mCity.setAreaCode(city.getAreaCode());
                    mCity.setAreaName(city.getAreaName());
                    mCity.setId(city.getId());
                    mCity.setParentId(city.getParentId());
                } else {
                    mCity = null;
                }
                page = 1;
                getData();
            }

            @Override
            public void onDismiss() {
                cityTitleText.setTextColor(getResources().getColor(R.color.color_666666));
                cityArrowsIcon.setImageResource(R.mipmap.counsel_down_arrows_icon);
            }
        });
        timePopWindow.setOnSelectTimeListener(new SelectTimePopWindow.OnSelectTimeListener() {
            @Override
            public void onSelect(List<Integer> ids) {
                mTimeIds = ids;
                page = 1;
                getData();
            }

            @Override
            public void onDismiss() {
                timeTitleText.setTextColor(getResources().getColor(R.color.color_666666));
                timeArrrowsIcon.setImageResource(R.mipmap.counsel_down_arrows_icon);
            }
        });
        filtratePopWindow.setOnSelectFiltrateListener(new SelectFiltratePopWindow.OnSelectFiltrateListener() {
            @Override
            public void onSelect(int min, int max, List<Integer> sexIds, List<Integer> ageIds, boolean isLoad) {
                mMin = min;
                mMax = max;
                mSexIds = sexIds;
                mAgeIds = ageIds;
                if (!isLoad) {
                    return;
                }
                page = 1;
                getData();
            }

            @Override
            public void onDismiss() {
                filtrateTitleText.setTextColor(getResources().getColor(R.color.color_666666));
                filtrateIcon.setImageResource(R.mipmap.counsel_filtrate_icon);
            }
        });
    }

    public void getHeaderBean() {
        HttpApi.app().getCounselHeaderInfo(this, new BaseRequestBean(), new HttpCallback<CounselHeaderResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CounselHeaderResponseBean data) {
                isHeaderLoadSuccess = true;
                mHeaderBean = data;
                initQuestionRecycler();
                sortPopWindow.setData(mHeaderBean.getClassification());
                timePopWindow.setData(mHeaderBean.getConsultationTime());
                filtratePopWindow.setData(mHeaderBean.getComprehensive());
                mMin = mHeaderBean.getComprehensive().getMinPrice();
                mMax = mHeaderBean.getComprehensive().getMaxPrice();
                if (mMax == 0) {
                    mMax = 1000;
                }
                refreshLayout.autoRefresh();
            }

            @Override
            public void onFailed(HttpError error) {
                isHeaderLoadSuccess = false;
                showToast(error.getErrMessage());
            }
        });
    }

    public void getData() {
        CounselListRequestBean bean = new CounselListRequestBean();
        if (mProvince != null) {
            bean.setProvCode(mProvince.getAreaCode());
            bean.setProvName(mProvince.getAreaName());
        }
        if (mCity != null) {
            bean.setCityCode(mCity.getAreaCode());
            bean.setCityName(mCity.getAreaName());
        }
        if (mSortIds != null && !mSortIds.isEmpty())
            bean.setClassification(mSortIds);
        if (mAgeIds != null && !mAgeIds.isEmpty())
            bean.setConsultationAge(mAgeIds);
        if (mTimeIds != null && !mTimeIds.isEmpty())
            bean.setConsultationTime(mTimeIds);
//        bean.setMaxPrice(mMax * 100);
//        bean.setMinPrice(mMin * 100);
        if (mSexIds != null && !mSexIds.isEmpty())
            bean.setSex(mSexIds);
        bean.setPageNum(page);
        HttpApi.app().getCounselListInfo(this, bean, new HttpCallback<CounselListResponseBean>() {
            @Override
            public void onSuccess(int code, String message, CounselListResponseBean data) {
                empty.setVisibility(View.GONE);
                if (page == 1) {
                    refreshLayout.finishRefresh();
                    if (data.getList().isEmpty()) {
                        expertAdapter.clear();
                        refreshLayout.setEnableLoadMore(false);
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    expertAdapter.refresh(data.getList());
                } else {
                    refreshLayout.finishLoadMore();
                    expertAdapter.append(data.getList());
                }
                refreshLayout.setEnableLoadMore(data.getRecordTotal() != expertAdapter.getCount());
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


    public void initQuestionRecycler() {
        if (mHeaderBean.getConsultationQuestion().isEmpty()) {
            return;
        }
        questionLayout.setVisibility(View.GONE);
        if (questionAdapter == null) {
            questionAdapter = new QuestionAdapter(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(questionAdapter);
        }
        questionAdapter.refresh(mHeaderBean.getConsultationQuestion());
    }

    @OnClick({R.id.fragment_counsel_search_layout, R.id.fragment_counsel_sort_layout, R.id.fragment_counsel_city_layout, R.id.fragment_counsel_time_layout, R.id.fragment_counsel_filtrate_layout})
    public void onViewClicked(View view) {
        if (isDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_counsel_search_layout:
                SearchActivity.newIntent(this);
                break;
            case R.id.fragment_counsel_sort_layout:
                sortTitleText.setTextColor(getResources().getColor(R.color.theme_color));
                sortArrowsIcon.setImageResource(R.mipmap.counsel_up_arrows_icon);
                sortPopWindow.showPopUpWindow(lineView);
                break;
            case R.id.fragment_counsel_city_layout:
                cityTitleText.setTextColor(getResources().getColor(R.color.theme_color));
                cityArrowsIcon.setImageResource(R.mipmap.counsel_up_arrows_icon);
                cityPopWindow.showPopUpWindow(lineView);
                break;
            case R.id.fragment_counsel_time_layout:
                timeTitleText.setTextColor(getResources().getColor(R.color.theme_color));
                timeArrrowsIcon.setImageResource(R.mipmap.counsel_up_arrows_icon);
                timePopWindow.showPopUpWindow(lineView);
                break;
            case R.id.fragment_counsel_filtrate_layout:
                filtratePopWindow.showPopUpWindow(lineView);
                break;
        }
    }

    @Override
    public void onShow() {
        super.onShow();
        if (!isHeaderLoadSuccess) {
            getHeaderBean();
        }
    }
}
