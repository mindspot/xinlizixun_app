package common.repository.http.entity.artivles;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class ArtivlesResponseBean extends BaseResponseBean {
    private PageInfoArticleBean pageInfoArticle;

    public PageInfoArticleBean getPageInfoArticle() {
        return pageInfoArticle;
    }

    public void setPageInfoArticle(PageInfoArticleBean pageInfoArticle) {
        this.pageInfoArticle = pageInfoArticle;
    }
}
