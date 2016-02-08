package com.example.jb.thirtyme.Login;

/**
 * Created by jb on 2016-02-08.
 */
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LoginActivityViewPager extends ViewPager {
    private boolean mIsSwipingEnabled = true;

    public LoginActivityViewPager(Context context) {
        super(context);
    }

    public LoginActivityViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIsSwipingEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsSwipingEnabled && super.onInterceptTouchEvent(ev);
    }

    public void setSwipingEnabled(boolean enabled) {
        mIsSwipingEnabled = enabled;
    }
}
