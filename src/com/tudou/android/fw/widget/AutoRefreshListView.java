
package com.tudou.android.fw.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.util.Log;

/**
 * if footview is visible to user, load more; if uer scroll to footview, load
 * more.
 * 
 * TODO merge with {@link AutoRefreshGridView}
 * 
 * @author bysong@tudou.com
 */
public class AutoRefreshListView extends ListView {
    private static final String TAG = AutoRefreshListView.class.getSimpleName();
    
    private static final boolean DEBUG = false;

    private AutoRefreshListView.LoadingView mFooter;
    private OnLoadingListener mListener;

    private boolean mRemoved;
    private boolean mAdded;

    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFooter = new LoadingView(context);
        mFooter.setVisibility(INVISIBLE);
        addFooterView(mFooter);
        mAdded = true;
    }

    public AutoRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRefreshListView(Context context) {
        this(context, null);
    }

    private void requestLoading() {
        if (null != mListener) {
            mListener.onLoading();
        }
    }

    /*public void showLoading(boolean error) {
        if (mRemoved && !mAdded) {
            TudouLog.d(TAG, "re-add footer view.");
            
            addFooterView(mFooter);
            setAdapter(getAdapter());
            mAdded = true;
        }
        
        mFooter.showLoading(error);
    }*/
    
    public void showLoading(boolean error) {
        if (mRemoved && !mAdded) {
            Log.d(TAG, "re-add footer view.");
            
            addFooterView(mFooter);
//            setAdapter(getAdapter());
            mAdded = true;
        }
        
        mFooter.showLoading(error);
    }

    public void hideLoading() {
        mFooter.hideLoading();
        
        Log.d(TAG, "remove  footer view");
        mRemoved = true;
        mAdded = false;
        removeFooterView(mFooter);
    }

    public void setOnLoadingListener(OnLoadingListener l) {
        mListener = l;
    }

    public void unSetOnLoadingListener(OnLoadingListener l) {
        mListener = null;
    }

    public static interface OnLoadingListener {
        public void onLoading();
    }

    class LoadingView extends FrameLayout {
        private final String TAG = AutoRefreshListView.TAG + "$" + LoadingView.class.getSimpleName();
        
        private View mAutoLoad;
        private View mReload;
        private boolean mError;
        private boolean mRequesting = false;

        public LoadingView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);

             setWillNotDraw(false);
             setWillNotCacheDrawing(true);
            
             inflate(context, R.layout.lib_auto_loading, this);
             mAutoLoad = findViewById(R.id.lib_auto_loading);
             mReload = findViewById(R.id.lib_re_load_container);
             ((Button)findViewById(R.id.lib_re_load)).setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    requestLoading();
                    mRequesting = true;
                }
            });
        }

        public LoadingView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LoadingView(Context context) {
            this(context, null);
        }
        
        void hideLoading() {
            if (DEBUG) {
                Log.d(TAG, "hideLoading().");
            }
            
            // XXX can not work! i must remove this footer view to
            // work, why? bysong@tudou.com
            setVisibility(GONE);            
        }
        
        void showLoading(boolean error) {            
            if (DEBUG) {
                Log.d(TAG, "showLoading(error: " + error + ").");
            }
            
            setVisibility(VISIBLE);
            mError = error;
            mRequesting = false;
            if (!error) {
                mReload.setVisibility(INVISIBLE);
                mAutoLoad.setVisibility(VISIBLE);
            } else {
                mReload.setVisibility(VISIBLE);
                mAutoLoad.setVisibility(INVISIBLE);   
           }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (getVisibility() == VISIBLE && !mError && !mRequesting) {
                requestLoading();
                mRequesting = true;
            }
        }
    }
}
