package com.tudou.android.fw.model.cache.imagecache;

import com.tudou.android.fw.util.Log;

import java.io.IOException;

public class MeasureCache implements IImageCache {
    private static final String TAG = MeasureCache.class.getSimpleName();
    private IImageCache mTarget;
    private String mCacheName;
    
    private int mMethodGetCalledTime;
    private long mMethodGetDuration;  
    private int mMethodInsertCalledTime;
    private long mMethodInsertDuration; 
    private int mMethodHasCalledTime;
    private long mMethodHasDuration;
    
    public MeasureCache(IImageCache target, String cacheName) {
        mTarget = target;
        mCacheName = cacheName;
    }

    @Override
    public byte[] get(ICacheSpec spec) throws IOException {
        long tick = System.nanoTime();
        byte[] data = mTarget.get(spec);
        long diff = System.nanoTime() - tick;
        
        mMethodGetCalledTime++;
        mMethodGetDuration += diff;
        
        Log.d(TAG, mCacheName + "\t:average elapse time in get(): " + (mMethodGetDuration / mMethodGetCalledTime));
        
        return data;
    }

    @Override
    public boolean insert(ICacheSpec spec, byte[] data) throws IOException {
        long tick = System.nanoTime();
        boolean insert = mTarget.insert(spec, data);
        long diff = System.nanoTime() - tick;
        
        mMethodInsertCalledTime++;
        mMethodInsertDuration += diff;
        
        Log.d(TAG, mCacheName + "\t:average elapse time in insert(): " + (mMethodInsertDuration / mMethodInsertCalledTime));
        
        return insert;
    }

    @Override
    public boolean has(ICacheSpec spec) throws IOException {
        long tick = System.nanoTime();
        boolean has = mTarget.has(spec);
        long diff = System.nanoTime() - tick;
        
        mMethodHasCalledTime++;
        mMethodHasDuration += diff;
        
        Log.d(TAG, mCacheName + "\t:average elapse time in insert(): " + (mMethodHasDuration / mMethodHasCalledTime));
        
        return has;
    }
}
