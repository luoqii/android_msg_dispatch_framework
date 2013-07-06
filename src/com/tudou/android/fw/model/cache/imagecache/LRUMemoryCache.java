package com.tudou.android.fw.model.cache.imagecache;

import android.support.v4.util.LruCache;

import com.tudou.android.fw.util.Log;

import java.io.IOException;

public class LRUMemoryCache implements IImageCache {
    private static final String TAG = LRUMemoryCache.class.getSimpleName();
    private static final boolean DEBUG = false;
    
    private LruCache<String, Object> mLruCache;
    
    public LRUMemoryCache(int maxSize) {
        mLruCache = new LruCache<String, Object>(maxSize);
        
        Log.i(TAG, "init lru size: " + maxSize);
    }

    @Override
    public byte[] get(ICacheSpec spec) throws IOException {        
        byte[] data = (byte[]) mLruCache.get(spec.getUrlDigest());
        if (DEBUG) {
            Log.d(TAG, "get(). spec: " + spec + "\tdata: " + data);
            Log.d(TAG, "hitCount: " + mLruCache.hitCount() + "\tmissCount: " + mLruCache.missCount());
        }
        
        return data;
    }

    @Override
    public boolean insert(ICacheSpec spec, byte[] data) throws IOException {
        if (DEBUG) {
            Log.d(TAG, "insert(). spec: " + spec);
        }
        mLruCache.put(spec.getUrlDigest(), data);
        
        return true;
    }

    @Override
    public boolean has(ICacheSpec spec) throws IOException {
        return true; // always try
    }
}
