package com.example.myviewlist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myviewlist.R;
import com.example.myviewlist.bean.NetWorkBaseBean;
import com.example.myviewlist.bean.Users;
import com.example.myviewlist.net.OnHttpRequestCallBack;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


//        requestData(new OnHttpRequestCallBack<Users>() {
//            @Override
//            public void onSuccess(Users users) {
//
//            }
//        });
    }

//    private void requestData(OnHttpRequestCallBack callBack) {
//        Type[] superclass = callBack.getClass().getGenericInterfaces();
//        Type type = superclass[0];
//        if(type!=null && type instanceof ParameterizedType){
//            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
//            Log.d(TAG, "requestData: types[0] == "+types[0]);
//        }
//    }
}