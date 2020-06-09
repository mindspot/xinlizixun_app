package common.repository.http.entity.base;

/**
 * Created by User on 2018/1/26.
 * <p>
 * 样式 基类
 */

public class StyleBean {

    /**
     * listview的count从0开始计数，所以type不是从0开始的话，count要加一
     */
    public static final int TYPE_VIEW_COUNT = 7;

    /**
     * 带subtitle的view
     */
    public static final int TYPE_VIEW_SUBTITLE = 1;
    /**
     * 普通view
     */
    public static final int TYPE_VIEW_NOSUBTITLE = 2;
    /**
     * 间隔
     */
    public static final int TYPE_VIEW_INTERVAL = 3;
    /**
     * 文案再中间的View
     */
    public static final int TYPE_VIEW_CENTER_TEXT = 4;
    /**
     * 下滑线
     */
    public static final int TYPE_VIEW_DIVIDING_LINE = 5;
    /**
     * 带红点的view
     */
    public static final int TYPE_VIEW_RED_HOT = 6;

    /**
     * 图标地址url
     */
    private String icon;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String sub_title;
    /**
     * 提示文案
     */
    private String tips;
    /**
     * 红点内数量
     */
    private int badgeValue;

    public int getBadgeValue() {
        return badgeValue;
    }

    public void setBadgeValue(int badgeValue) {
        this.badgeValue = badgeValue;
    }

    /**
     * 样式类型
     */
    private int type;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
