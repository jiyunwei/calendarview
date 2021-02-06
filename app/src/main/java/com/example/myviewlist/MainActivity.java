package com.example.myviewlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myviewlist.activity.BaseCalendarActivity;
import com.example.myviewlist.activity.HotelCalendarActivity;
import com.example.myviewlist.activity.HotelDetailActivity;
import com.example.myviewlist.activity.TestActivity;
import com.example.myviewlist.activity.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mListView = findViewById(R.id.list_view);
        mListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Object item = parent.getAdapter().getItem(position);
            if (item instanceof String) {
                String text = (String) item;
                Intent intent = new Intent();
                switch (text) {
                    case "基础日历":
                        intent.setClass(MainActivity.this, BaseCalendarActivity.class);
                        break;
                    case "酒店日历":
                        intent.setClass(MainActivity.this, HotelCalendarActivity.class);
                        break;
                    case "酒店详情页":
                        intent.setClass(MainActivity.this, HotelDetailActivity.class);
                        break;
                    case "WebView测试":
                        intent.setClass(MainActivity.this, WebViewActivity.class);
                        break;
                    case "测试":
                        intent.setClass(MainActivity.this, TestActivity.class);
                        break;

                }

                startActivity(intent);
            }
        });
    }
}