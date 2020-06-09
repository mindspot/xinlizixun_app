package com.framework.component.ui.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016-09-23
 */
public class TwoRoundProgressView extends BaseView {
    private static final int START_ANGLE = -90;
    private static final int SWEEP_ANGLE = 360;

    private RectF innerRect;
    private DrawTextUtil drawTextUtil;

    public TwoRoundProgressView(Context context) {
        this(context, null);
    }

    public TwoRoundProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoRoundProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        innerRect = new RectF();
        drawTextUtil = new DrawTextUtil();
        if (isInEditMode()) {
            return;
        }
        color.bottom = hintColor;
        color.top = showColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setOutterCircle(canvas);
        setInnerCircle(canvas);
        color.text = color.top;
        drawTextUtil.invoke(canvas, mPaint, range, color, mainText, rightText, (1f / 3f));
    }

    private void setOutterCircle(Canvas canvas) {
        mPaint.setColor(color.bottom);
        canvas.drawArc(mRectF, START_ANGLE, SWEEP_ANGLE, false, mPaint);
    }

    private void setInnerCircle(Canvas canvas) {
        initInnerRect();
        mPaint.setColor(color.top);
        canvas.drawArc(innerRect, START_ANGLE, ((float) progress.curr / progress.max) * SWEEP_ANGLE, false, mPaint);
    }

    private void initInnerRect() {
        final int halfStrokeWidth = strokeWidth / 2;
        innerRect.left = mRectF.left + halfStrokeWidth; // 左上角x
        innerRect.top = mRectF.top + halfStrokeWidth; // 左上角y
        innerRect.right = mRectF.right - halfStrokeWidth;// 左下角x
        innerRect.bottom = mRectF.bottom - halfStrokeWidth; // 右下角y
    }


    public void setColor(int innerColor, int outterColor) {
        color.top = innerColor;
        color.bottom = outterColor;
    }

    public void setText(float percent, String mainText) {
        setText(percent, mainText, "");
    }

    public void setText(float percent, String mainText, String rightText) {
        percent = Math.min(percent, 1.0f);
        setProgressWithAnim((int) (Math.max(percent, 0f) * 100));

        this.mainText = mainText == null ? "" : mainText;
        this.rightText = rightText == null ? "" : rightText;
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }
}
