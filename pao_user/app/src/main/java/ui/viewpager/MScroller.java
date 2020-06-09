package ui.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

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
public class MScroller  extends Scroller {

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };


    public boolean noDuration;

    public void setNoDuration(boolean noDuration) {
        this.noDuration = noDuration;
    }

    public MScroller(Context context) {
        this(context,sInterpolator);
    }

    public MScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if(noDuration)
            //界面滑动不需要时间间隔
            super.startScroll(startX, startY, dx, dy, 0);
        else
            super.startScroll(startX, startY, dx, dy,duration);
    }
}
