package common.repository.http.entity.app;

import java.util.List;

/**
 * Created by hpzhan on 2020/2/23.
 */

public class LabelItemBean {
    private List<String> list;
    private String title;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
