package com.example.myviewlist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myviewlist.R;
import com.example.myviewlist.adapter.HouseTypeAdapter;

public class HotelDetailActivity extends AppCompatActivity {

    private RecyclerView mRvHouseTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        initView();
    }

    private void initView() {
        mRvHouseTypeList = findViewById(R.id.rvHouseTypeList);
        mRvHouseTypeList.setLayoutManager(new LinearLayoutManager(this));
        HouseTypeAdapter mHouseTypeAdapter = new HouseTypeAdapter(this);
        mRvHouseTypeList.setAdapter(mHouseTypeAdapter);
    }
}