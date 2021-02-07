package com.example.myviewlist.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myviewlist.R;
import com.example.myviewlist.adapter.HouseTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HotelDetailActivity extends AppCompatActivity {

    private static final int SET_CHECKIN_DATE = 0x100;
    private static final String TAG = "jyw";
    private RecyclerView mRvHouseTypeList;
    private View mIvClose;
    private TextView mTvCheckInTime;
    private TextView mTvCheckOutTime;
    private TextView mTvCheckInTimeDesc;
    private TextView mTvCheckOutTimeDesc;
    private TextView mTvTotalDay;
    private LinearLayout mLLCheckIn;
    private long checkInTime, checkOutTime;
    private static final String[] weekDays = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        initView();
    }

    private void initView() {
        mRvHouseTypeList = findViewById(R.id.rvHouseTypeList);
        mLLCheckIn = findViewById(R.id.ll_checkin);
        mTvCheckInTime = findViewById(R.id.tvCheckInTime);
        mTvCheckOutTime = findViewById(R.id.tvCheckOutTime);
        mTvCheckInTimeDesc = findViewById(R.id.tvCheckInTimeDesc);
        mTvCheckOutTimeDesc = findViewById(R.id.tvCheckOutTimeDesc);
        mTvTotalDay = findViewById(R.id.tvTotalDay);

        mRvHouseTypeList.setLayoutManager(new LinearLayoutManager(this));
        HouseTypeAdapter mHouseTypeAdapter = new HouseTypeAdapter(this);
        mRvHouseTypeList.setAdapter(mHouseTypeAdapter);

        mIvClose = findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLLCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置入住日期
                Intent intent = new Intent(HotelDetailActivity.this, HotelCalendarActivity.class);
                intent.putExtra("checkInTime", checkInTime);
                intent.putExtra("checkOutTime", checkOutTime);
                startActivityForResult(intent, SET_CHECKIN_DATE);
            }
        });

        //设置默认的入住日期
        setDefaultCheckInDate();
    }


    private void setCheckInDate(Date inDate, Date outDate) {
        if (inDate == null || outDate == null) {
            setDefaultCheckInDate();
            return;
        }
        Log.d(TAG, "setCheckInDate: 重新设置了入住时间:" + inDate + " --- " + outDate);

        mTvCheckInTime.setText(getMonthAndDateString(inDate));
        mTvCheckInTimeDesc.setText(getCheckInTimeDesc(inDate,0));
        mTvCheckOutTime.setText(getMonthAndDateString(outDate));
        mTvCheckOutTimeDesc.setText(getCheckInTimeDesc(outDate,1));
        mTvTotalDay.setText(getTotalDay(inDate,outDate)+"晚");

    }

    private int getTotalDay(Date inDate, Date outDate) {
        long l = outDate.getTime() - inDate.getTime();
        int days=new Long(l/(1000*60*60*24)).intValue();
        return days;
    }

    /**
     * 获取某个日期是周几  并且加入 入住 字样
     * @param date
     * @return
     */
    private String getCheckInTimeDesc(Date date,int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            return "今天"+(type==0?"入住":"离店");
        }
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "getCheckInTimeDesc: i = "+i);
        String weekDay = weekDays[i-1];
        return weekDay+(type==0?"入住":"离店");
    }

    /**
     * 设置默认的入住时间
     */
    private void setDefaultCheckInDate() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        mTvCheckInTime.setText(getMonthAndDateString(now));
        mTvCheckInTimeDesc.setText("今天入住");
        Calendar calendar1 = (Calendar) calendar.clone();
        calendar1.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar1.getTime();
        Log.d(TAG, "setDefaultCheckInDate: 明天 == "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tomorrow));
        mTvCheckOutTime.setText(getMonthAndDateString(tomorrow));
        mTvCheckOutTimeDesc.setText(getCheckInTimeDesc(tomorrow,1));
        mTvTotalDay.setText("1晚");

        checkInTime = now.getTime();
        checkOutTime = tomorrow.getTime();
    }

    private String getMonthAndDateString(Date date) {
        return new SimpleDateFormat("MM月dd日").format(date == null ? new Date() : date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SET_CHECKIN_DATE:
                    long inTime = data.getLongExtra("checkInTime", 0);
                    long outTime = data.getLongExtra("checkOutTime", 0);
                    if (inTime != 0) {
                        checkInTime = inTime;
                    }
                    if (outTime != 0) {
                        checkOutTime = outTime;
                    }

                    if (inTime != 0 && outTime != 0) {
                        setCheckInDate(new Date(checkInTime), new Date(checkOutTime));
                    }

                    break;
            }
        }
    }
}