
package com.tudou.android.fw.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.LogcatActivity.FilterSpec;
import com.tudou.android.fw.application.App;
import com.tudou.android.fw.application.ComponentCloseable;
import com.tudou.android.fw.model.ITudouSchedule;
import com.tudou.android.fw.model.ITudouSchedule.Callback;
import com.tudou.android.fw.model.TudouScheduleFacotry;
import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.page.layout.AnnotaionGestureOverlayView;
import com.tudou.android.fw.util.DisplayUtil;
import com.tudou.android.fw.util.Log;
import com.tudou.android.fw.util.WindowUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * top-level activity based on {@link TudouScheduler}.
 * <P>
 * you can send request by {@link #sendRequest(IRequest)}; Asynchronously 
 * when response which is triggered by this is arrived, 
 * {@link #onResponse(IResponse)} will be invoked if all are Ok, you should
 * check error result with {@link IResponse#inError()} if a response is not
 * triggered, a warning message will be logged.
 * <P>
 * subclass should (un)register callback appropriately, but you can avoid to
 * this by call {@link #manageCallback()} which will register callback onStart()
 * and unregister callback onStop() automatically. good luck
 * <p>
 * TODO pull gesture & overlay logic to upper level. bysong
 * 
 * @author bysong@tudou.com
 * @see #sendRequest(IRequest)
 * @see #onResponse(IResponse)
 * @see IResponse#inError()
 * @see #manageCallback(boolean)
 */
public abstract class TudouActivity extends DebugActivity
        implements ComponentCloseable, OnGesturePerformedListener
{
    private static final String TAG = TudouActivity.class.getSimpleName();

    private static final boolean LOG = false;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_GESTURE = false;
    private static final boolean DEBUG_RES_CONFIG = true;
    private static final boolean INJECT_GESTURE_OVERLAY = false;

    protected App mApplication;
    protected ITudouSchedule mTudouService;
    protected ITudouSchedule.Callback mCallback;

    private static final boolean DEBUG_RCV_UNWANTED_RESPONSE = true && DEBUG;
    private HashMap<Long, Boolean> mExpectedRes = new HashMap<Long, Boolean>();

    private boolean mManageCallback;

    private static final boolean OVERLAY = false;
    protected GestureLibrary mGestureLib;
    private static final double GESTURE_SCORE_THRESHOLD = 5.0;

    /**
     * an response(error or error-less) is return.
     * 
     * @param response
     * @see {@link IResponse#inError()}
     */
    protected abstract void onResponse(IResponse response);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = App.getInstance();
        registerComponentListener();
        init();

        if (null == getTag()) {
            String detailMessage = "getTag() must return a non-null String.";
            throw new IllegalArgumentException(detailMessage);
        }

        // XXX we need pull this logic up ??? bysong@tudou.com
        if (DEBUG_RES_CONFIG && !mApplication.isReleaseMode()) {
            DisplayMetrics dm = this.getResources().getDisplayMetrics();
            // TODO is there a better solution for this??? bysong@tudou.com
            Log.d(getTag(),
                    "current res conf: " + getResources().getString(R.string.lib_res_conf)
                            + ",resolution:" + dm.widthPixels + "X" + dm.heightPixels
                            + ",physical size:" + DisplayUtil.getScreenSize(this));
        }
    }

    private void init() {

        mCallback = new Callback() {

            @Override
            public void onResponse(IResponse res) {
                TudouActivity.this.doResponse(res);
            }

        };
        mTudouService = TudouScheduleFacotry.getInstance().createTudouSchedule();
        manageCallback(true);

        mGestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mGestureLib.load()) {
            Log.w(TAG, "can NOT load gesture lib.");
        }

        injectOverlay();
        setRequestedOrientation(onGetScreenOrientation());
    }

    /**
     * return application's screen orientation adjustment.
     * 
     * @return
     * @see {@link ActivityInfo}
     */
    protected int onGetScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mGestureLib.recognize(gesture);

        if (predictions.size() > 0) {
            if (DEBUG_GESTURE) {
                Log.d(TAG, "gesture size: " + predictions.size());
            }
            Prediction prediction = predictions.get(0);

            if (DEBUG_GESTURE) {
                Log.d(TAG, "gesture score: " + prediction.score);
            }
            if (prediction.score > GESTURE_SCORE_THRESHOLD) {
                String name = prediction.name;
                if (getString(R.string.lib_gesture_debug).equals(name)) {
                    gotoDebugActivity();
                } else if (getString(R.string.lib_gesture_log).equals(name)) {
                    gotoLogActivity();
                } else if (getString(R.string.lib_gesture_close).equals(name)) {
                    closeApplication();
                }
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        View v = onPreSetContentView(view, null);

        super.setContentView(v);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        View v = onPreSetContentView(view, params);

        super.setContentView(v, params);
    }

    protected View onPreSetContentView(View contentView, LayoutParams params) {
        if (!mApplication.isReleaseMode() && !mApplication.isMonkeyMode() && INJECT_GESTURE_OVERLAY) {
            return injectGestrueOverlayView(contentView);
        }

        return contentView;
    }

    private View injectGestrueOverlayView(View contentView) {
        ViewGroup.LayoutParams fillParent = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);

        AnnotaionGestureOverlayView overlay = new AnnotaionGestureOverlayView(this);
        overlay.setGestureColor(Color.TRANSPARENT);
        overlay.setUncertainGestureColor(Color.TRANSPARENT);
        overlay.addOnGesturePerformedListener(this);
        overlay.addView(contentView, fillParent);

        contentView = overlay;

        return overlay;
    }

    protected void gotoDebugActivity() {
    }

    protected void gotoInfoActivity() {
    }

    protected void gotoLogActivity() {
        if (null == mApplication) {
            return;
        }
        Intent intent = new Intent(this,
                LogcatActivity.class
                );

        FilterSpec spec = new FilterSpec();
        spec.mId = "com.tudou.android.DEFAULT_FILTER";
        spec.mLabel = mApplication.getLogTag();
        spec.mTag = mApplication.getLogTag();
        // spec.mPid = Process.myPid();
        intent.putExtra(LogcatActivity.EXTRA_FILTER_SPEC, spec);
        startActivity(intent);
    }

    protected void closeApplication() {
        if (null != mApplication) {
            mApplication.closeApplication();
        }
    }

    @SuppressWarnings("unused")
    private void injectOverlay() {
        if (!OVERLAY || mApplication.isReleaseMode()) {
            return;
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Window w = WindowUtil.makeNewWindow(this);
        if (null == w) {
            return;
        }
        w.setWindowManager(wm, null, null);

        View overlay = new View(this);
        w.setContentView(overlay, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wm.addView(w.getDecorView(), params);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mManageCallback) {
            registerCallback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mManageCallback) {
            unRegisterCallback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unRigesterComponentListener();
    }

    protected void registerCallback() {
        mTudouService.registerCallBack(mCallback);
    }

    protected void unRegisterCallback() {
        mTudouService.unRegisterCallBack(mCallback);
    }

    protected void manageCallback(boolean manage) {
        mManageCallback = manage;
    }

    protected void sendRequest(IRequest req) {
        if (LOG) {
            Log.d(getTag(), "SEND  request. " + "\trequest: " + req);
        }

        mExpectedRes.put(req.getId(), true);
        mTudouService.sendRequest(req);
    }

    private void doResponse(IResponse res) {
        if (responseIsWanted(res)) {
            onResponse(res);
        }
    }

    private boolean responseIsWanted(IResponse res) {
        long reqId = res.getRequestId();
        Boolean expected = mExpectedRes.get(reqId);

        boolean wanted = expected == Boolean.TRUE;
        if (wanted) {
            mExpectedRes.remove(reqId);
        }

        if (Boolean.TRUE == expected) {
            if (LOG) {
                Log.d(getTag(), "RCV response. " + "\tresponse: "
                        + res);
            }
        }

        if (DEBUG_RCV_UNWANTED_RESPONSE) {
            if (Boolean.TRUE != expected) {
                Log.w(
                        getTag(),
                        "RCV response which we have NOT triggered. do you unRegisterCallback it? response: "
                                + res);
            }
        }

        return wanted;
    }

    @Override
    public void close() {
        super.finish();// do not call this.finish().
    }

    @Override
    public void finish() {
        super.finish();

        unRigesterComponentListener();
    }

    /**
     * 
     */
    private void registerComponentListener() {
        mApplication.reginsterComponentCallback(this);
    }

    /**
     * 
     */
    private void unRigesterComponentListener() {
        if (null != mApplication) {
            mApplication.unRegisterComponentCallback(this);
            mApplication = null;
        }
    }

    protected void onFireInTheHole() {
        if (null != mApplication && mApplication.isMonkeyMode()) {
            return;
        }
        gotoInfoActivity();
    }
}
