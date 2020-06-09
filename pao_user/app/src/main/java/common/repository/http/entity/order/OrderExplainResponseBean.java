package common.repository.http.entity.order;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class OrderExplainResponseBean extends BaseResponseBean {
    private int account;
    private DetailedDescriptionBean detailedDescription;

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public DetailedDescriptionBean getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(DetailedDescriptionBean detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public static class DetailedDescriptionBean {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
