package com.wj.w_click;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorLong;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jiwang on 2019/10/17.
 * 自定义时钟
 */

public class Click extends View {

    private float mWidth;
    private float mHeight;
    private float mHourR;
    private float mMinuteR;
    private float mSecondR;

    private float mHourDeg;
    private float mMinuteDeg;
    private float mSecondDeg;
    private Paint mHelperPaint = createPaint(Color.RED);
    private Paint mPaint = createPaint();
    private String[] NUMBER_TEXT_LIST = new String[]{"日", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

    private static final ValueAnimator mAnimator;

    static {
        mAnimator = ValueAnimator.ofFloat(6f, 0f);//由6降到1
        mAnimator.setDuration(150);
        mAnimator.setInterpolator(new LinearInterpolator()); //插值器设为线性
    }

    public Click(Context context) {
        super(context);
    }


    public Click(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Click(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    //在onLayout方法中计算View去除padding后的宽高
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = (float) (getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
        mHeight = (float) (getMeasuredHeight() - getPaddingTop() - getPaddingBottom());

        //统一用View宽度*系数来处理大小，这样可以联动适配样式
        mHourR = mWidth * 0.143f;
        mMinuteR = mWidth * 0.35f;
        mSecondR = mWidth * 0.35f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canvas == null) {
            return;
        }
        //设置黑色背景
        canvas.drawColor(Color.BLACK);
        canvas.save();

        canvas.translate(mWidth / 2, mHeight / 2);

        //绘制原件
        drawCenter(canvas);

        drawHour(canvas, mHourDeg);

        drawMinute(canvas, mMinuteDeg);

        drawSecond(canvas, mSecondDeg);

        canvas.drawLine(0, 0, mWidth, 0, mHelperPaint);

        canvas.restore();


    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        //绘制数字时间

        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        String minuteStr;
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = minute + "";
        }
        mPaint.setTextSize(mHourR * 0.4f);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(hour + ":" + minuteStr, 0f, getBottomedY(mPaint), mPaint);

        //绘制月份、星期
        int month = (calendar.get(Calendar.MONTH) + 1);
        String monthStr;
        if (month < 10) {
            monthStr = (0 + month) + "";
        } else {
            monthStr = month + "";
        }


        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        mPaint.setTextSize(mHourR * 0.16f);
        mPaint.setAlpha(255);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(monthStr + "." + day + " 星期" + toText((dayOfWeek - 1) + ""), 0f, getToppedY(mPaint), mPaint);

    }

    /**
     * 画小时
     *
     * @param canvas
     * @param degrees
     */
    private void drawHour(Canvas canvas, float degrees) {


        mPaint.setTextSize(mHourR * 0.18f);
        //处理整体选装
        canvas.save();
        canvas.rotate(degrees);

        for (int i = 0; i < 12; i++) {
            canvas.save();
            //每30度绘制一次  360/12=30度 画一圈
            float idegress = 360 / 12f * i;
            canvas.rotate(idegress);
            //设置画笔
            if ((idegress + degrees) == 0) {
                mPaint.setAlpha(255);
            } else {
                mPaint.setAlpha((int) (0.6f * 255));
            }
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(toText((i + 1) + "") + "点", mHourR, getCenteredY(mPaint), mPaint);
            canvas.restore();
        }

        canvas.restore();
    }


    /**
     * 画分钟
     *
     * @param canvas
     * @param degrees
     */
    private void drawMinute(Canvas canvas, float degrees) {

        mPaint.setTextSize(mHourR * 0.18f);
        //旋转
        canvas.save();

        canvas.rotate(degrees);

        for (int i = 0; i < 60; i++) {

            canvas.save();

            float idegress = 360 / 60f * i;

            canvas.rotate(idegress);
            if (degrees + idegress == 0) {
                mPaint.setAlpha(255);
            } else {
                mPaint.setAlpha((int) (0.6f * 255));
            }
            mPaint.setTextAlign(Paint.Align.RIGHT);

            if (i < 59) {
                canvas.drawText(toText((i + 1) + "") + "分", mMinuteR, getCenteredY(mPaint), mPaint);
            }
            canvas.restore();

        }
        canvas.restore();

    }

    /**
     * 绘制秒
     *
     * @param canvas
     * @param degress
     */

    private void drawSecond(Canvas canvas, float degress) {
        //设置字体大小
        mPaint.setTextSize(mHourR * 0.18f);
        mPaint.setStrokeWidth(2);
        //设置整体旋转
        canvas.save();
        canvas.rotate(degress);

        for (int i = 0; i < 60; i++) {
            canvas.save();
            //每次选装角度
            int idegress = (int) (360 / 60f * i);

            canvas.rotate(idegress);

            if (idegress + degress == 0) {
                mPaint.setAlpha(255);
            } else {
                mPaint.setAlpha((int) (255 * 0.6f));
            }
            mPaint.setTextAlign(Paint.Align.LEFT);

            if (i < 59) {

                canvas.drawText(toText((i + 1) + "") + "秒", mSecondR, getCenteredY(mPaint), mPaint);
            }
            canvas.restore();
        }
        canvas.restore();
    }

    /**
     * 时钟转动方法
     */
    public void doInvalidate() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.HOUR);
        int hour = calendar.get(calendar.HOUR);
        final int minute = calendar.get(calendar.MINUTE);
        final int second = calendar.get(calendar.SECOND);
        //这里将三个角度与实际时间关联起来，当前几点几分几秒，就把相应的圈逆时针旋转多少
        mHourDeg = -360 / 12 * (hour - 1);
        mMinuteDeg = -360 / 60f * (minute - 1);
        mSecondDeg = -360 / 60f * (second - 1);

        //记录当前角度，然后让秒圈线性的旋转6°
        final float hd = mHourDeg;
        final float md = mMinuteDeg;
        final float sd = mSecondDeg;
        //处理动画
        mAnimator.removeAllUpdateListeners();//需要移除先前的监听

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float av = (Float) animation.getAnimatedValue();
                if (minute == 0 && second == 0) {
                    mHourDeg = hd + av * 5;//时圈旋转角度是分秒的5倍，线性的旋转30°
                }

                if (second == 0) {
                    mMinuteDeg = md + av;//线性的旋转6°
                }

                mSecondDeg = sd + av;//线性的旋转6°

                invalidate();


            }
        });

        mAnimator.start();
    }




    private Paint createPaint() {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        return paint;
    }

    private Paint createPaint(int color) {

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        return paint;
    }

    /**
     * 扩展获取绘制文字时在x轴上 垂直居中的y坐标
     */
    private Float getCenteredY(Paint paint) {
        return paint.getFontSpacing() / 2 - paint.getFontMetrics().bottom;
    }

    /**
     * 扩展获取绘制文字时在x轴上 贴紧x轴的上边缘的y坐标
     */
    private Float getBottomedY(Paint paint) {
        return -paint.getFontMetrics().bottom;
    }

    /**
     * 扩展获取绘制文字时在x轴上 贴近x轴的下边缘的y坐标
     */
    private Float getToppedY(Paint paint) {
        return -paint.getFontMetrics().ascent;
    }

    /**
     * 数字转换文字
     */
    private String toText(String number) {
        String result = "";
        char[] chars = number.toCharArray();
        //处理 10，11，12.. 20，21，22.. 等情况
        if (chars.length > 1) {
            if (chars[0] != 1) {
                result += NUMBER_TEXT_LIST[chars[0]-'0'];
            }
            if (result.equals("一")){
                result="";
            }
            result += "十";
            if (chars[1] > 0) {
                result += NUMBER_TEXT_LIST[chars[1]-'0'];
            }
        } else {
            result = NUMBER_TEXT_LIST[chars[0]-'0'];
        }

        return result;
    }


    public void stopInvalidate() {
        mAnimator.removeAllUpdateListeners();
    }
}
