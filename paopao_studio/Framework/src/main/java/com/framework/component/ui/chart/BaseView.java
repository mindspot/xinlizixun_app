package com.framework.component.ui.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 16/9/23.
 */
public abstract class BaseView extends View {
    public static final int DEFAULT_CIRCLE_LINE_STROKE_WIDTH = 20;

    protected final int hintColor = Color.parseColor("#e9e9e9");
    protected final int showColor = Color.parseColor("#00a0e8");

    protected Range range;
    protected int strokeWidth;// 环的宽度
    protected MyColor color;
    protected Progress progress;
    // 画圆所在的距形区域
    protected RectF mRectF;
    protected Paint mPaint;

    protected String mainText = "maintext", rightText = " righttext";

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBase();
        init(context, attrs, defStyleAttr);
        setSide();
    }

    private void initBase() {
        strokeWidth = DEFAULT_CIRCLE_LINE_STROKE_WIDTH;
        range = new Range();
        color = new MyColor();
        color.bottom = hintColor;
        color.top = showColor;

        progress = new Progress();
        mRectF = new RectF();
        mPaint = new Paint();
    }

    protected abstract void init(Context context, AttributeSet attrs, int defStyleAttr);


    private void setSide() {
        if (isInEditMode()) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                int width = getWidth();
                int height = getHeight();
                if (width <= 0) {
                    measure(height, height);
                    return;
                }

                if (height <= 0) {
                    measure(width, width);
                    return;
                }
                int min = Math.min(width, height);
                measure(min, min);
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initRange();
        initPaint(canvas);// 设置画笔相关属性
        initRectF();// 位置
    }

    private void initRange() {
        int width = this.getWidth();
        int height = this.getHeight();

        range.min = Math.min(width, height);
        range.max = Math.max(width, height);
        range.width = width;
        range.height = height;
    }

    private void initPaint(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initRectF() {
        mRectF.left = strokeWidth / 2 + (range.width - range.min) / 2; // 左上角x
        mRectF.top = strokeWidth / 2 + (range.height - range.min) / 2; // 左上角y
        mRectF.right = range.width - mRectF.left; // 左下角x
        mRectF.bottom = range.height - mRectF.top; // 右下角y
    }


    public void setMaxProgress(int maxProgress) {
        this.progress.max = maxProgress;
    }

    public void setCurrProgress(int currProgress) {
        progress.curr = currProgress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int currProgress) {
        progress.curr = currProgress;
        this.postInvalidate();
    }

    private static final int SLEEP_TIME = 16;// 一帧的时间
    private CountDownTimer animCountDownTimer;

    public void setProgressWithAnim(int progress) {
        canclePreviousCountDownTimerIfNeed();
        final int future = getFuture(progress);
        createAnimCountDownTimer(future);
        animCountDownTimer.start();
    }

    private void canclePreviousCountDownTimerIfNeed() {
        if (animCountDownTimer != null) {
            animCountDownTimer.cancel();
        }
    }

    private int getFuture(int progress) {
        return SLEEP_TIME * Progress.MAX_PROGRESS * progress / this.progress.max;
    }

    private void createAnimCountDownTimer(final int future) {
        animCountDownTimer = new CountDownTimer(future, SLEEP_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    setCurrProgress((int) ((future - millisUntilFinished) / SLEEP_TIME));
                } else {
                    setProgressNotInUiThread((int) ((future - millisUntilFinished) / SLEEP_TIME));
                }
            }

            @Override
            public void onFinish() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    setCurrProgress(future / SLEEP_TIME);
                } else {
                    setProgressNotInUiThread(future / SLEEP_TIME);
                }
            }
        };
    }


    public static class DrawTextUtil {
        private Canvas canvas;
        private Paint paint;
        private Range range;
        private MyColor color;
        private String mainText, rightText;

        // inner use
        private int mainHeight, rightHeight;
        private float mainTextX, mainTextY;
        private float mainHeightPercent = 2f / 5f;

        public DrawTextUtil() {
        }

        public void invoke(Canvas canvas, Paint paint, Range range, MyColor color, String mainText, String rightText, float mainHeightPercent) {
            this.canvas = canvas;
            this.paint = paint;
            this.range = range;
            this.color = color;
            this.mainText = mainText;
            this.rightText = rightText;
            this.mainHeightPercent = mainHeightPercent;

            draw(paint);
        }

        private void draw(Paint paint) {
            getHeights();
            paint.setColor(color.text);
            paint.setStyle(Paint.Style.FILL);
//            try {
//                paint.setTypeface(AppContext.getTypeface());
//            } catch (NullPointerException ignore) {
//            }
            // 测量进度值与‘%’的宽度，并计算进度值的基准线的x,y
            final int rightTextWidth = getRightTextWidth();
            final int maintextWidth = getMainTextWidth();
            getProgressTextPoint(rightTextWidth, maintextWidth);

            drawMainText();// 绘制 main text
            drawRightText(maintextWidth);// 绘制right text
        }

        private void getHeights() {
            mainHeight = (int) (range.min * mainHeightPercent);
            rightHeight = (int) (mainHeight / 2f);
        }

        private int getRightTextWidth() {
            return getTextWidth(rightHeight, rightText);
        }

        private int getMainTextWidth() {
            return getTextWidth(mainHeight, mainText);
        }

        private int getTextWidth(float height, String text) {
            paint.setTextSize(height);
            return (int) paint.measureText(text, 0, text.length());
        }

        private void getProgressTextPoint(int rightTextWidth, int maintextWidth) {
            Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
            mainTextX = (range.width - maintextWidth - rightTextWidth) / 2;
            mainTextY = (range.height + mainHeight) / 2 - fmi.bottom;
        }

        private void drawMainText() {
            paint.setTextSize(mainHeight);
            canvas.drawText(mainText, mainTextX, mainTextY, paint);
        }

        private void drawRightText(int progressWidth) {
            paint.setTextSize(rightHeight);
            canvas.drawText(rightText, mainTextX + progressWidth, mainTextY, paint);
        }
    }


    public static final class Range {
        public int width, height, max, min;
    }

    public static class MyColor {
        public int bottom, top, text;
    }

    public static class Progress {
        public static final int MAX_PROGRESS = 100;
        int curr = 36, max = MAX_PROGRESS;
    }

}
