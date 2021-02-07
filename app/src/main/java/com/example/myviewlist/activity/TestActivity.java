package com.example.myviewlist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.myviewlist.R;
import com.example.myviewlist.adapter.HouseTypeAdapter;
import com.example.myviewlist.bean.NetWorkBaseBean;
import com.example.myviewlist.bean.Users;
import com.example.myviewlist.net.OnHttpRequestCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private RecyclerView mRvHouseTypeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        initView();
    }

    private void initView() {
        mRvHouseTypeList = findViewById(R.id.my_list);
        mRvHouseTypeList.setLayoutManager(new LinearLayoutManager(this));
        HouseTypeAdapter mHouseTypeAdapter = new HouseTypeAdapter(this);
        mRvHouseTypeList.setAdapter(mHouseTypeAdapter);
    }
}