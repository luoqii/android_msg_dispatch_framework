
package com.tudou.android.fw.model.cache.imagecache;

import com.tudou.android.fw.image.ImageWorkWrapper;
import com.tudou.android.fw.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/**
 * use {@link ImageWorkWrapper} instead.
 * 
 * @author bysong@tudou.com
 *
 */
@Deprecated
public class ImageCache implements IImageCache {
    public String TAG = ImageCache.class.getSimpleName();


    private List<IImageCache> mCaches;

    public ImageCache(List<IImageCache> caches) {
        mCaches = caches;
    }
    
    public ImageCache(IImageCache... caches){
    	mCaches = Arrays.asList(caches);
    }

    @Override
    public byte[] get(ICacheSpec spec) {
        byte[] data = null;
        for (IImageCache c : mCaches) {
            if (null != c) {
                try {
					data = c.get(spec);
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
                if (null != data) {
                    break;
                }
            }
        }

        return data;
    }

    @Override
    public boolean insert(ICacheSpec spec, byte[] data) {
        boolean inserted = false;
        for(IImageCache c : mCaches) {
            if (null != c) {
                try {
					inserted = c.insert(spec, data);
				} catch (IOException e) {
					Log.e(TAG, "", e);
				}
                if (inserted) {
                    break;
                }
            }
        }
        
        return inserted;
    }

    @Override
    public boolean has(ICacheSpec spec) throws IOException {
        boolean hasCached = false;
        for (IImageCache c : mCaches) {
            if (null != c) {
                hasCached = c.has(spec);
                if (hasCached) {
                    break;
                }
            }
        }

        return hasCached;
    }
}
