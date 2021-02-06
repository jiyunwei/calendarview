package com.example.myviewlist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

public class MyTitleBehavior extends CoordinatorLayout.Behavior<TextView> {
    private static final String TAG = "MyTitleBehavior";


    public MyTitleBehavior() {
    }

    public MyTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        Log.d(TAG, "onDependentViewChanged: "+dependency.getTop());
        return super.onDependentViewChanged(parent, child, dependency);
    }


}
