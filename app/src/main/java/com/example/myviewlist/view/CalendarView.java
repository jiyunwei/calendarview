package com.example.myviewlist.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class CalendarView extends LinearLayout implements IBaseCalendar {
    protected static final String TAG = "CalendarView";

    private View mView;
    private OnDateSelectedListener onDateSelectedListener;
    private RecyclerView mRvMonths;
    private MyMonthAdapter mMonthAdapter;
    private Calendar mCalendar;
    //最多展示的月数
    private int monthMaxCount = 6;
    private Date mNowDate;
    private int mStartMonth;
    protected DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date enter; //入住酒店的时间
    private Date out; //离开酒店的时间

    protected void setEnterAndOutHotelTime(Date enter, Date out) {
        this.enter = enter;
        this.out = out;

        if (enter == null && out == null) {

        } else {
            mMonthAdapter.notifyDataSetChanged();
            logd("刷新月份适配器...");
        }

    }


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
                if (calendar.get(Calendar.YEAR) == mCalendar.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == mCalendar.get(Calendar.MONTH) && calendar.get(Calendar.DATE) == mCalendar.get(Calendar.DATE)) {
                    Log.d(TAG, "loadDayData: 设置了今天的标识...");
                    dateBean.setIsToday("1");
                } else {
                    dateBean.setIsToday("0");
                }

                //判断是否为今天之前的天数
                if (calendar.before(mCalendar)) {
                    dateBean.setIsTodayPrev("1");
                } else {
                    dateBean.setIsTodayPrev("0");
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
            TextView mTvTitle, mTvDesc;
            LinearLayout mLLDay;

            public DayAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                mTvTitle = itemView.findViewById(R.id.tvTitle);
                mTvDesc = itemView.findViewById(R.id.tvDesc);
                mLLDay = itemView.findViewById(R.id.ll_day);

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

            //是否为今天前面的数据
            if ("1".equals(dateBean.getIsTodayPrev())) {
                holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
            } else {
                holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            }

            //是否为今天
            if ("1".equals(dateBean.getIsToday())) {
                setTodayTitleAndSize(holder.itemView, holder.mTvTitle, holder.mTvDesc, dateBean);
                holder.mTvDesc.setVisibility(VISIBLE);
            } else {
                holder.mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                holder.mTvDesc.setVisibility(GONE);
            }

            if (enter != null) {
                if ("1".equals(dateBean.getInValid())
                        && "0".equals(dateBean.getIsTodayPrev())
                        && compareDateEquals(enter, dateBean.getTime())) {
                    logd("入住时间..." + sdf.format(enter));
                    dealEnterOrOutDayStyle(holder.itemView, holder.mLLDay, holder.mTvTitle, holder.mTvDesc, dateBean, true);
                }

                if (out != null) {
                    if ("1".equals(dateBean.getInValid())
                            && "0".equals(dateBean.getIsTodayPrev())
                            && compareDate(out, enter)
                            && compareDateEquals(out, dateBean.getTime())) {
                        logd("离店时间..." + sdf.format(out));
                        dealEnterOrOutDayStyle(holder.itemView, holder.mLLDay, holder.mTvTitle, holder.mTvDesc, dateBean, false);
                    }


                    //处理入住 与 离店日期中间天的背景
                    if (compareGreaterThan(dateBean.getTime(), enter)
                            && compareLessThan(dateBean.getTime(), out)
                            && !compareDateEquals(dateBean.getTime(), out)
                            && "1".equals(dateBean.getInValid())
                            && "0".equals(dateBean.getIsTodayPrev())) {
                        dealInEnterAndOutTimeStyle(holder.itemView, holder.mLLDay, holder.mTvTitle, holder.mTvDesc, dateBean);
                    }

                }
            }

            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dealOnDayClicked(holder.itemView, holder.mTvTitle, holder.mTvDesc, dateBean);
                }
            });

        }


        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    /**
     * 处理在入住 和 离店时间中间的item的背景
     *
     * @param itemView
     * @param mTvTitle
     * @param mTvDesc
     * @param dateBean
     */
    protected void dealInEnterAndOutTimeStyle(View itemView, LinearLayout mLLDay, TextView mTvTitle, TextView mTvDesc, DateBean dateBean) {

    }

    /**
     * 处理酒店日历中入住和离店的时间选中效果
     *
     * @param itemView
     * @param mTvTitle
     * @param mTvDesc
     * @param dateBean
     * @param isEnter
     */
    protected void dealEnterOrOutDayStyle(View itemView, LinearLayout mLLDay, TextView mTvTitle, TextView mTvDesc, DateBean dateBean, boolean isEnter) {

    }

    /**
     * 处理天的点击时间
     *
     * @param itemView
     * @param mTvTitle
     * @param mTvDesc
     * @param dateBean
     */
    public void dealOnDayClicked(View itemView, TextView mTvTitle, TextView mTvDesc, DateBean dateBean) {
        Log.d(TAG, "dealOnDayClicked: 基础日历处理天item的点击事件...");
    }

    protected void setTodayTitleAndSize(@NonNull View itemView, @NonNull TextView titleView, @NonNull TextView descView, DateBean dateBean) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        descView.setText("今天");
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

    protected class DateBean {
        //是否为无效数据
        private String inValid;
        //是否为今天以前的数据
        private String isTodayPrev;
        //每天的时间
        private Date time;
        //是否为今天
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

        public String getIsTodayPrev() {
            return isTodayPrev;
        }

        public void setIsTodayPrev(String isTodayPrev) {
            this.isTodayPrev = isTodayPrev;
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


    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void logd(String info) {
        Log.d(TAG, info);
    }

    protected String formatDateToStandardTime(Date date) {
        return sdf.format(date);
    }

    /**
     * 对比两个Date 大小
     *
     * @param date1
     * @param date2
     * @return
     */
    protected boolean compareDate(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        return calendar1.compareTo(calendar2) > 0;
    }

    /**
     * 对比两个Date 是否日期相等
     *
     * @param date1
     * @param date2
     * @return
     */
    protected boolean compareDateEquals(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);


        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE);
    }


    /**
     * 判断Date1 是否在Date2 30天的范围内
     *
     * @param date1
     * @param date2
     * @return
     */
    protected boolean compareLessThan30Day(Date date1, Date date2) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.add(Calendar.DAY_OF_MONTH, 30);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        return calendar1.compareTo(calendar2) <= 0;
    }

    /**
     * date1 < date2
     *
     * @param date1
     * @param date2
     * @return
     */
    protected boolean compareLessThan(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar1.compareTo(calendar2) < 0;
    }

    /**
     * date1 > date2
     *
     * @param date1
     * @param date2
     * @return
     */
    protected boolean compareGreaterThan(Date date1, Date date2) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        return calendar1.compareTo(calendar2) > 0;
    }

}
