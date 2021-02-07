package com.example.myviewlist.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myviewlist.R;

public class HotelDetailTitleBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "jyw";
    // 列表顶部和title底部重合时，列表的滑动距离。
    private float deltaY;

    public HotelDetailTitleBehavior() {
        super();
    }

    public HotelDetailTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if(target instanceof NestedScrollView){
            Log.d(TAG, "onNestedPreScroll: 是嵌套布局...");
           View hotelNameView =  target.findViewById(R.id.ll_hotel_name);
            int hotelNameViewTop = hotelNameView.getTop();
            int hotelNameHeight = hotelNameView.getHeight();
            Log.d(TAG, "onNestedPreScroll: 酒店名称上方的y == "+hotelNameViewTop + " hotelNameHeight酒店名称的高度 = "+hotelNameHeight);
            NestedScrollView mScrollView = (NestedScrollView) target;
            mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY>oldScrollY){
                        Log.d(TAG, "onScrollChange: 正在下滑..."+scrollY);
                    }else if(scrollY<oldScrollY){
                        Log.d(TAG, "onScrollChange: 正在上滑..."+scrollY);
                    }

                    if(scrollY >hotelNameViewTop){
                        int distanceY = scrollY - hotelNameViewTop;
                        float alpha = distanceY /(hotelNameHeight*1.0f);
                        alpha = Math.min(1.0f,alpha);
                        Log.d(TAG, "onScrollChange: distanceY = "+distanceY + " alpha = "+alpha);
                        child.setAlpha(alpha);
                    }else{
                        child.setAlpha(0);
                    }
                }
            });
        }


    }
}
