package common.repository.http.entity.home;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class HomeResponseBean extends BaseResponseBean {
    private List<BannerItemBean> indexImages;
    private List<MenuItemBean> classification;
    private List<TestItemBean> testQuestions;
    private List<ArtivlesItemBean> articles;
    private String testMoreUrl;

    public List<BannerItemBean> getIndexImages() {
        return indexImages;
    }

    public void setIndexImages(List<BannerItemBean> indexImages) {
        this.indexImages = indexImages;
    }

    public List<MenuItemBean> getClassification() {
        return classification;
    }

    public void setClassification(List<MenuItemBean> classification) {
        this.classification = classification;
    }

    public List<TestItemBean> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestItemBean> testQuestions) {
        this.testQuestions = testQuestions;
    }

    public List<ArtivlesItemBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArtivlesItemBean> articles) {
        this.articles = articles;
    }

    public String getTestMoreUrl() {
        return testMoreUrl;
    }

    public void setTestMoreUrl(String testMoreUrl) {
        this.testMoreUrl = testMoreUrl;
    }
}
