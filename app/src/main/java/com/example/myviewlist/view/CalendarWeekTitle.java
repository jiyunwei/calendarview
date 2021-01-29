package com.example.myviewlist.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myviewlist.R;

public class CalendarWeekTitle extends LinearLayout {

    private int mItemTextSize;
    private static final String[] weekData1 = {"日", "一", "二", "三", "四", "五", "六"};
    private static final String[] weekData2 = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static final String[] weekData3 = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public CalendarWeekTitle(Context context) {
        super(context);
        init(context, null);
    }

    public CalendarWeekTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CalendarWeekTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarWeekTitle);
        mItemTextSize = typedArray.getInt(R.styleable.CalendarWeekTitle_weekTextSize, 1);
        int itemTextColor = typedArray.getColor(R.styleable.CalendarWeekTitle_weekTextColor, Color.BLACK);
        boolean isBoldText = typedArray.getBoolean(R.styleable.CalendarWeekTitle_weekTextIsBold,false);
        String[] weekData = new String[]{};
        switch (mItemTextSize) {
            case 1:
                weekData = weekData1;
                break;
            case 2:
                weekData = weekData2;
                break;
            case 3:
                weekData = weekData3;
                break;
            default:
                weekData = weekData1;
        }

        LinearLayout.LayoutParams lp;
        for (int i = 0; i < weekData.length; i++) {
            TextView tvTitle = new TextView(context);
            lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            tvTitle.setLayoutParams(lp);
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setTextColor(itemTextColor);
            TextPaint titlePaint = tvTitle.getPaint();
            titlePaint.setFakeBoldText(isBoldText);
            tvTitle.setText(weekData[i]);
            addView(tvTitle);
        }

        typedArray.recycle();
    }
}
