package com.example.hotelcalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.example.hotelcalendar.R;

import java.util.Calendar;
import java.util.Date;


public class HotelCalendarView extends CalendarView {


    private int tempLeftPoint,tempTopPoint;



    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (enterHotelTime != null && outHotelTime != null) {
                logd("选择的入住日期：" + sdf.format(enterHotelTime) + " 离店日期：" + sdf.format(outHotelTime));
                showToast("选择的入住日期：" + sdf.format(enterHotelTime) + " 离店日期：" + sdf.format(outHotelTime));
                if(onDateSelectedListener!=null){
                    onDateSelectedListener.onDateSelected(enterHotelTime,outHotelTime);
                }
            }
        }
    };
    private Date enterHotelTime, outHotelTime;

    public HotelCalendarView(Context context) {
        super(context);
        init(context,null);
    }

    public HotelCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public HotelCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    private void init(Context context,AttributeSet attrs){
        enterHotelTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date tomorrow = calendar.getTime();
        outHotelTime = tomorrow;
        setEnterAndOutHotelTime(enterHotelTime,outHotelTime);
    }

    @Override
    protected void setTodayTitleAndSize(@NonNull View itemView, @NonNull TextView titleView, @NonNull TextView descView, DateBean dateBean) {
        super.setTodayTitleAndSize(itemView, titleView, descView, dateBean);
        titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_333333));

    }

    @Override
    public void dealOnDayClicked(View itemView,LinearLayout mLLDay, TextView mTvTitle, TextView mTvDesc, DateBean dateBean) {
        super.dealOnDayClicked(itemView,mLLDay, mTvTitle, mTvDesc, dateBean);
        Log.d(TAG, "dealOnDayClicked: 酒店日历处理每天的点击事件...");
        if (isLegalDate(dateBean)) {
            //不是过期的日期 包括今天之前的 和 30天之外的
            if (enterHotelTime == null) {
                enterHotelTime = dateBean.getTime();
            } else if (outHotelTime == null
                    && compareDate(dateBean.getTime(), enterHotelTime)) {
                if (compareLessThan30Day(dateBean.getTime(), enterHotelTime)) {
                    outHotelTime = dateBean.getTime();

                } else {
                    //离店日期范围超出了30天
                    showToast("入住周期不能大于30天");
                    return;
                }


            } else {
                enterHotelTime = dateBean.getTime();
                outHotelTime = null;


            }

            setEnterAndOutHotelTime(enterHotelTime, outHotelTime);

            if (mHandler.hasMessages(1)) {
                mHandler.removeMessages(1);
                Log.d(TAG, "dealOnDayClicked: 清理了消息....");
            }
            if (enterHotelTime != null && outHotelTime != null) {
                mHandler.sendEmptyMessageDelayed(1, 2000);
                Log.d(TAG, "dealOnDayClicked: 开始延迟消息...");
            }
        }
    }



    /**
     * 处理在入住 和 离店时间中间的item的背景
     * @param itemView
     * @param mTvTitle
     * @param mTvDesc
     * @param dateBean
     */
    @Override
    protected void dealInEnterAndOutTimeStyle(View itemView, LinearLayout mLLDay, TextView mTvTitle, TextView mTvDesc, DateBean dateBean) {
        itemView.setBackground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                int width = itemView.getWidth();
                int height = itemView.getHeight();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.color_manyday_selected));
                RectF rectF = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rectF, 0, 0, paint);
            }
        }));
    }

    /**
     * 处理酒店中选中入住 及 离店时间的选中效果
     *
     * @param itemView
     * @param mTvTitle
     * @param mTvDesc
     * @param dateBean
     * @param isEnter
     */
    @Override
    protected void dealEnterOrOutDayStyle(View itemView,LinearLayout mLLDay, TextView mTvTitle, TextView mTvDesc, DateBean dateBean, boolean isEnter) {

        itemView.setBackground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                int width = itemView.getWidth();
                int height = itemView.getHeight();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.color_manyday_selected));
                RectF rectF1 = new RectF(0,0,width,height);
                canvas.drawRoundRect(rectF1,height/2,height/2,paint);
                if(enterHotelTime==null || outHotelTime==null){

                }else{
                    RectF rectF;
                    if(isEnter){
                        rectF = new RectF(width/2, 0, width, height);
                    }else{
                        rectF = new RectF(0, 0, width-width/2, height);
                    }
                    canvas.drawRoundRect(rectF, 0, 0, paint);
                }

            }
        }));
        mLLDay.setBackground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                int width = mLLDay.getWidth();
                int height = mLLDay.getHeight();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.color_day_selected));
                RectF rectF = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            }
        }));


        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTvTitle.setTextColor(Color.WHITE);
        if(compareDateEquals(dateBean.getTime(),new Date())){
            mTvTitle.setText("今天");
        }
        mTvDesc.setVisibility(VISIBLE);
        mTvDesc.setTextColor(Color.WHITE);
        mTvDesc.setText(isEnter ? "入住" : "离店");

    }

    /**
     * 是不是合法的日期
     *
     * @param dateBean
     * @return
     */
    private boolean isLegalDate(DateBean dateBean) {
        if (dateBean != null) {
            if ("0".equals(dateBean.getInValid())) {
                return false;
            }
            if ("1".equals(dateBean.getIsTodayPrev())) {
                Toast.makeText(getContext(), "不能选择之前的日期", Toast.LENGTH_SHORT).show();
                return false;
            }
//            Date time = dateBean.getTime();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(time);
//
//            Calendar todayDelay30 = Calendar.getInstance();
//            todayDelay30.add(Calendar.DAY_OF_MONTH, 30);
//
//
//            if (calendar.compareTo(todayDelay30) > 0) {
//                showToast("选择的入住周期不能大于30天");
//                return false;
//            }

            return true;

        }
        return false;
    }



}
