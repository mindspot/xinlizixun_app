package com.kuangji.paopao.base;

/**
 * 操作结果异常 <b>编码&提示语</b> 枚举，按模块维护
 *
 * @author haolong
 */
public enum ResultCodeEnum {


    /**
     * 023 消息
     */
    ERROR_MESSAGE_ERR(-12300, "消息发送失败,请稍后再试"),

    /**
     * 024 SETTLED IN 入驻
     */
    ERROR_SETTLED_IN_ID_CARDS(-12400, "入驻身份证信息不符合规范"),
    ERROR_SETTLED_IN_DIPLOMA(-12401, "入驻学历信息不符合规范"),
    ERROR_SETTLED_IN_QUALIFICATION_CERTIFIC(-12402, "资历信息不合符规范"),
    ERROR_SETTLED_IN_INVITATIONCODE(-12403, "无此邀请码"),
    ERROR_SETTLED_IN_SUPERVISOR_TIME(-12404, "督导开始时间需小于督导结束时间"),
    ERROR_SETTLED_IN_WORK(-12405, "工作模式需选择"),
    ERROR_SETTLED_IN_SERIVICE(-12406, "至少选择一次单次服务"),
    ERROR_SETTLED_NO_SUPERVISOR_TIME(-12407, "督导结束时间不能为空"),
    ERROR_SETTLED_IN_QUALIFICATION_CERTIFIC_IMG(-12402, "督导证明信件"),
    ERROR_SETTLED_TRAININGCERTIFICATES_IN_IMG(-12403, "培训证书"),
    ERROR_SETTLED_CLASSIFICATION(-12405, "请选择擅长领域"),
    ERROR_SETTLED_CONTENTION(-12406, "争对人群出错"),
    ERROR_SETTLED_PAPER(-12407, "选择论文出错"),
    ERROR_SETTLED_PRACTITIONERS(-12408, "从业年限出错"),
    ERROR_SETTLED_IN_SERVICE_PRICE(-12406, "最低单次服务价格不能为0"),
    ERROR_SETTLED_IN_SERVICE_RESUME(-12407, "简历不能为空"),
    ERROR_SETTLED_IN_SERVICE_MESSAGE(-12408, "寄语不能为空"),
    ERROR_SETTLED_IN_SERVICE_IMG(-12409, "此类图片不能超出3个"),
    INVITATION_CODE_EXIST(-12410, "已有一个督导师拥有此督导码"),
    ERROR_INVITATION_CODE(-12411, "邀请码不正确"),
    ERROR_INVITATION_TIME_TYPE(-12412, "排班时间类型错误"),
    /**
     * 025WORK_TIME
     */
    ERROR_WOKK_TIME(-12500, "时间格式不正确"),
    ERROR_WRONG_WOKK_START_END(-12501, "不在合理时间范围内"),
    ERROR_WRONG_TIME_SLOT(-12502, "没有这个时间段"),
    ERROR_WRONG_TIME_NOT_NULL(-12503, "时间段不能为空"),
    ERROR_WRONG_TIME_REPEAT(-12504, "时间段重复"),
    ERROR_WRONG_TIME_NOT_THAN(-12505, "不在平台设定范围内"),
    ERROR_WRONG_TIME_NOT_TYPE(-12506, "平台时间段不能为空"),
    ERROR_WRONG_TIME_SERVICE(-12507, "服务时间低于平台设置服务时间"),
    ERROR_WRONG_TIME_NO_SERVICE(-12508, "当前没有设置这个服务时间"),
    ERROR_WRONG_TIME_USE_SERVICE(-12509, "当前时间已经被使用"),
    ERROR_WRONG_TIME_LAG(-12510, "预订时间不能必须小于系统时间"),
    /**
     * 026 USER
     */
    ERROR_USER_NULL(-12600, "没有对应用户数据"),
    ERROR_USER_NOT_CODE(-12601, "验证码错误"),
    ERROR_LOGIN_TO_EXAMINE(-12602, "审核未通过或者登入受限"),
    ERROR_CONSULTANT_LOGIN_TO_EXAMINE(-12602, "审核暂未通过或者登入受限"),
    ERROR_LOGIN_USER_TYPE(-12604, "身份不符合"),
    ERROR_LOGIN_PHONE(-12605, "不是手机号"),
    ERROR_LOGIN_CODE(-12606, "平台验证码发放错误,稍后再试试"),
    ERROR_LOGIN_TOKEN(-9999, "token验证出错请重新登入"),
    ERROR_CREATE_TOKEN(-9999, "token生成错误"),
    ERROR_LOGIN_NO_USER(-12605, "账户不存在"),
    ERROR_LOGIN_PWD(-12606, "密码错误"),
    ERROR_LOGIN_EMP_USER_NAME(-12607, "账户不能为空"),
    ERROR_LOGIN_CODE_EXISTS(-12608, "验证码已经发放,请注意查收"),
    ERROR_USER_PHONE_EXISTS(-12609, "当前手机号已经被注册"),
    ERROR_LOGIN_EMAIL(-12610, "不是邮箱"),
    ERROR_LOGIN_NOT_INSERT(-126011, "改账户已经入驻过"),
    ERROR_LOGIN_NOT_REALNAME(-126012, "不能修改当前名称"),
    ERROR_LOGIN_NOT_NULL_TOKEN(-9999, "token不能为空"),
    ERROR_LOGIN_BE_OVERDUE_TOKEN(-9999, "token过期"),
    ERROR_LOGIN_OUT_TOKEN(-9998, "当前账号别处登入"),
    ERROR_LOGIN_RESTRICTED_ENTRY(-9997, "限制登入"),
    /**
     * 027 COLLECTION
     */
    ERROR_COLLECTION_USER(-12700, "用户id出错！"),
    ERROR_COLLECTION_CONSULTANT(-12701, "咨询师id为出错！"),
    ERROR_COLLECTION_NO(-12702, "已经收藏了！"),
    ERROR_COLLECTION_TOKEN(-12703, "请登入后再搜藏"),

    /**
     * 028 WORK 预定时间
     */
    ERROR_WORK_TIME(-12800, "当前时间已经被预约！"),
    ERROR_WORK_NOT_BOOOK_TIME(-12801, "请设置预定时间！"),
    ERROR_WORK_BOOOK_TIME(-12802, "修改预约状态不对！"),

    /**
     * 029 Coupon 优惠劵
     */
    ERROR_COUPON_NO_GOODS(-12900, "优惠劵不存在或已经下架！"),
    ERROR_COUPON_NO_USE_GOODS(-12901, "优惠劵未达使用条件！"),
    ERROR_COUPON_USED(-12902, "优惠劵已经被使用！"),
    ERROR_COUPON_NO_USE_AMOUNT(-12903, "优惠劵使用金额不能小于或者等于0！"),
    ERROR_ORDER_WORK_TIME(-12903, "该咨询师当前时间已经被锁定！"),
    /**
     * 030 Order
     */
    ERROR_TRADE_ORDER_NO_GOODS(-13000, "请选择下单商品！"),
    ERROR_TRADE_ORDER_NO_SHOP(-13001, "店铺已歇业！"),
    ERROR_TRADE_ORDER_NO_BUYER(-13002, "请登录成功后再下单！"),
    ERROR_TRADE_ORDER_DOWN_GOODS(-13003, "您选择的商品已下架！"),
    ERROR_TRADE_ORDER_ILLGEAL_PARAMS(-13004, "创建订单参数无效！"),
    ERROR_TRADE_ORDER_ILLGEAL_GOODS(-13005, "选购商品信息无效！"),
    ERROR_ORDER_LESS_GOODS_AMOUNT(-13006, "商品金额不能低于1分钱！"),
    ERROR_ORDER_INSERT_FAIL(-13007, "订单入库失败！"),
    ERROR_ORDER_UNIFIED_FAIL(-13008, "统一下单！"),
    ERROR_ORDER_BAD_PAY_TYPE(-13009, "暂不支持该支付方式！"),
    ERROR_ORDER_NO_BUYER(-13010, "请填写收货人信息！"),
    ERROR_ORDER_FAIL(-13011, "下单失败！"),
    ERROR_ORDER_BAD_TYPE(-13012, "暂不支持该类型订单！"),
    ERROR_ORDER_NO_OPER(-13013, "请填写下单人信息！"),
    ERROR_ORDER_UPDATE_FAILD(-13014, "订单更新失败！"),
    ERROR_ORDER_NO_GOODS_VUID(-13015, "下单商品已售空或下降"),
    ERROR_ORDER_FAIL_VUID(-13016, "下单失败"),
    ERROR_ORDER_CANCEL_FAIL(-13017, "该订单无法取消！"),
    ERROR_ORDER_CANNOT_CANCEL(-13018, "该订单不支持取消操作!"),
    ERROR_ORDER_NOT_EXIST(-13019, "无效订单!"),
    ERROR_ORDER_CANNOT_SEND(-13020, "该订单不支持发货操作!"),
    ERROR_ORDER_SEND_FAIL(-13021, "发货失败!"),
    ERROR_ORDER_FINISH_FAIL(-13022, "该订单无法关闭!"),
    ERROR_ORDER_NOT_SUPPORT_FINISH(-13023, "线上订单无法直接关单!"),
    ERROR_ORDER_NO_FINISH(-13024, "订单未完成，不能结束!"),
    ERROR_ORDER_NO_PAY_SUCC(-13025, "该订单为未支付订单!"),
    ERROR_ORDER_NOT_PAY_WAIT(-13026, "订单已失效，无法支付!"),
    ERROR_ORDER_GEN_PAY_FAIL(-13027, "生成支付信息失败!"),
    ERROR_ORDER_HOTEL_NO_STAYER(-13028, "请填写入住人信息!"),
    ERROR_ORDER_HOTEL_NO_CONTACT(-13029, "请填写联系方式!"),
    ERROR_ORDER_CANCEL_NOSUPPORT(-13030, "该订单不支持取消操作!"),
    ERROR_ORDER_BAD_FROM(-13031, "订单来源无效！"),
    ERROR_ORDER_NO_ADDR(-13032, "请填写收货信息！"),
    ERROR_ORDER_NO_GOODS(-13036, "无效订单商品"),
    ERROR_ORDER_CHECKIN_FAIL(-13037, "办理入住失败"),
    ERROR_ORDER_REFUND_FAIL(-13038, "该订单已申请过退款"),
    ERROR_ORDER_NOT_FIN_FAIL(-13039, "暂无可使用订单"),
    ERROR_ORDER_NOT_FIND_ORDER(-13040, "当前没有此类型订单！"),
    ERROR_ORDER_NOT_BUY(-13041, "当前存在2笔取消订单无法下单！"),
    ERROR_ORDER_NOT_PAY_ORDER(-13042, "当前存在未支付订单！"),
    ERROR_ORDER_CONFIRM(-13043, "当前订单已被取消！"),
    ERROR_ORDER_END(-13044, "当前订单已经被督导完毕！"),
    /**
     * 031 Goods
     */
    ERROR_GOODS_BAD_PRICE(-13100, "价格不能为0！"),
    ERROR_GOODS_BAD_STOCK(-13101, "库存不能为空！"),
    ERROR_GOODS_NO_IMGS(-13102, "请至少上传一张图片！"),
    ERROR_GOODS_INVALID_CLASS(-13103, "不支持该商品类型！"),
    ERROR_GOODS_ADD_FAIL(-13104, "发布商品失败！"),
    ERROR_GOODS_UPDATE_FAIL(-13105, "发布商品失败！"),
    ERROR_GOODS_UPDOWN(-13106, "商品上/下架操作失败"),
    ERROR_GOODS_NOTEXIST(-13107, "商品不存在"),
    ERROR_GOODS_IMAGE_UPDATE_FAIL(-13108, "商品图片修改失败"),
    ERROR_GOODS_UPDATE_PRICE_FAIL(-13109, "价格调整失败"),
    ERROR_GOODS_UPDATE_STOCK_FAIL(-13110, "库存调整失败"),
    ERROR_GOODS_UPDATE_NAME_FAIL(-13111, "名称编辑失败"),
    ERROR_GOODS_BAD_NAME(-13112, "商品名称不能为空"),
    ERROR_GOODS_SET_MEAL_NO_TIMES(-13113, "套餐商品使用次数不足"),
    ERROR_GOODS_SET_MEALS_BUY(-13114, "套餐还没使用完毕"),
    SERVICE_VERIFY_WAITING(-13115, "你有服务正在审核"),
    /**
     * 032 Consultant
     */
    INVITATION_CODE_EXISTED(-13200, "督导码已经存在"),
    /**
     * 033 Consultant
     */
    NO_SUB_COMMISSION_RECORD(-13300, "分佣记录不存在"),

    /**
     * 034 Coupon
     */
    COUPON_REDEEM_CODE_ERROR(-13400, "优惠券兑换码错误"),
    COUPON_STATUS_ERROR(-13401, "优惠券已失效"),
    COUPON_QTY_ERROR(-13402, "优惠券数量不足"),
    COUPON_SENDED(-13403, "优惠券已发放"),
    /**
     * 995 Pay（支付）
     */
    ERROR_PAY_NO_PAYTYPE(-99500, "请选择支付方式！"),
    ERROR_PAY_INVALID_PAYTYPE(-99501, "暂不支持该支付方式！"),
    ERROR_PAY_NO_OUT_TRADE_NO(-99502, "商户支付单号无效！"),
    ERROR_PAY_NO_IP(-99503, "终端IP无效！"),
    ERROR_PAY_NO_OPENID(-99504, "微信用户无法识别！"),
    ERROR_PAY_NO_BODY(-99505, "支付内容无效！"),
    ERROR_PAY_INVALID_FEE(-99506, "支付金额不得低于1分！"),
    ERROR_PAY_FAIL(-99507, "支付失败！"),
    ERROR_PAY_NO_TRADE_NO(-99508, "交易号无效"),
    ERROR_PAY_NO_ACCOUNT(-99509, "余额不足"),
    ERROR_PAY_NO_SET_MEAL(-99510, "套餐卡支付失败"),

    /**
     * 996 微信
     */
    ERROR_MP_AUTH_NEWUSER(-99600, "微信认证失败"),

    /**
     * 999 图片服务
     */
    ERROR_IMAGE_NULLFILE(-99900, "无效的文件！"),
    ERROR_IMAGE_FILENAME(-99901, "无效的文件名称！"),
    ERROR_IMAGE_SUFFIX(-99902, "无效的文件后缀"),
    /* ========================================【全局通用--99】提示 Start ========================================*/
    /**
     * 操作成功
     */
    SUCC(0, "操作成功！"),

    /**
     * 新增成功
     */
    CREATE_SUCESS(1001, "新增成功，请查看详情！"),

    /**
     * 编辑成功
     */
    EDIT_SUCESS(1002, "编辑成功，请查看详情！"),

    /**
     * 删除成功
     */
    DELETE_SUCESS(1003, "删除成功！"),


    /**
     * 系统异常
     */
    FAIL(-1000, "操作失败,请稍后再试!"),
    FAIL_LOGIN(-1001, "用户信息已失效，请重新登录"),

    /**
     * 参数错误异常
     */
    ILLGEAL_PARAMTER(-1012, "请求参数不完整"),

    /**
     * 新增内容已存在异常
     */
    DUPLICATE_RECORD(-1013, "抱歉，您要新增的内容已存在"),

    /**
     * 待处理数据集为空
     */
    EMPTY_LIST(-1014, "抱歉，待处理数据集为空"),

    /**
     * 查询不到数据
     */
    NON_EXIST_DATA(-1015, "请求数据不存在！"),

    API_TIMEOUT(-1016, "接口调用超时"),

    API_ERROR_SIGN(-1017, "验签失败"),

    /**
     * SQL 操作异常（SQLException）
     */
    SQL_EXCEPTION(-1101, "抱歉，数据库操作失败，请联系系统管理员"),

    /**
     * SQL 语句语法异常（BadSqlGrammarException）
     */
    SQL_GRAMMAR_EXCEPTION(-1102, "抱歉，数据库操作失败，请联系系统管理员"),

    /**
     * SQL 语句语法异常（MySQLSyntaxErrorException）
     */
    MYSQL_SYNTAX_EXCEPTION(-1103, "抱歉，数据库操作失败，请联系系统管理员"),;


    /* ========================================【全局通用】提示 End ========================================*/


    /**
     * 操作结果编码，前缀可区分不同业务模块
     */
    private final int code;

    /**
     * 操作结果提示，如：成功、失败、各种异常原因等
     */
    private final String msg;

    /**
     * 构造函数
     *
     * @param resultCode    操作结果编码，前缀可区分不同业务模块
     * @param resultMessage 操作结果提示，如：成功、失败、各种异常原因等
     */
    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取操作结果编码，前缀可区分不同业务模块
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取操作结果提示，如：成功、失败、各种异常原因等
     */
    public String getMsg() {
        return msg;
    }
}
