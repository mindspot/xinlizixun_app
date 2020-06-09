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
 * @author Administrator 2016-09-18
 */
public class RoundProgressView extends View {
    private static final int START_ANGLE = 270;
    private static final int SWEEP_ANGLE = 360;
    private static final int DEFAULT_CIRCLE_LINE_STROKE_WIDTH = 20;
    private static final int MAX_PROGRESS = 100;


    private final int hintColor = Color.parseColor("#e9e9e9");
    private final int showColor = Color.parseColor("#00a0e8");
    private final int textColor = Color.parseColor("#888888");

    // 画圆所在的距形区域
    private final RectF mRectF;

    private final Paint mPaint;
    private final Range range;
    private final DrawTextUtil drawText;


    // outter set
    private final float ringStrokeWidth;// 环的宽度
    private final MyColor color;
    private final Progress progress;
    private final boolean hasEndPointer;
    private String centerText;

    public RoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mPaint = new Paint();
        range = new Range();
        drawText = new DrawTextUtil();

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);
        ringStrokeWidth = types.getDimension(R.styleable.RoundProgressView_round_ring_stroke_width, DEFAULT_CIRCLE_LINE_STROKE_WIDTH);

        color = new MyColor();
        color.hint = types.getColor(R.styleable.RoundProgressView_round_hint_color, hintColor);
        color.show = types.getColor(R.styleable.RoundProgressView_round_show_color, showColor);
        color.text = types.getColor(R.styleable.RoundProgressView_round_text_color, textColor);

        progress = new Progress();
        progress.max = types.getInt(R.styleable.RoundProgressView_round_max_progress, MAX_PROGRESS);
        progress.curr = types.getInt(R.styleable.RoundProgressView_round_curr_progress, 0);
        hasEndPointer = types.getBoolean(R.styleable.RoundProgressView_round_has_end_pointer, false);
        centerText = types.getText(R.styleable.RoundProgressView_round_center_text).toString();

        types.recycle();
        setSide();
    }

    private void setSide() {
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
//        drawPointer(canvas);// 绘制小圆圈 -->tip：以后可以恢复的
//        if (hasEndPointer) {
//            drawCircle.invoke(canvas, mPaint, ringStrokeWidth, range, color, progress);
//        }
        drawText.invoke(canvas, mPaint, range, color, centerText);
    }

    private void initRange() {
        int padding = (int) ringStrokeWidth / 2;
        int width = this.getWidth() - padding;
        int height = this.getHeight() - padding;

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
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawArc(Canvas canvas) {
        mPaint.setColor(color.hint);
        canvas.drawArc(mRectF, START_ANGLE, SWEEP_ANGLE, false, mPaint);

        mPaint.setColor(color.show);
        float angle = ((float) progress.curr / progress.max) * SWEEP_ANGLE;
        canvas.drawArc(mRectF, START_ANGLE - angle, angle, false, mPaint);
    }

    private void drawPointer(Canvas canvas) {
        int centerX = range.width / 2;
        int centerY = range.height / 2;
        float circleRadius = ringStrokeWidth / 2;
        float ringRadius = range.min / 2 - circleRadius;

        mPaint.setStyle(Paint.Style.FILL);

        float angle = ((float) progress.curr / progress.max) * SWEEP_ANGLE;
        double radians = Math.toRadians(START_ANGLE - angle);
        float cx = (float) (centerX + ringRadius * Math.cos(radians));
        float cy = (float) (centerY + ringRadius * Math.sin(radians));

        float bigCircleRadius = ringStrokeWidth;
        float smallCircleRadius = bigCircleRadius / 2;

        mPaint.setColor(color.show);
        canvas.drawCircle(cx, cy, bigCircleRadius, mPaint);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, smallCircleRadius, mPaint);
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
        return SLEEP_TIME * MAX_PROGRESS * progress / RoundProgressView.this.progress.max;
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
        int hint, show, text;
    }

    private static class Progress {
        int curr, max;
    }

    private static class DrawTextUtil {
        private Canvas canvas;
        private Paint paint;
        private Range range;
        private MyColor color;
        private String text;

        // inner use
        private int textHeight, textWidth;
        private float textX, textY;

        public DrawTextUtil() {
        }

        public void invoke(Canvas canvas, Paint paint, Range range, MyColor color, String text) {
            this.canvas = canvas;
            this.paint = paint;
            this.range = range;
            this.color = color;
            this.text = text;

            draw(paint);
        }

        private void draw(Paint paint) {
            getTextHeightAndWidth();
            paint.setColor(color.text);
            paint.setStyle(Paint.Style.FILL);
//            try {
//                paint.setTypeface(AppContext.getTypeface());
//            } catch (NullPointerException ignore) {
//            }
            // 计算进度值的基准线的x,y
            getTextPoint();
            drawProgressText();// 绘制进度文案
        }

        private void getTextHeightAndWidth() {
            textHeight = range.min * 2 / 5;
            textWidth = getTextWidth(textHeight, text);
        }

        private int getTextWidth(float height, String text) {
            paint.setTextSize(height);
            return (int) paint.measureText(text, 0, text.length());
        }

        private void getTextPoint() {
            Paint.FontMetricsInt fmi = paint.getFontMetricsInt();
            textX = (range.width - textWidth) / 2;
            textY = (range.height + textHeight) / 2 - fmi.bottom;
        }

        private void drawProgressText() {
            paint.setTextSize(textHeight);
            canvas.drawText(text, textX, textY, paint);
        }

    }

    public void setText(int textResId) {
        setText(getResources().getString(textResId));
    }

    public void setText(String text) {
        centerText = text;
        invalidate();
    }
}