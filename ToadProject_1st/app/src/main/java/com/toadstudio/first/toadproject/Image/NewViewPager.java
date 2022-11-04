package com.toadstudio.first.toadproject.Image;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 273 on 2018-03-06.
 */

public class NewViewPager extends ViewPager {
    public static String TAG = "ToadPrj_NewViewPager";
    static int mSavedX = 0;
    static int mCount = 0;  //todo mCount는 배열 길이 일듯
    static int toggle = 0;
    static int currentItem = 0;
    final static int HORIZONTAL_ITEM_CHANGE_THRESHOLD = 1000;

    public NewViewPager(Context context) {
        super(context);
    }

    public NewViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void scrollBy ( int x, int y ) {
        if (toggle == 0) {
            mSavedX = x;
            toggle = 1;
            currentItem = getCurrentItem();
        } else {
            mSavedX = mSavedX - x;
            toggle = 0;

            if (currentItem == 0) {
                if (mSavedX < 0) {
                    setCurrItem();
                    return;
                }
            }

            if (currentItem == mCount - 1) {
                if (mSavedX > 0) {
                    setCurrItem();
                    return;
                }
            }

            //   super.scrollBy( x, 0 );
            /*
            if (getWidth() - Math.abs(mSavedX) < HORIZONTAL_ITEM_CHANGE_THRESHOLD) {
                if (mSavedX < 0) {
                    setPrevItem();
                } else {
                    setNextItem();
                }
            }
            */
        }
/*
        Log.d(TAG, "x : " + x
                + "savedX : " + mSavedX
                + " curr : " + currentItem
                + " count : " + mCount
                + " width : " + getWidth());
                */
    }

    public void setNextItem(){
        mSavedX = 0;
 //       Log.d(TAG, "setNextItem");
        setCurrentItem( currentItem+1 , true );
    }

    public void setPrevItem(){
        mSavedX = 0;
//        Log.d(TAG, "setPrevItem");
        setCurrentItem( currentItem-1 , true );
    }

    public void setCurrItem(){
        mSavedX = 0;
        setCurrentItem( currentItem , true );
    }

    public void setCurrNum(int num) {
        setCurrentItem(num, true);
   //     Log.d(TAG, "setCurrNum: " + num);
     //   currentItem = num;
    }

    // pointerIndex out of range
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}
