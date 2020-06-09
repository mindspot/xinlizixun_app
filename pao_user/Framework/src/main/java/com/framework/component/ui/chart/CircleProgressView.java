package com.framework.component.ui.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.framework.R;

/**
 * @author Administrator 2016-08-18
 */
public class CircleProgressView extends View implements IPercentView {
    private static final float START_ANGLE = 162f;
    private static final float SWEEP_ANGLE = 216;
    private static final int DEFAULT_CIRCLE_LINE_STROKE_WIDTH = 10;
    private static final int MAX_PROGRESS = 100;

    private final int hintColor = Color.parseColor("#e9e9e9");
    private final int showColor = Color.parseColor("#00a0e8");


    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;
    private final Range range;
//    private final DrawTextUtil drawText;


    // outter set
    private final float ringStrokeWidth;// 环的宽度
    private final MyColor color;
    private final Progress progress;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mPaint = new Paint();
        range = new Range();
//        drawText = new DrawTextUtil();

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        ringStrokeWidth = types.getDimension(R.styleable.CircleProgressView_circle_ring_stroke_width, DEFAULT_CIRCLE_LINE_STROKE_WIDTH);

        color = new MyColor();
        color.hint = types.getColor(R.styleable.CircleProgressView_circle_hint_color, hintColor);
        color.show = types.getColor(R.styleable.CircleProgressView_circle_show_color, showColor);

        progress = new Progress();
        progress.max = types.getInt(R.styleable.CircleProgressView_circle_max_progress, MAX_PROGRESS);
        progress.curr = types.getInt(R.styleable.CircleProgressView_circle_curr_progress, 0);
        types.recycle();

        setSide();
    }

    private void setSide() {
        if (isInEditMode()) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                int width = getWidth();
                int height = getHeight();
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

        drawArc(canvas); // 绘制圆圈，进度条背景
//        drawText.invoke(canvas, mPaint, range, color, progress);
    }

    private void initRange() {
        int width = this.getWidth();
        int height = this.getHeight();

        range.min = Math.min(width, height);
        range.max = Math.max(width, height);
        range.width = width;
        range.height = height;
    }

    private void initRectF() {
        mRectF.left = ringStrokeWidth / 2 + (range.width - range.min) / 2; // 左上角x
        mRectF.top = ringStrokeWidth / 2 + (range.height - range.min) / 2; // 左上角y
        mRectF.right = range.width - mRectF.left; // 左下角x
        mRectF.bottom = range.height - mRectF.top; // 右下角y
    }

    private void initPaint(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(ringStrokeWidth);
    }

    private void drawArc(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setColor(color.hint);
        canvas.drawArc(mRectF, START_ANGLE, SWEEP_ANGLE, false, mPaint);

        mPaint.setColor(color.show);
        canvas.drawArc(mRectF, START_ANGLE, ((float) progress.curr / progress.max) * SWEEP_ANGLE, false, mPaint);
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
        return SLEEP_TIME * MAX_PROGRESS * progress / CircleProgressView.this.progress.max;
    }

    private void createAnimCountDownTimer(final int future) {
        animCountDownTimer = new CountDownTimer(future, SLEEP_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgressNotInUiThread((int) ((future - millisUntilFinished) / SLEEP_TIME));
            }

            @Override
            public void onFinish() {
                setProgressNotInUiThread(future / SLEEP_TIME);
            }
        };
    }


    private static class Range {
        int width, height, max, min;
    }

    private static class MyColor {
        int hint, show;
    }

    private static class Progress {
        int curr, max;
    }

    private static class DrawTextUtil {
        private static final String PERCENT_TEXT = "%";

        private Canvas canvas;
        private Paint paint;
        private Range range;
        private MyColor color;
        private String progress;

        // inner use
        private int progressHeight, percentHeight;
        private float progressX, progressY;

        public DrawTextUtil() {
        }

        public void invoke(Canvas canvas, Paint paint, Range range, MyColor color, Progress progress) {
            this.canvas = canvas;
            this.paint = paint;
            this.range = range;
            this.color = color;
            this.progress = progress.curr + "";

            draw(paint);
        }

        private void draw(Paint paint) {
            getProgressAndPercentHeight();
            paint.setColor(color.show);
            paint.setStyle(Paint.Style.FILL);
//            try {
//                paint.setTypeface(AppContext.getTypeface());
//            } catch (NullPointerException ignore) {
//            }
            // 测量进度值与‘%’的宽度，并计算进度值的基准线的x,y
            final int precentWidth = getPrecentWidth();
            final int progressWidth = getProgressTextWidth();
            getProgressTextPoint(precentWidth, progressWidth);

            drawProgressText();// 绘制进度文案
            drawPercentText(progressWidth);// 绘制%
        }

        private void getProgressAndPercentHeight() {
            progressHeight = range.min * 2 / 5;
            percentHeight = range.min / 5;
        }

        private int getPrecentWidth() {
            return getTextWidth(percentHeight, PERCENT_TEXT);
        }

        private int getProgressTextWidth() {
            return getTextWidth(progressHeight, progress);
        }

        private int getTextWidth(float height, String text) {
            paint.setTextSize(height);
            return (int) paint.measureText(text, 0, text.length());
        }

        private void getProgressTextPoint(int precentWidth, int progressWidth) {
            Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
            progressX = (range.width - progressWidth - precentWidth) / 2;
            progressY = (range.height + progressHeight) / 2 - fmi.bottom;
        }

        private void drawProgressText() {
            paint.setTextSize(progressHeight);
            canvas.drawText(progress, progressX, progressY, paint);
        }

        private void drawPercentText(int progressWidth) {
            paint.setTextSize(percentHeight);
            canvas.drawText(PERCENT_TEXT, progressX + progressWidth, progressY, paint);
        }
    }

}