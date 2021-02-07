package com.example.myviewlist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myviewlist.R;
import com.example.myviewlist.view.CalendarView;
import com.example.myviewlist.view.HotelCalendarView;

import java.util.Date;

public class HotelCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_calendar);

        HotelCalendarView mCalendarView = findViewById(R.id.hotel_calendarview);
        mCalendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date inDate, Date outDate) {
                Intent intent = new Intent();
                intent.putExtra("checkInTime",inDate.getTime());
                intent.putExtra("checkOutTime",outDate.getTime());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        Intent intent = getIntent();
        long inTime = intent.getLongExtra("checkInTime", 0);
        long outTime = intent.getLongExtra("checkOutTime", 0);
        if(inTime!=0 && outTime!=0){
            mCalendarView.setEnterAndOutHotelTime(new Date(inTime),new Date(outTime));
        }
    }
}