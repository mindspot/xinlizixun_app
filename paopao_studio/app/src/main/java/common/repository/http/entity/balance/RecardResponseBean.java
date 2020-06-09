package common.repository.http.entity.balance;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package common.repository.http.entity.balance
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.27 下午 5:27
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class RecardResponseBean extends BaseResponseBean {
    private int total;
    private List<RecardItemBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecardItemBean> getList() {
        return list;
    }

    public void setList(List<RecardItemBean> list) {
        this.list = list;
    }
}
