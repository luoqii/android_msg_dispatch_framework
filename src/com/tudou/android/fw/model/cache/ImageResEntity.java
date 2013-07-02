package com.tudou.android.fw.model.cache;

import android.graphics.Bitmap;

public class ImageResEntity {
    private String mUrl;
    private String mUrlDigest;
    private Bitmap mBitmap;
    
    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }  
    
    public String getUrlDigest() {
        return mUrlDigest;
    }
    
    public void setUrlDigest(String digest) {
        this.mUrlDigest = digest;
    }
    
    public Bitmap getBitmap() {
        return mBitmap;
    }
    
    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
