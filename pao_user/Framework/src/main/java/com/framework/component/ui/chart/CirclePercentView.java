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
 * 圆环百分比进度视图
 *
 * @author Administrator 2017-04-24
 */
public class CirclePercentView extends View implements IPercentView {
    private static final int START_ANGLE = 268;
    private static final int SWEEP_ANGLE = 360;
    private static final int DEFAULT_CIRCLE_LINE_STROKE_WIDTH = 20;
    private static final int MAX_PROGRESS = 100;

    private final int hintColor = Color.parseColor("#00a0e8");
    private final int showColor = Color.parseColor("#e9e9e9");

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;
    private final Range range;
    private final DrawTextUtil drawText;


    // outter set
    private final float ringStrokeWidth;// 环的宽度
    private final MyColor color;
    private final Progress progress;

    public CirclePercentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mPaint = new Paint();
        range = new Range();
        drawText = new DrawTextUtil();

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView);
        ringStrokeWidth = types.getDimension(R.styleable.CirclePercentView_circle_percent_ring_stroke_width, DEFAULT_CIRCLE_LINE_STROKE_WIDTH);

        color = new MyColor();
        color.hint = types.getColor(R.styleable.CirclePercentView_circle_percent_hint_color, hintColor);
        color.show = types.getColor(R.styleable.CirclePercentView_circle_percent_show_color, showColor);

        progress = new Progress();
        progress.max = types.getInt(R.styleable.CirclePercentView_circle_percent_max_progress, MAX_PROGRESS);
        progress.curr = types.getInt(R.styleable.CirclePercentView_circle_percent_curr_progress, 0);
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
        drawText.invoke(canvas, mPaint, range, color, progress, ringStrokeWidth);
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
//        mPaint.setStrokeCap(Paint.Cap.ROUND);// 圆环的线是否为圆角

        mPaint.setColor(color.hint);
        canvas.drawArc(mRectF, START_ANGLE, SWEEP_ANGLE, false, mPaint);

        mPaint.setColor(color.show);
        canvas.drawArc(mRectF, START_ANGLE, ((float) (progress.max - progress.curr) / progress.max) * SWEEP_ANGLE + 2, false, mPaint);
    }

    public void setMaxProgress(int maxProgress) {
        this.progress.max = maxProgress;
    }

    public void setCurrProgress(int currProgress) {
        progress.curr = currProgress;
        this.invalidate();
    }

    @Override
    public void setProgressNotInUiThread(int currProgress) {
        progress.curr = currProgress;
        this.postInvalidate();
    }

    private static final int SLEEP_TIME = 16;// 一帧的时间
    private CountDownTimer animCountDownTimer;

    @Override
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
        return SLEEP_TIME * MAX_PROGRESS * progress / CirclePercentView.this.progress.max;
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
        private Progress progress;
        private String precentText;

        // inner use
        private float percentTextHeight;// 文本的高度
        private float percentTextX, percentTextY;// 文本基准线的x,y

        public DrawTextUtil() {
        }

        public void invoke(Canvas canvas, Paint paint, Range range, MyColor color, Progress progress, float ringStrokeWidth) {
            this.canvas = canvas;
            this.paint = paint;
            this.range = range;
            this.color = color;
            this.progress = progress;
            this.precentText = progress.curr * 100 / progress.max + PERCENT_TEXT;
            percentTextHeight = ringStrokeWidth * 3;

            draw(paint);
        }

        private void draw(Paint paint) {
            paint.setColor(color.show);
            paint.setStyle(Paint.Style.FILL);
            // 测量百分比文本的宽度，与文本基准线的x,y
            getProgressTextPoint();

            drawProgressText();// 绘制进度文案
        }

        private void getProgressTextPoint() {
            paint.setTextSize(percentTextHeight);
            int precentTextWidth = (int) paint.measureText(precentText, 0, precentText.length());

            int centerPointX = range.width / 2;
            int centerPointY = range.height / 2;
            float radius = range.min / 2 - precentTextWidth;

            float angle = ((float) (progress.max - progress.curr) / progress.max) * SWEEP_ANGLE + 2;
            double radians = Math.toRadians(START_ANGLE + angle);
            float cx = (float) (centerPointX + radius * Math.cos(radians));
            float cy = (float) (centerPointY + radius * Math.sin(radians));


            Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
            percentTextX = cx - precentTextWidth / 2;
            percentTextY = cy + percentTextHeight / 2 - fmi.bottom;
        }

        private void drawProgressText() {
            paint.setTextSize(percentTextHeight);
            canvas.drawText(precentText, percentTextX, percentTextY, paint);
        }

    }

}