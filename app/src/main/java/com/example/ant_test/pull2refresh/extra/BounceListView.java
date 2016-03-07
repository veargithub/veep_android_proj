package com.example.ant_test.pull2refresh.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * @Title: BounceListView.java
 * @Package com.example.ant_test.pull2refresh.extra
 * @Description: 这是一个网上找到的具有“阻尼”效果的listview
 * @author Chenxiao
 * @date 2014-12-15 下午3:50:42
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BounceListView extends ListView{  
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;  
       
    private Context mContext;  
    private int mMaxYOverscrollDistance;  
       
    public BounceListView(Context context){  
        super(context);  
        mContext = context;  
        initBounceListView();  
    }  
       
    public BounceListView(Context context, AttributeSet attrs){  
        super(context, attrs);  
        mContext = context;  
        initBounceListView();  
    }  
       
    public BounceListView(Context context, AttributeSet attrs, int defStyle){  
        super(context, attrs, defStyle);  
        mContext = context;  
        initBounceListView();  
    }  
       
    private void initBounceListView(){  
        //get the density of the screen and do some maths with it on the max overscroll distance  
        //variable so that you get similar behaviors no matter what the screen size  
           
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();  
        final float density = metrics.density;  
           
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);  
    }  
       
    @Override  
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){   
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;   
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);    
    }  
       
}  
