/**
 * @author wuyan
 *
 */
package com.framework.utils;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class UIDisplayAdaptiveUtil {
	
	private static UIDisplayAdaptiveUtil adaptiveUtil = new UIDisplayAdaptiveUtil();
	//标准分辨率/1920     1080     3.0
	private int default_px_h = 1080;
	private int default_px_w = 1920;
	private float default_density = 3.0f;
	private float widthScale;
	private float heightScale;
	private int screenWidth = 1280,screenHeight = 800;
	private float screenDensity=1.0f;
	public static UIDisplayAdaptiveUtil getInstance(){
		return adaptiveUtil;
	}
	/**
	 * 初始化屏幕信息,获取屏幕宽高
	 * @param metrics
	 */
	public void init(DisplayMetrics metrics){
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        screenDensity =  metrics.density;
        initAdapterScale();
	}
	
	
	private void initAdapterScale() {
		 widthScale = screenWidth* default_density / (default_px_w * screenDensity);
		 heightScale = screenHeight * default_density / (default_px_h *screenDensity);
		 
	}
	
	/**
	 * 获取自适应后的横向数值
	 * @param landStandard 为1280X800下地标准数值
	 * @return
	 */
	public int getLandScape(int landStandard){
		int land = (int) (landStandard*widthScale);
		return land;
	}
	
	/**
	 * 获取自适应后的竖向数值
	 * @param portraitStandard 为1280X800下地标准数值
	 * @return
	 */
	public int getPortrait(int portraitStandard){
		int portrait = (int) (portraitStandard*heightScale);
		return portrait;
	}
	/**
	 * 自适应所有View，包含所有child
	 * 注意：viewgroup必须已经被add或者从xml中获取，否则没有LayoutParams
	 * @param viewGroup
	 */
	public void adaptiveChildView(ViewGroup viewGroup){
		if(null == viewGroup){
			return;
		}
		int childCount = viewGroup.getChildCount();
		View child;
		for(int i = 0;i < childCount;i++){
			child = viewGroup.getChildAt(i);
			if(child instanceof ViewGroup){
				adaptiveChildView((ViewGroup)child);
			}else{
				adaptiveView(child);
			}
		}
		adaptiveView(viewGroup);
	}
	/**
	 * 自适应当前view
	 * 注意：view必须已经被add或者从xml中获取，否则没有LayoutParams
	 * @param view
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void adaptiveView(View view){
		if(null == view || null == view.getLayoutParams()){
			return;
		}
		
		// 如果是WRAP_CONTENT，测量出高度宽度，适配
		if(view.getLayoutParams().width == LayoutParams.WRAP_CONTENT && !(view instanceof ViewGroup)){
//		if(view.getLayoutParams().width == LayoutParams.WRAP_CONTENT){
			//创造不一致条件，强制重新绘制
//			view.measure(view.getMeasuredWidth() + 1, view.getMeasuredHeight() + 1);
//			view.measure(0, 0);
			/**
			 * 	①、UNSPECIFIED(未指定)，父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小； 
				②、EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小； 
				③、AT_MOST(至多)，子元素至多达到指定大小的值。
			 */
			
			int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
			int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
			view.measure(w, h);
			if(view instanceof TextView){
				//自适应的TextView不需要重新设置宽度
//				((TextView) view).setWidth(getLandScape(view.getMeasuredWidth()));
			}else if(view instanceof ImageView){
				view.getLayoutParams().width = getLandScape(view.getMeasuredWidth());
			}else{
				view.getLayoutParams().width = getLandScape(view.getMeasuredWidth());
			}
			//如果不是MATCH_PARENT，那肯定是固定值，简单的拿过来适配
		}else if(view.getLayoutParams().width != LayoutParams.MATCH_PARENT && view.getLayoutParams().width != LayoutParams.WRAP_CONTENT){
			view.getLayoutParams().width = getLandScape(view.getLayoutParams().width);
		}
		//适配位置
		view.setX(getLandScape((int) view.getX()));
		view.setY(getPortrait((int)view.getY()));
		
		if(view.getLayoutParams().height == LayoutParams.WRAP_CONTENT && !(view instanceof ViewGroup)){
//		if(view.getLayoutParams().height == LayoutParams.WRAP_CONTENT){
			//创造不一致条件，强制重新绘制
//			view.measure(view.getMeasuredWidth() - 1, view.getMeasuredHeight() - 1);
//			view.measure(0, 0);
			int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
			int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
			view.measure(w, h);
			if(view instanceof TextView){
				((TextView) view).setHeight(getPortrait(view.getMeasuredHeight()));
			}else if(view instanceof ImageView){
				view.getLayoutParams().height = getPortrait(view.getMeasuredHeight());
			}else{
				view.getLayoutParams().height = getPortrait(view.getMeasuredHeight());
			}
		}else if(view.getLayoutParams().height != LayoutParams.MATCH_PARENT && view.getLayoutParams().height != LayoutParams.WRAP_CONTENT){
			view.getLayoutParams().height = getPortrait(view.getLayoutParams().height);
		}
		LayoutParams param = view.getLayoutParams();
		adaptiveViewByMargin(view);
		if(param instanceof AbsoluteLayout.LayoutParams){
			((AbsoluteLayout.LayoutParams)param).x = getLandScape((int) ((AbsoluteLayout.LayoutParams)param).x);
			((AbsoluteLayout.LayoutParams)param).y = getPortrait((int) ((AbsoluteLayout.LayoutParams)param).y);
		}
		view.setPadding(getLandScape(view.getPaddingLeft()), getPortrait(view.getPaddingTop()), getLandScape(view.getPaddingRight()), getPortrait(view.getPaddingBottom()));
		
//		view.setTop(getPortrait(view.getTop()));
//		view.setBottom(getPortrait(view.getBottom()));
//		view.setLeft(getLandScape(view.getLeft()));
//		view.setRight(getLandScape(view.getRight()));
	}
	
	public void adaptiveViewByMargin(View view){
		if(null == view || null == view.getLayoutParams()){
			return;
		}
		LayoutParams param = view.getLayoutParams();
		if(param instanceof MarginLayoutParams){
			((MarginLayoutParams)param).topMargin = getPortrait(((MarginLayoutParams)param).topMargin);
			((MarginLayoutParams)param).rightMargin = getLandScape(((MarginLayoutParams)param).rightMargin);
			((MarginLayoutParams)param).leftMargin = getLandScape(((MarginLayoutParams)param).leftMargin);
			((MarginLayoutParams)param).bottomMargin = getPortrait(((MarginLayoutParams)param).bottomMargin);
		}
	}
}
