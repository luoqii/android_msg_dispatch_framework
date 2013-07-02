package com.tudou.android.fw.test.androidunit;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudou.android.fw.activity.AbsPageActivity;
import com.tudou.android.fw.activity.LogcatActivity;
import com.tudou.android.fw.page.layout.AnnotaionGestureOverlayView;
import com.tudou.android.fw.page.layout.AnnotationLinearLayout;
import com.tudou.android.fw.page.layout.LinearLayout;
import com.tudou.android.fw.page.layout.RootLinearLayout;

public class AbspageActivityTestCase extends ActivityInstrumentationTestCase2<LogcatActivity> {
    
    private LogcatActivity mActivity;
    private View mHierarchy;
    private boolean mException;

    public AbspageActivityTestCase() {
        super("com.tudou.android.fw", LogcatActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mHierarchy = new RootLinearLayout(mActivity);
    }

    public void testAssureRootMsgHandler(){
        mHierarchy = new RootLinearLayout(mActivity);
        
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        com.tudou.android.fw.page.layout.LinearLayout child = new com.tudou.android.fw.page.layout.LinearLayout(mActivity);
        
        ((ViewGroup)mHierarchy).addView(child);
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        child.addView(new TextView(mActivity));
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        AnnotationLinearLayout annotationLinearLayout = new AnnotationLinearLayout(mActivity);
        child.addView(annotationLinearLayout);
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        android.widget.LinearLayout nonPageChild = new android.widget.LinearLayout(mActivity);
        ((ViewGroup)annotationLinearLayout).addView(nonPageChild);
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        nonPageChild.addView(new AnnotaionGestureOverlayView(mActivity));    
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        // no error
        assertTrue(true);
        
        nonPageChild.addView(new NullPage(mActivity));    
        AbsPageActivity.assureRootMsgHandler(mHierarchy);        
        mException = false;        
        try {
            AbsPageActivity.assureRootMsgHandler(mHierarchy);
        } catch (IllegalStateException e) {
            mException = true;
        }
        
        assertTrue("must throws an IllegalStateException exception", mException);

        
    }
    
    public void testAssureRootMsgHandler_null(){
        mHierarchy = null;        
        mException = false;
        
        try {
            AbsPageActivity.assureRootMsgHandler(mHierarchy);
        } catch (IllegalArgumentException e) {
            mException = true;
        }
        
        assertTrue("must throws an IllegalArgumentException exception", mException);
        
    }
    
    public void testAssureRootMsgHandler_noRoot(){
        mHierarchy = new LinearLayout(mActivity);        
        mException = false;
        
        try {
            AbsPageActivity.assureRootMsgHandler(mHierarchy);
        } catch (IllegalArgumentException e) {
            mException = true;
        }
        
        assertTrue("must throws an IllegalArgumentException exception", mException);
        
    }

}
