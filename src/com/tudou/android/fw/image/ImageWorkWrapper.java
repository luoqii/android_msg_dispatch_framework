
package com.tudou.android.fw.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker;
import com.tudou.android.fw.util.Log;

public class ImageWorkWrapper {
    private static final String TAG = ImageWorkWrapper.class.getSimpleName();
    private static ImageWorkWrapper sOtherInstance;
    private static ImageWorkWrapper sSourceInstance;
    private ImageWorker mImageWorker;
    private Context mContext;

    private ImageWorkWrapper(Context context, int maxLruSize, int defaultRes, boolean isSourceSite) {
        Log.d(TAG, "maxLruSize: " + maxLruSize);
        ImageCacheParams cacheParams = new ImageCacheParams(
                isSourceSite ? ImageCacheParams.ImageType.SOURCE_SITE
                        : ImageCacheParams.ImageType.THUMBNAIL);
        mContext = context;
        cacheParams.memCacheSize = maxLruSize;
        // mImageWorker = new ImageFetcher(context, 200);
        mImageWorker = new TudouImageWorker(context);
        mImageWorker.setLoadingImage(defaultRes);
        mImageWorker.setImageCache(new ImageCache(context, cacheParams));
        mImageWorker.setImageFadeIn(false);
    }

    public static ImageWorkWrapper getThumbImgInstance(Context appContext, int maxLruSize,
            int defaultRes) {
        if (sOtherInstance == null) {
            sOtherInstance = new ImageWorkWrapper(appContext, maxLruSize, defaultRes, false);
        }

        return sOtherInstance;
    }

    public static ImageWorkWrapper getSourceImgInstance(Context appContext, int maxLruSize,
            int defaultRes) {
        if (sSourceInstance == null) {
            sSourceInstance = new ImageWorkWrapper(appContext, maxLruSize, defaultRes, true);
        }

        return sSourceInstance;
    }

    public void loadImage(Object picUrl, ImageView imageView) {
        mImageWorker.loadImage(picUrl, imageView);
    }

    public void loadImage(Object picUrl, ImageView imageView, Bitmap loadingBmp,
            ScaleType loadingScaleType) {
        mImageWorker.loadImage(picUrl, imageView, loadingBmp, loadingScaleType);
    }

    public void loadImageWithoutDefaultLoading(Object picUrl, ImageView imageView) {
        mImageWorker.loadImage(picUrl, imageView, false);
    }

    public void loadImage(Object picUrl, ImageView imageView, int loadingBmpResId,
            ScaleType loadingScaleType) {
        Bitmap loading = BitmapFactory.decodeResource(mContext.getResources(), loadingBmpResId);
        mImageWorker.loadImage(picUrl, imageView, loading, loadingScaleType);
    }
}
