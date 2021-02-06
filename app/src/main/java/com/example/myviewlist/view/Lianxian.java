package com.example.myviewlist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myviewlist.R;

public class Lianxian extends LinearLayout {
    private static final String TAG = "Lianxian";

    private View mView;
    private static final String [] imagesName= new String[]{"one","two","three","four","five"};
    private static final DogBean [] dogs= new DogBean[]{new DogBean(R.drawable.one,"one"),new DogBean(R.drawable.two,"two"),new DogBean(R.drawable.three,"three"),new DogBean(R.drawable.four,"four"),new DogBean(R.drawable.five,"five")};
    private int startX,startY,endX,endY;
    private Paint paint;

    public Lianxian(Context context) {
        super(context);
        init(context, null);
        setWillNotDraw(false);
    }

    public Lianxian(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setWillNotDraw(false);
    }



    public Lianxian(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setWillNotDraw(false);
    }
    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.lianxian_view, this, false);
        LinearLayout ll1 = mView.findViewById(R.id.ll1);
        ll1.removeAllViews();
        for(int i=0;i<imagesName.length;i++){
            String s = imagesName[i];
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.lianxianitem, ll1, false);
            textView.setText(s);
            ll1.addView(textView);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int left = v.getLeft()+100;
                    int top = v.getTop()+200;
                    Log.d(TAG, "onClick: "+left+","+top);
                    startX = left;
                    startY = top;

                    for(int j=0;j<ll1.getChildCount();j++){
                        TextView child = (TextView) ll1.getChildAt(j);
                       child.setTextColor(ContextCompat.getColor(context,R.color.color_333333));
                    }
                    textView.setTextColor(Color.RED);
                }
            });
        }
        LinearLayout ll2 = mView.findViewById(R.id.ll2);
        for(int i=0;i<dogs.length;i++){
            DogBean dogBean= dogs[i];
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400,400,1);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(ContextCompat.getDrawable(context,dogBean.image));
            ll2.addView(imageView);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int left = v.getLeft()+600;
                    int top = v.getTop()+200;
                    Log.d(TAG, "onClick: "+left+","+top);
                    endX = left;
                    endY = top;

                    invalidate();
                }
            });
        }
        addView(mView);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: "+startX+"--"+startY+"--"+endX+"--"+endY);
        canvas.drawLine(startX,startY,endX,endY,paint);
    }

    static class DogBean{
        int image;
        String name;

        public DogBean(int image, String name) {
            this.image = image;
            this.name = name;
        }
    }
}
