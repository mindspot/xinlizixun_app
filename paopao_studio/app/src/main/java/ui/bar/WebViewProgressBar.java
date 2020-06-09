package ui.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import com.paopao.paopaostudio.R;


/**
 * 加载进度条
 * @author changed by calex_feng
 * @version V1.1
 * @date 2015-06-01
 */
public class WebViewProgressBar extends ProgressBar {
	private int curProgress;
	private int maxProgress;
	private static final int INTERVAL = 1;
	private static final int TAG_WEBVIEW=100;
	public WebViewProgressBar(Context context) {
		super(context);
//		initPaint();
	}
	public WebViewProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initPaint();
    }
	public WebViewProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initPaint();
    }
	
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	
	/**
	 * 初始化画笔
	 * 在上面书写类似百分比的文字
	 */
	private void initPaint(){
		this.setBackgroundColor(getResources().getColor(R.color.theme_color));
	}
	
	public void setTheBackground(int color)
	{
		this.setBackgroundColor(color);
	}
	
	/**
	 * 设置进度条动画
	 * @param progress 进度
	 */
	public synchronized void setAnimProgress(int progress) {
		maxProgress = progress;
		curProgress = 0;
		mHandler.sendEmptyMessageDelayed(INTERVAL, 5);
	}
	public synchronized void setAnimProgress2(int progress) {
		maxProgress = progress;
		curProgress = this.getProgress();
		Message msg=mHandler.obtainMessage();
		msg.what=TAG_WEBVIEW;
		mHandler.sendMessageDelayed(msg, 5);
	}
	
	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INTERVAL:
				if (curProgress < maxProgress) {
					setProgress(curProgress);
					curProgress ++;
					mHandler.sendEmptyMessageDelayed(INTERVAL, 5);
				} else {
					setProgress(curProgress);
				}
				
				break;
			case TAG_WEBVIEW:
				if (curProgress < maxProgress) {
					setProgress(curProgress);
					curProgress ++;
					mHandler.sendEmptyMessageDelayed(TAG_WEBVIEW, 5);
				} else {
					setProgress(curProgress);
				}
				if(curProgress==100){
					Animation animationUP = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,-1.0f);
					animationUP.setDuration(600);
					animationUP.setFillAfter(true);
					WebViewProgressBar.this.setProgress(0);
					WebViewProgressBar.this.setVisibility(View.GONE);
				}
				break;
			default:
				setProgress(curProgress);
				break;
			}
		};
	};
}
