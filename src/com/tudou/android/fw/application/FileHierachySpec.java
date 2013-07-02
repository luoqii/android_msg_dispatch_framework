package com.tudou.android.fw.application;

/**
 * specify file hierachy in external(SD Card)/internal sotrage.
 * 
 * initiate extDirPath firstly.
 * 
 * @author bysong@tudou.com
 *
 * @see #setExtDirPath(String);
 */
public class FileHierachySpec {
    
    private static FileHierachySpec sInstance;
    
    private String mExtDirPath;

    private Object absolutePath;

    private String mExtCachePath;

    public static FileHierachySpec getInstance() {
        if (sInstance == null) {
            sInstance = new FileHierachySpec();
        }
        
        return sInstance;
    }
    
    public String getExtDirPath() {
        return mExtDirPath;
    }
    
    /**
     * specify application's external dir.
     * ${company}/${platform}/${product}
     * 
     * @param path
     */
    public void setExtDirPath(String path) {
        mExtDirPath = path;
    }
    
    public void setExtCacheDirPath(String absolutePath) {
        mExtCachePath = absolutePath;
    }
    
    public String getExtCacheDirPath() {
        if (null != mExtCachePath && mExtCachePath.length() > 0) {
            return mExtCachePath;
        }
        
        return mExtDirPath + "/" + ".cache";
    }
    
    public String getExtConfDirPath() {
        return mExtDirPath + "/" + ".conf";
    }
    
    public String getExtLogDirPath() {
        return mExtDirPath + "/" + "log";
    }
    
    public String getExtDownloadDirPath() {
        return mExtDirPath + "/" + "download";
    }
    
}
