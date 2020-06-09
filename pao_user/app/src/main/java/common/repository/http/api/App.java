package common.repository.http.api;


import com.framework.page.Page;

import common.repository.http.HttpApiBase;
import common.repository.http.HttpCallback;
import common.repository.http.entity.app.ServicePhoneResponseBean;
import common.repository.http.entity.app.ServiceUrlResponseBean;
import common.repository.http.entity.app.UpdateAppResponseBean;
import common.repository.http.entity.artivles.ArtivlesResponseBean;
import common.repository.http.entity.counsel.CounselHeaderResponseBean;
import common.repository.http.entity.counsel.CounselListResponseBean;
import common.repository.http.entity.counsel.detail.CounselDetailResponseBean;
import common.repository.http.entity.home.HomeResponseBean;
import common.repository.http.entity.order.ConsultantOrderResponseBean;
import common.repository.http.entity.order.ConsumeRecordResponseBean;
import common.repository.http.entity.order.OrderExplainResponseBean;
import common.repository.http.entity.order.OrderListResponseBean;
import common.repository.http.entity.order.OrderUserInfoResponseBean;
import common.repository.http.entity.user.UserInfoBean;
import common.repository.http.entity.user.UserInfoResponseBean;
import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.BaseResponseBean;
import common.repository.http.param.FileBean;
import common.repository.http.param.app.GetCodeRequestBean;
import common.repository.http.param.app.LoginRequestBean;
import common.repository.http.param.app.UploadAddressRequestBean;
import common.repository.http.param.artivles.ArtivlesRequestBean;
import common.repository.http.param.balance.BalanceRecordRequestBean;
import common.repository.http.param.balance.SaveCardRequestBean;
import common.repository.http.param.balance.WithdrawRequestBean;
import common.repository.http.param.chat.ChatStatusRequestBean;
import common.repository.http.param.counsel.CollectRequestBean;
import common.repository.http.param.counsel.CounselListRequestBean;
import common.repository.http.param.counsel.SearchCounselListRequestBean;
import common.repository.http.param.order.CancleOrderRequestBean;
import common.repository.http.param.order.ConsultantOrderRequestBean;
import common.repository.http.param.order.ConsumeRecordRequestBean;
import common.repository.http.param.order.PayOrderInfoRequestBean;
import common.repository.http.param.user.ChatUserStatusRequestBean;
import common.repository.http.param.user.FeedBackRequestBean;
import common.repository.http.param.user.SaveUserInfoRequestBean;
import util.ServiceConfig;

/**
 * Created by Administrator on 2017/3/6.
 */
public class App extends HttpApiBase {


    public static App instance() {
        return Helper.instance;
    }

    private static final class Helper {
        static final App instance = new App();
    }

    private App() {

    }

    //首页
    public void getHomeInfo(Page page, BaseRequestBean bean, final HttpCallback<HomeResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_HOME_URL);
        getHttp().get(page, url, bean, callback);
    }

    //文章
    public void getArtivlesInfo(Page page, ArtivlesRequestBean bean, final HttpCallback<ArtivlesResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ARTIVLES_URL + "/" + bean.getCommId() + "/" + bean.getPageNum());
        getHttp().get(page, url, bean, callback);
    }

    //咨询头部信息
    public void getCounselHeaderInfo(Page page, BaseRequestBean bean, final HttpCallback<CounselHeaderResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUNSEL_HEADER_URL);
        getHttp().get(page, url, bean, callback);
    }

    //咨询信息
    public void getCounselListInfo(Page page, CounselListRequestBean bean, final HttpCallback<CounselListResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUNSEL_LIST_URL);
        getHttp().postJSON(page, url, bean, callback);
    }

    //咨询信息
    public void searchCounselListInfo(Page page, SearchCounselListRequestBean bean, final HttpCallback<CounselListResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SEARCH_COUNSEL_LIST_URL);
        getHttp().get(page, url, bean, callback);
    }

    //咨询师信息
    public void getCounselInfo(Page page, int id, final HttpCallback<CounselDetailResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUNSEL_DETAIL_URL + "/" + id);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //收藏咨询师
    public void insertCollect(Page page, CollectRequestBean bean, final HttpCallback<BaseResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUNSEL_INSERT_COLLECT_URL);
        getHttp().post(page, url, bean, callback);
    }

    //取消收藏咨询师
    public void delCollect(Page page, int id, final HttpCallback<BaseResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUNSEL_DEL_COLLECT_URL + "/" + id);
        getHttp().delete(page, url, new BaseRequestBean(), callback);
    }

    //获取优惠卷列表
    public void getCouponList(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUPON_LIST_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //获取可用优惠卷列表
    public void getUseCouponList(Page page, String money, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COUPON_LIST_URL + "/" + money);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    public void gotoLogin(Page page, LoginRequestBean bean, final HttpCallback<UserInfoBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_LOGIN_URL);
        getHttp().post(page, url, bean, callback);
    }

    public void getCode(Page page, GetCodeRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_CODE_URL);
        getHttp().post(page, url, bean, callback);
    }

    //订单
    public void getOrderList(Page page, int pageNum, final HttpCallback<OrderListResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ORDER_LIST_URL + "/" + pageNum);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //获取收藏
    public void getCollectList(Page page, int pageNum, final HttpCallback<CounselListResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COLLECT_LIST_URL + "/" + pageNum);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //订单信息填写页面
    public void getOrderUserInfo(Page page, final HttpCallback<OrderUserInfoResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ORDER_USERINFO_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //下普通订单
    public void buyConsultantOrder(Page page, ConsultantOrderRequestBean bean, final HttpCallback<ConsultantOrderResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CONSULTANT_ORDER_URL);
        getHttp().postJSON(page, url, bean, callback);
    }

    //下套餐订单
    public void buyTaoConsultantOrder(Page page, ConsultantOrderRequestBean bean, final HttpCallback<ConsultantOrderResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_TAO_CONSULTANT_ORDER_URL);
        getHttp().postJSON(page, url, bean, callback);
    }

    //获取用户信息
    public void getUserInfo(Page page, final HttpCallback<UserInfoResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_USERINFO_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //保存用户信息
    public void saveUserInfo(Page page, SaveUserInfoRequestBean bean, final HttpCallback<UserInfoResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SAVE_USERINFO_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取订单说明
    public void getOrderExplain(Page page, final HttpCallback<OrderExplainResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ORDER_EXPLAIN_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //套餐购买
    public void orderBuyTao(Page page, ConsultantOrderRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ORDER_TAOCAN_URL);
        getHttp().put(page, url, bean, callback);
    }


    //获取预约时间
    public void getTime(Page page, int id, String date, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_DATE_URL + "/" + id + "/" + date);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }


    //上传图片
    public void uploadImage(Page page, FileBean bean, final HttpCallback<String> callback) {
        String url = ServiceConfig.SERVICE_UPLOAD_IMAGE_URL;
        getHttp().upload(page, url, bean, callback);
    }

    //购买套餐列表
    public void getSetMealList(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SETMEAL_LIST_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //反馈
    public void uploadFeedback(Page page, FeedBackRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_UPLOAD_FEEDBACK_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取支付宝 预支付信息
    public void getPayInfo(Page page, PayOrderInfoRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_ALI_PAY_INFO_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取通话权限
    public void getChatInfo(Page page, ChatStatusRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CHAT_STATUS_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取地址信息
    public void getAddressInfo(Page page, final HttpCallback<String> callback) {
        getHttp().addressInfo(page, callback);
    }

    //上报地址信息
    public void uploadAddressInfo(Page page, UploadAddressRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_UPLOAD_AADDRESS_INFO_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取绑定卡列表
    public void saveUserCard(Page page, SaveCardRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SAVE_USER_CARD_INFO_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取余额
    public void getUserBalance(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_USER_YUE_URL);
        getHttp().post(page, url, new BaseRequestBean(), callback);
    }

    //获取绑定卡列表
    public void getUserCardList(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_USER_CARD_LIST_URL);
        getHttp().post(page, url, new BaseRequestBean(), callback);
    }

    //提现记录
    public void getBalanceRecord(Page page, BalanceRecordRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_WITHDRAW_RECORD_LIST_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取提现验证码
    public void getWithdrawCode(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_GET_WITHDRAW_CODE_URL);
        getHttp().post(page, url, new BaseRequestBean(), callback);
    }

    //提现申请
    public void withdraw(Page page, WithdrawRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_WITHDRAW_URL);
        getHttp().post(page, url, bean, callback);
    }

    //刷新token
    public void refreshToken(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_REFRESH_TOKEN_URL);
        getHttp().post(page, url, new BaseRequestBean(), callback);
    }

    //取消订单
    public void cancleOrder(Page page, CancleOrderRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CANCLE_ORDER_URL);
        getHttp().post(page, url, bean, callback);
    }

    //获取城市信息
    public void getCityInfos(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CITY_INFOS_URL);
        getHttp().post(page, url, new BaseRequestBean(), callback);
    }


    //获取服务电话
    public void getServicePhone(Page page, final HttpCallback<ServicePhoneResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SERVICE_PHONE_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //获取公司介绍
    public void getCompanyInfo(Page page, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_COMPANY_INFO_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //获取服务地址
    public void getServiceConfigUrl(Page page, final HttpCallback<ServiceUrlResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_SERVICE_URL_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //获取App更新信息
    public void getUpdateAppInfo(Page page, final HttpCallback<UpdateAppResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_UPDATE_APP_URL);
        getHttp().get(page, url, new BaseRequestBean(), callback);
    }

    //流水记录
    public void getCounsumeRecordList(Page page, ConsumeRecordRequestBean bean, final HttpCallback<ConsumeRecordResponseBean> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CONSUME_RECORD_URL);
        getHttp().get(page, url, bean, callback);
    }

    //咨询师是否在线
    public void getChatUserLineStatus(Page page, ChatUserStatusRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_CHAT_USER_LINE_STATUS_URL);
        getHttp().get(page, url, bean, callback);
    }

    //提醒咨询师上线
    public void remindChatUserLine(Page page, ChatUserStatusRequestBean bean, final HttpCallback<String> callback) {
        String url = getServiceUrl(ServiceConfig.SERVICE_REMIND_CHAT_USER_LINE_URL);
        getHttp().post(page, url, bean, callback);
    }
}
