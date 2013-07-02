package com.tudou.android.fw.model.cache.imagecache;

import java.io.IOException;

/**
 * do image cache stuff. 
 * now we do NOT support house-keeping work.
 * 
 * TODO need cache limit.
 * 
 * @author bysong@tudou.com
 *
 */
public interface IImageCache {
    /**
     * retrive cached image data, if have;
     * 
     * @param infomation based on cache directory.
     * @return data if have, otherwise 
     * @throws IOException 
     */
    public byte[] get(ICacheSpec spec) throws IOException;
    
    /**
     * 
     * insert image data to cache
     * 
     * @param infomation based on cache directory.
     * @param data
     * @return true if inserting is ok.
     * @throws IOException 
     */
    public boolean insert(ICacheSpec spec, byte[] data) throws IOException;
    
    /**
     * check if cache has existed.
     * 
     * @param infomation based on cache directory.
     * @return true if itemId has cached;
     * @throws IOException 
     */
    public boolean has(ICacheSpec spec) throws IOException;
    
    /**
     * describe a cache item.
     * 
     * @author bysong@tudou.com
     *
     */
    public interface ICacheSpec {  
        /**
         * you guess 
         * 
         * @return 
         */
        public String getUrlDigest();        
        /**
         * you guess
         * 
         * @return
         */
        public String getCacheFileNameSufix();
    }
}
