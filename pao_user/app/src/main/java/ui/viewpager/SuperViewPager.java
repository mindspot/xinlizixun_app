package ui.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_user
 * @Package ui.viewpager
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.05.29 下午 2:26
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class SuperViewPager extends ViewPager {


    private ViewPageHelper helper;

    public SuperViewPager(Context context) {
        this(context, null);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        helper = new ViewPageHelper(this);

    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        MScroller scroller = helper.getScroller();
        if (Math.abs(getCurrentItem() - item) > 1) {
            scroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }
}
