package com.example.myviewlist.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myviewlist.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarView extends LinearLayout {
    private static final String TAG = "CalendarView";

    private View mView;
    private OnDateSelectedListener onDateSelectedListener;
    private RecyclerView mRvMonths;
    private MyMonthAdapter mMonthAdapter;
    private Calendar mCalendar;
    //最多展示的月数
    private int monthMaxCount = 6;
    private Date mNowDate;
    private int mStartMonth;


    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public CalendarView(Context context) {
        super(context);
        init(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.calendar_view, this, false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        monthMaxCount = typedArray.getInt(R.styleable.CalendarView_maxMonthCount, 6);
        if (monthMaxCount > 36) {
            monthMaxCount = 36;
        }
        mRvMonths = mView.findViewById(R.id.rvMonths);
        mMonthAdapter = new MyMonthAdapter(context);
        mRvMonths.setAdapter(mMonthAdapter);
        mRvMonths.setLayoutManager(new LinearLayoutManager(context));


        //添加视图
        addView(mView);
        //获取自定义的参数
        typedArray.recycle();

        //加载天的数据
        loadDayData();
    }

    /**
     * 加载天的数据
     */
    private void loadDayData() {
        mCalendar = Calendar.getInstance();
        //当前月份
        mStartMonth = mCalendar.get(Calendar.MONTH);
        //当前时间
        mNowDate = mCalendar.getTime();

        List<MonthDataBean> calendarData = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy年MM月");

        for (int i = mStartMonth; i < mStartMonth + monthMaxCount; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, i);
            Date curTime = calendar.getTime();

            //当前的月份标题
            String curMonthAndYear = df.format(curTime);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1));
            //每个月内的天数
            List<DateBean> cellData = new ArrayList<>();
            //当月的最大天数
            int maxDays = getCurrentMonthMaxDays(i);
            maxDays = maxDays + dayOfWeek;

            while (cellData.size() < maxDays) {
                //判断是否为当前月的时间
                DateBean dateBean = new DateBean();
                Date currentDate = calendar.getTime();

                if (calendar.get(Calendar.MONTH) == i) {
                    dateBean.setInValid("1");
                } else {
                    dateBean.setInValid("0");

                }

                //判断是否为今天
                if (calendar.get(Calendar.YEAR) ==mCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == mCalendar.get(Calendar.MONTH) && calendar.get(Calendar.DATE) == mCalendar.get(Calendar.DATE)) {
                    Log.d(TAG, "loadDayData: 设置了今天的标识...");
                    dateBean.setIsToday("1");
                }else{
                    dateBean.setIsToday("0");
                }

                dateBean.setTime(currentDate);

                cellData.add(dateBean);


                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            MonthDataBean monthDataBean = new MonthDataBean(curMonthAndYear, cellData);
            calendarData.add(monthDataBean);


        }


        if (calendarData != null && calendarData.size() > 0) {

            mMonthAdapter.setData(calendarData);
        }


    }

    /**
     * 获取某个月的最大天数
     *
     * @param tempCurrentMonth
     * @return
     */
    private int getCurrentMonthMaxDays(int tempCurrentMonth) {
        Log.d(TAG, "getCurrentMonthMaxDays: tempCurrentMonth = " + tempCurrentMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, tempCurrentMonth);
        int maximum = calendar.getActualMaximum(Calendar.DATE);
        Log.d(TAG, "getCurrentMonthMaxDays: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()) + "月份的最大天数 = " + maximum);
        return maximum;
    }


    /**
     * 月份展示的适配器
     */
    class MyMonthAdapter extends RecyclerView.Adapter<MyMonthAdapter.DayAdapterViewHolder> {

        private Context mContext;
        private List<MonthDataBean> mData;

        public MyMonthAdapter(Context context) {
            super();
            mContext = context;
        }

        public void setData(List<MonthDataBean> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        class DayAdapterViewHolder extends RecyclerView.ViewHolder {
            TextView mTvTitle;
            RecyclerView mRvDays;

            public DayAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvTitle = itemView.findViewById(R.id.tvTitle);
                mRvDays = itemView.findViewById(R.id.rv_days);
            }
        }

        @NonNull
        @Override
        public DayAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_month_itemview, parent, false);
            DayAdapterViewHolder viewHolder = new DayAdapterViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DayAdapterViewHolder holder, int position) {

            MonthDataBean monthDataBean = mData.get(position);
            String title = monthDataBean.title;

            holder.mTvTitle.setText(title);

            holder.mRvDays.setLayoutManager(new GridLayoutManager(mContext, 7));
            MyDayAdapter dayAdapter = new MyDayAdapter(mContext);
            holder.mRvDays.setAdapter(dayAdapter);
            List<DateBean> days = monthDataBean.days;
            if (days != null) {
                dayAdapter.setData(days);
            }

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }


    class MyDayAdapter extends RecyclerView.Adapter<MyDayAdapter.DayAdapterViewHolder> {

        private Context mContext;
        private List<DateBean> mData;

        public MyDayAdapter(Context context) {
            super();
            mContext = context;

        }


        public void setData(List<DateBean> data) {
            if (data == null) return;
            this.mData = data;
            notifyDataSetChanged();
        }

        class DayAdapterViewHolder extends RecyclerView.ViewHolder {
            TextView mTvTitle,mTvDesc;

            public DayAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvTitle = itemView.findViewById(R.id.tvTitle);
                mTvDesc = itemView.findViewById(R.id.tvDesc);

            }
        }

        @NonNull
        @Override
        public DayAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_day_itemview, parent, false);
            DayAdapterViewHolder viewHolder = new DayAdapterViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DayAdapterViewHolder holder, int position) {
            DateBean dateBean = mData.get(position);
            if ("1".equals(dateBean.getInValid())) {
                holder.mTvTitle.setText(String.valueOf(dateBean.getTime().getDate()));
            } else {
                holder.mTvTitle.setText("");
            }
            if("1".equals(dateBean.getIsToday())){
                holder.mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                holder.mTvDesc.setText("今天");
                holder.mTvDesc.setVisibility(VISIBLE);
            }else{
                holder.mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                holder.mTvDesc.setVisibility(GONE);
            }


        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    public interface OnDateSelectedListener {
        //入住时间 和离店时间的监听
        void onDateSelected(Date inDate, Date outDate);
    }

    private class MonthDataBean {
        private final String title;
        private final List<DateBean> days;


        public MonthDataBean(String title, List<DateBean> days) {
            this.title = title;
            this.days = days;
        }
    }

    private class DateBean {
        private String inValid;
        private String preOrNext;
        private Date time;
        private String isToday;

        public void setIsToday(String isToday) {
            this.isToday = isToday;
        }

        public String getIsToday() {
            return isToday;
        }

        public void setInValid(String inValid) {
            this.inValid = inValid;
        }

        public String getPreOrNext() {
            return preOrNext;
        }

        public void setPreOrNext(String preOrNext) {
            this.preOrNext = preOrNext;
        }

        public String getInValid() {
            return inValid;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

}
