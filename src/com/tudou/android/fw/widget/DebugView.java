package com.tudou.android.fw.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.util.WindowUtil;

public class DebugView extends FrameLayout {
    private static final boolean DEBUG = true;
    private static final String TAG = DebugView.class.getSimpleName();
	
    private WindowManager.LayoutParams mDecorLayoutParams;
	private WindowManager mWindowManager;
	private Window mWindow;
    private View mDecor;
    private Context mContext;
    private DebugView mRoot;
	
	public DebugView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DebugView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mRoot = this;
		addView(inflate(mContext, R.layout.lib_debug_view_content, null));
		CharSequence activityName = mContext.getClass().getSimpleName() + "@" + mContext.hashCode();
		((TextView)findViewById(R.id.content)).setText(activityName);
	
		initFloatingWindowLayout();
	    initFloatingWindow();
	}
	
	public void hide() {
	    mWindowManager.removeView(mDecor);
	}

	public void show() {
	    mWindowManager.addView(mDecor, mDecorLayoutParams);

	}

	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (mRoot != null)
            initControllerView(mRoot);
	}
	
	private void initFloatingWindowLayout() {
        mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = Gravity.TOP;
        p.height = LayoutParams.WRAP_CONTENT;
        p.x = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = 10000;
        p.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                ;  
        
        p.token = null;
        p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
    }
    
    private void initFloatingWindow() {
        mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        // bysong
//        mWindow = PolicyManager.makeNewWindow(mContext);
        mWindow = WindowUtil.makeNewWindow(mContext);
        
        mWindow.setWindowManager(mWindowManager, null, null);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
//        mDecor.setOnTouchListener(mTouchListener);
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);
        
        // While the media controller is up, the volume control keys should
        // affect the media stream type
        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }
    
    private void initControllerView(View root) {
		
	}

}
