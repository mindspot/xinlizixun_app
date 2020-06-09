package com.framework.component.ui.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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
public class SemiCirclePercentView extends View implements IPercentView {
    private static final int START_ANGLE = 180;//起始角度
    private static final int SWEEP_ANGLE = 180;//进度条划过角度
    private static final int DEFAULT_CIRCLE_LINE_STROKE_WIDTH = 10;//进度条默认宽度
    private static final int DEFAULT_CIRCLE_OUTER_STROKE_WIDTH = 40;//外环默认宽度
    private static final int DEFAULT_CIRCLE_INNER_CENTER_STROKE_WIDTH = 6;//内环默认宽度
    private static final int DEFAULT_CIRCLE_INNER_CENTER_INTERVAL_STROKE_WIDTH = 20;//进度条与内环之间的间隔
    private static final int MAX_PROGRESS = 100;//最大进度

    private final int hintColor = Color.parseColor("#99f2e1e1");//进度条底色默认颜色
    private final int showColor = Color.parseColor("#faf5f5");//进度条默认颜色
    private final int outerColor = Color.parseColor("#33f3b8ac");//外环默认颜色
    private final int interColor = Color.parseColor("#4Dffffff");//内环默认颜色

    // 画圆所在的距形区域
    private final RectF mRectOutter;//外环区域
    private final RectF mRectProgress;//进度条区域

    private final Paint mPaint;//画笔
    private final Range range;//尺寸相关
    /**
     * 内容中心的坐标
     */
    private Point centerPoint;

    // outter set
    private final float ringStrokeWidth;// 环的宽度
    private final float ringOuterStrokeWidth;// 外环的宽度
    private final float ringInnerStrokeWidth;// 外环的宽度
    private final float ringInnerStrokeIntervalWidth;// 进度条于内环之间的间隔
    private final MyColor color;//颜色相关
    private final Progress progress;//进度相关

    private Bitmap bitmap;//图片
    private float progressRadius;//进度条半径


    private float innerRadius;//进度条半径

    public SemiCirclePercentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectProgress = new RectF();
        mRectOutter = new RectF();
        centerPoint = new Point();
        mPaint = new Paint();
        range = new Range();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.progress_circle);
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.SemiCirclePercentView);
        ringStrokeWidth = types.getDimension(R.styleable.SemiCirclePercentView_circle_semi_ring_stroke_width, DEFAULT_CIRCLE_LINE_STROKE_WIDTH);
        ringOuterStrokeWidth = types.getDimension(R.styleable.SemiCirclePercentView_circle_semi_curr_outer_width, DEFAULT_CIRCLE_OUTER_STROKE_WIDTH);
        ringInnerStrokeWidth = types.getDimension(R.styleable.SemiCirclePercentView_circle_semi_curr_inner_width, DEFAULT_CIRCLE_INNER_CENTER_STROKE_WIDTH);
        ringInnerStrokeIntervalWidth = types.getDimension(R.styleable.SemiCirclePercentView_circle_semi_curr_inner_interval_width, DEFAULT_CIRCLE_INNER_CENTER_INTERVAL_STROKE_WIDTH);
        color = new MyColor();
        color.hint = types.getColor(R.styleable.SemiCirclePercentView_circle_semi_hint_color, hintColor);
        color.show = types.getColor(R.styleable.SemiCirclePercentView_circle_semi_show_color, showColor);
        color.outer = types.getColor(R.styleable.SemiCirclePercentView_circle_semi_curr_outer_color, outerColor);
        color.inner = types.getColor(R.styleable.SemiCirclePercentView_circle_semi_curr_inner_circle_color, interColor);

        progress = new Progress();
        progress.max = types.getInt(R.styleable.SemiCirclePercentView_circle_semi_max_progress, MAX_PROGRESS);
        progress.curr = types.getInt(R.styleable.SemiCirclePercentView_circle_semi_curr_progress, 0);
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

        mPaint.setStyle(Paint.Style.STROKE);
        initStaticView(canvas);
        drawArc(canvas); // 绘制圆圈，进度条背景
    }

    private void initRange() {
        int width = this.getWidth();
        int height = this.getHeight();
        range.min = Math.min(width, height);
        range.max = Math.max(width, height);
        range.width = width;
        range.height = height;
        centerPoint.set(range.width / 2, range.height);
        getMinWidth();
    }

    public void getMinWidth() {
        while (range.min * 2 > range.width) {
            range.min -= 10;
        }
        range.min -= getPaddingLeft();
    }

    private void initRectF() {
        mRectOutter.left = centerPoint.x - range.min + ringOuterStrokeWidth / 2; // 左上角x
        mRectOutter.top = centerPoint.y - range.min + ringOuterStrokeWidth / 2; // 左上角y
        mRectOutter.right = centerPoint.x + range.min - ringOuterStrokeWidth / 2; // 左下角x
        mRectOutter.bottom = centerPoint.y + range.min - ringOuterStrokeWidth / 2; // 右下角y

        mRectProgress.left = centerPoint.x - range.min + ringOuterStrokeWidth + ringStrokeWidth / 2; // 左上角x
        mRectProgress.top = centerPoint.y - range.min + ringOuterStrokeWidth + ringStrokeWidth / 2; // 左上角y
        mRectProgress.right = centerPoint.x + range.min - ringOuterStrokeWidth - ringStrokeWidth / 2; // 左下角x
        mRectProgress.bottom = centerPoint.y + range.min - ringOuterStrokeWidth - ringStrokeWidth / 2; // 右下角y

        progressRadius = range.min - ringOuterStrokeWidth - ringStrokeWidth / 2;
        innerRadius = range.min - ringOuterStrokeWidth - ringStrokeWidth - ringInnerStrokeIntervalWidth - ringInnerStrokeWidth;
    }

    /**
     * 初始化画笔
     *
     * @param canvas
     */
    private void initPaint(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);
    }

    /**
     * 绘制静态部分
     */
    public void initStaticView(Canvas canvas) {
        mPaint.setColor(color.outer);
        mPaint.setStrokeWidth(ringOuterStrokeWidth);
        canvas.drawArc(mRectOutter, START_ANGLE, SWEEP_ANGLE, false, mPaint);

        mPaint.setColor(color.inner);
        mPaint.setStrokeWidth(ringInnerStrokeWidth);
        for (int i = 3; i <= 180; i += 6) {
            Point point = getPointFromAngleAndRadius(START_ANGLE + i, innerRadius);
            canvas.drawCircle(point.x, point.y, ringInnerStrokeWidth / 2, mPaint);
        }
    }

    /**
     * 绘制进度条
     */
    private void drawArc(Canvas canvas) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(ringStrokeWidth);
        mPaint.setColor(color.hint);
        canvas.drawArc(mRectProgress, START_ANGLE + 1, SWEEP_ANGLE - 2, false, mPaint);

        mPaint.setColor(color.show);
        float pro = ((float) progress.curr / progress.max) * (SWEEP_ANGLE - 2);
        canvas.drawArc(mRectProgress, START_ANGLE + 1, pro, false, mPaint);
        drawArcRoune(progressRadius, START_ANGLE + pro + 1, canvas);
    }


    /**
     * 绘制进度条部分的圆
     *
     * @param radius 圆弧的半径
     * @param angle  所处于圆弧的多少度的位置
     */
    private void drawArcRoune(float radius, float angle, Canvas canvas) {
        Point point = getPointFromAngleAndRadius(angle, radius);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStyle(Paint.Style.FILL);
        int height = bitmap.getHeight() / 2;
        int width = bitmap.getWidth() / 2;
        canvas.drawBitmap(bitmap, point.x - width, point.y - height, mPaint);
    }

    /**
     * 根据角度和半径，求一个点的坐标
     *
     * @param angle
     * @param radius
     * @return
     */
    private Point getPointFromAngleAndRadius(float angle, float radius) {
        double x = radius * Math.cos(angle * Math.PI / 180) + centerPoint.x;
        double y = radius * Math.sin(angle * Math.PI / 180) + centerPoint.y;
        return new Point((int) x, (int) y);
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

    private static final int SLEEP_TIME = 15;// 一帧的时间
    private static final int TIME = 1500;
    private CountDownTimer animCountDownTimer;
    private static int TIME_PROGRESS;//一帧的进度

    @Override
    public void setProgressWithAnim(int progress) {
        TIME_PROGRESS = SemiCirclePercentView.this.progress.max / (TIME / SLEEP_TIME);
        canclePreviousCountDownTimerIfNeed();
        createAnimCountDownTimer();
        animCountDownTimer.start();
    }

    private void canclePreviousCountDownTimerIfNeed() {
        if (animCountDownTimer != null) {
            animCountDownTimer.cancel();
        }
    }

    private void createAnimCountDownTimer() {
        animCountDownTimer = new CountDownTimer(TIME, SLEEP_TIME) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgressNotInUiThread((int) ((TIME - millisUntilFinished) / SLEEP_TIME * TIME_PROGRESS));
            }

            @Override
            public void onFinish() {
                setProgressNotInUiThread(SemiCirclePercentView.this.progress.max);
            }
        };
    }


    private static class Range {
        int width, height, max, min;
    }

    private static class MyColor {
        int hint, show, outer, inner;
    }

    private static class Progress {
        int curr, max;
    }

}