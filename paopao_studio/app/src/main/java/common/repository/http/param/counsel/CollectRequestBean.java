package common.repository.http.param.counsel;

import common.repository.http.param.BaseRequestBean;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class CollectRequestBean extends BaseRequestBean {
    private int collectionId;

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
}
