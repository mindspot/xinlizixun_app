package module.app;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.framework.component.BaseApplication;
import com.framework.http.bean.HttpError;
import com.framework.page.Page;

import java.util.ArrayList;
import java.util.List;

import common.repository.bean.OrderTimeBean;
import common.repository.http.HttpApi;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.ServicePhoneResponseBean;
import common.repository.http.entity.app.ServiceUrlResponseBean;
import common.repository.http.entity.balance.CardItemBean;
import common.repository.http.entity.counsel.detail.PersonalInforBean;
import common.repository.http.entity.counsel.detail.WorkInfoBean;
import common.repository.http.entity.home.MenuItemBean;
import util.location.DLocationUtils;
import util.location.DLocationWhat;
import util.location.OnLocationChangeListener;


public class DataApplication extends BaseApplication {
    private static final String TAG = DataApplication.class.getSimpleName();
    //分类
    private List<MenuItemBean> mClassify;

    public List<MenuItemBean> getmClassify() {
        return mClassify;
    }

    public void setmClassify(List<MenuItemBean> classify) {
        if (mClassify == null) {
            mClassify = new ArrayList<MenuItemBean>();
        }
        mClassify.clear();
        MenuItemBean menuItemBean = new MenuItemBean();
        menuItemBean.setId(0);
        menuItemBean.setVal("推荐");
        mClassify.add(menuItemBean);
        if (classify != null) {
            mClassify.addAll(classify);
        }
    }

    //咨询师信息
    private PersonalInforBean personalInforBean;

    public PersonalInforBean getPersonalInforBean() {
        return personalInforBean;
    }

    public void setPersonalInforBean(PersonalInforBean personalInforBean) {
        this.personalInforBean = personalInforBean;
    }

    //选择的服务信息
    private WorkInfoBean workInfoBean;

    public WorkInfoBean getWorkInfoBean() {
        return workInfoBean;
    }

    public void setWorkInfoBean(WorkInfoBean workInfoBean) {
        this.workInfoBean = workInfoBean;
    }

    //是否使用套餐卡支付
    private boolean useTao = false;

    public boolean isUseTao() {
        return useTao;
    }

    public void setUseTao(boolean useTao) {
        this.useTao = useTao;
    }

    //预约的时间
    private OrderTimeBean orderTimeBean;

    public OrderTimeBean getOrderTimeBean() {
        return orderTimeBean;
    }

    public void setOrderTimeBean(OrderTimeBean orderTimeBean) {
        this.orderTimeBean = orderTimeBean;
    }

    private CardItemBean cardItemBean;

    public CardItemBean getCardItemBean() {
        return cardItemBean;
    }

    public void setCardItemBean(CardItemBean cardItemBean) {
        this.cardItemBean = cardItemBean;
    }

    public void getLocation() {
        DLocationUtils.getInstance().register(new OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                setLocation(location);
            }

            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    return;
                }
                Log.e(TAG, "定位方式：" + location.getProvider());
                Log.e(TAG, "纬度：" + location.getLatitude());
                Log.e(TAG, "经度：" + location.getLongitude());
                Log.e(TAG, "海拔：" + location.getAltitude());
                Log.e(TAG, "时间：" + location.getTime());
                setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (status == DLocationWhat.STATUS_ENABLE) {
                    getLocation();
                }
            }
        });
    }

    public void setLocation(Location location) {
    }

    private String nowUserId;

    public String getNowUserId() {
        return nowUserId;
    }

    public void setNowUserId(String nowUserId) {
        this.nowUserId = nowUserId;
    }

    private ServicePhoneResponseBean servicePhoneResponseBean;

    public ServicePhoneResponseBean getServicePhoneResponseBean() {
        return servicePhoneResponseBean;
    }

    public void setServicePhoneResponseBean(ServicePhoneResponseBean servicePhoneResponseBean) {
        this.servicePhoneResponseBean = servicePhoneResponseBean;
    }

    private ServiceUrlResponseBean serviceUrlResponseBean;

    public ServiceUrlResponseBean getServiceUrlResponseBean() {
        return serviceUrlResponseBean;
    }

    public void setServiceUrlResponseBean(ServiceUrlResponseBean serviceUrlResponseBean) {
        this.serviceUrlResponseBean = serviceUrlResponseBean;
    }
}
