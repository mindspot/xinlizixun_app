package common.repository.http.entity.artivles;

import java.util.List;

import common.repository.http.entity.base.TotalBean;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class PageInfoArticleBean extends TotalBean {
    private List<ArtivlesArtivlesItemBean> list;

    public List<ArtivlesArtivlesItemBean> getList() {
        return list;
    }

    public void setList(List<ArtivlesArtivlesItemBean> list) {
        this.list = list;
    }
}
