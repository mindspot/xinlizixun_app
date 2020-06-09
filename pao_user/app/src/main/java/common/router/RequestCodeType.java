package common.router;

/**
 * startActivityForResult中的requestCode的枚举，所有的requestCode，都在这里
 *
 * @author Administrator
 * @date 2017/7/29
 */
public class RequestCodeType {

    public static final int BASE_RC = 1000;

    // 魔蝎
    public static final int REQUEST_CODE_MOXIE_EMAIL_BILLS = BASE_RC + 100;// 邮箱账单
    public static final int REQUEST_CODE_MOXIE_ALIPAY = BASE_RC + 101;// 支付宝账单
    public static final int REQUEST_CODE_MOXIE_EBANK_BILLS = BASE_RC + 102;// 网银账单
    public static final int REQUEST_CODE_MOXIE_INSURANCE = BASE_RC + 103;// 车险
    public static final int REQUEST_CODE_MOXIE_LIFEINSURANCE = BASE_RC + 104;// 保单

    public final static int ACCUMULATION_FUND_CODE_LOGIN_SUCCESS = BASE_RC + 110;// 公积金认证数据采集成功

    // 紧急联系人界面 AuthEmergencyContactActivity
    public final static int CODE_GET_CONTACT = BASE_RC + 111;// 直系亲属联系人
    public final static int CODE_GET_CONTACT2 = BASE_RC + 112;// 其他联系人
    public final static int CODE_GET_CONTACT_FOR_COMMAND = BASE_RC + 113;// 打开通讯录的指令
    // webview
    public final static int WEBVIEW_CODE_TAKE_PHOTO = BASE_RC + 122;//选择拍照
    public final static int WEBVIEW_CODE_CHOOSE_ALBUM = BASE_RC + 123;//选择相册
    public final static int WEBVIEW_CODE_SMS_SHARED = BASE_RC + 124; //短信分享成功

    // TakePhotoActivity
    public final static int TAKE_PHOTO_CODE_TAKE_PHOTO = BASE_RC + 132;//选择拍照
    public final static int TAKE_PHOTO_CODE_CHOOSE_ALBUM = BASE_RC + 133;//选择相册

    // UpLoadPictureActivity
    public final static int UPLOAD_CODE_TAKE_PHOTO = BASE_RC + 134;


    // PersonalDetailActivity
    public static final int TAKE_PHOTO_IDCARD_FAONT = 1;// 照相机-身份证正面
    public static final int TAKE_PHOTO_IDCARD_BACK = 2;// 照相机-身份证反面
    public static final int TAKE_PHOTO_FACE = 3;// 照相机-拍脸

    public static final int PAGE_INTO_LIVENESS = BASE_RC + 140;// face++拍脸:活体检测
    public static final int INTO_IDCARDSCAN_PAGE = BASE_RC + 141;// face++身份证正面
    public static final int INTO_IDCARDSCAN_PAGE_BACK = BASE_RC + 142;// face++身份证反面

    // weex
    public static final int WEEX = BASE_RC + 200;
    // weex 选择图片
    public static final int WEEX_PICK_IMAGE = BASE_RC + 201;

    public static final int WEBVIEW_OPEN_FILE_CHOOSER_CAMERA = 7001;
    public static final int WEBVIEW_OPEN_FILE_CHOOSER_CHOOSE = 7002;

    public static final int WEBVIEW_WX_PAY_FINSH = 8001;//微信 支付
    public static final int WECHAT_GOTO_MINI = BASE_RC + 1001;//跳转微信小程序
    public static final int ADDRESS_ADD_ADDRESS = 8101;// 添加收货地址
    public static final int ADDRESS_UPDATE_ADDRESS = 8102;// 添加修改地址


}
