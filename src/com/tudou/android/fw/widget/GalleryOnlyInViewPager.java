package com.tudou.android.fw.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Gallery;


/**
 * 
 * 
 * inspired by czhu@tudou.com
 * edit by bysong@tudou.com
 *
 */
public class GalleryOnlyInViewPager extends Gallery {
    private static final String TAG = GalleryOnlyInViewPager.class.getSimpleName();
    private boolean mHasViewPager;
    private ViewPager mViewPager;

    public GalleryOnlyInViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public GalleryOnlyInViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public GalleryOnlyInViewPager(Context context) {
        super(context);
        
        init();
    }

    private void init() {
        mHasViewPager = parentHasViewPager();
    }

    private boolean parentHasViewPager() {
        boolean has = false;
        
        ViewGroup g = null;
        ViewParent p = this.getParent();
        while (p instanceof ViewGroup) {
            g = (ViewGroup) p;
            if (g instanceof ViewPager) {
                mViewPager = (ViewPager) g;
                has = true;
                break;
            }
            
            p = g.getParent();
        }
        
        return has;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHasViewPager && MotionEvent.ACTION_DOWN == event.getAction()) {
            requestDisallowInterceptTouchEvent(true);
        }
        
        return super.onTouchEvent(event);
    }
}
