
package com.tudou.android.fw.model.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;

import com.tudou.android.fw.application.FileHierachySpec;
import com.tudou.android.fw.model.ambassador.IAmbassador;
import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.impl.TudouHttpClient;
import com.tudou.android.fw.model.cache.imagecache.FileCache;
import com.tudou.android.fw.model.cache.imagecache.IImageCache;
import com.tudou.android.fw.model.cache.imagecache.IImageCache.ICacheSpec;
import com.tudou.android.fw.model.cache.imagecache.MeasureCache;
import com.tudou.android.fw.model.task.AbsTask;
import com.tudou.android.fw.util.TudouLog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultImageAmbassador implements IAmbassador {
    private static final String TAG = DefaultImageAmbassador.class.getSimpleName();
    
    private static final boolean LOG = true;
    private static final boolean DEBUG_MEASURE = false;
    
    private static DefaultImageAmbassador sInstance;
    private ThreadPoolExecutor mExecutor;

    private IImageCache mCache;

    private CacheHandlerThread mCacheChecker;

    private Handler mHandler;


    private DefaultImageAmbassador() {
        mExecutor = new ThreadPoolExecutor(4, 4, Long.MAX_VALUE, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        
        mCache = new FileCache(new File(FileHierachySpec.getInstance().getExtCacheDirPath()));          
        if (DEBUG_MEASURE) {
            mCache = new MeasureCache(mCache, "diskCache");
        }
        
        mCacheChecker = new CacheHandlerThread("CacheHandler");
        mCacheChecker.start();
        mHandler = new Handler(mCacheChecker.getLooper(), new Callback() {
            
            @Override
            public boolean handleMessage(Message msg) {
                int what = msg.what;
                switch (what) {
                    case CacheHandlerThread.MSG_QUERY_CACHE:
                        performQuery(msg);
                        break;
                    case CacheHandlerThread.MSG_INSERT_CACHE:
                        performInsert(msg);
                        break;
                }
                return true;
            }

            /**
             * @param msg
             */
            protected void performInsert(Message msg) {
                ICacheSpec spec;
                spec = (ICacheSpec) ((Object[])msg.obj)[0];
                byte[] cache = (byte[]) ((Object[])msg.obj)[1];
                try {                    
                    mCache.insert(spec, cache);
                } catch (IOException e) {
                    TudouLog.e(TAG, "IOException", e);
                }
            }

            /**
             * @param msg
             */
            protected void performQuery(Message msg) {
                ICacheSpec spec;
                IRequest req = (IRequest)((Object[])msg.obj)[0];
                IHandler handler = (IHandler)((Object[])msg.obj)[1];
                Object data = req.getData();
                IImageReqEntity  imageReq = (IImageReqEntity) data;
                
                String digest = imageReq.getPicUrlDigest();
                spec = new ImageSpec(imageReq);
                Bitmap bitmap = null;
                try {

                    byte[] cache = null;
                    Bitmap b = null;
                    if (mCache.has(spec)) {
                        cache = mCache.get(spec);

                        bitmap = BitmapFactory.decodeByteArray(cache, 0, cache.length);
                        
                        ImageResEntity entity = null;
                        entity = new ImageResEntity();
                        entity.setBitmap(bitmap);
                        entity.setUrl(imageReq.getPicUrl());
                        entity.setUrlDigest(digest);
                        handler.sendResponseMessage(new ImageResponse(req, entity));
                    } else {
                        mExecutor.execute(new ImageTask(req, handler));
                    }
                } catch (IOException e) {
                    TudouLog.e(TAG, "IOException", e);
                }
            }
        });
    }

    public static DefaultImageAmbassador getInstance() {
        if (null == sInstance) {
            sInstance = new DefaultImageAmbassador();
        }

        return sInstance;
    }

    @Override
    public long scheduleRequest(IRequest req, IHandler handler) {

        // delegate to mCacheHandlerThread do it firstly.
//        Message msg = mHandler.obtainMessage(mCacheChecker.MSG_QUERY_CACHE);
//        Object[] para = new Object[2];
//        para[0] = req;
//        para[1] = handler;
//        msg.obj = para;
//        mHandler.sendMessage(msg);

         mExecutor.execute(new ImageTask(req, handler));
        return req.getId();
    }

    @Override
    public void unScheduleRequest(long schduleId) {

    }
    
    public class CacheHandlerThread extends HandlerThread {
        public static final int MSG_QUERY_CACHE = 1;
        public static final int MSG_INSERT_CACHE = 2;

        public CacheHandlerThread(String name, int priority) {
            super(name, priority);
        }

        public CacheHandlerThread(String name) {
            super(name);
        }
        
    }

    public class ImageTask extends AbsTask {
        private static final boolean LOG = false;

        private byte[] mBuffer = new byte[1024];
        private String TAG = this.getClass().getSimpleName();
        

        public ImageTask(IRequest req, IHandler mUiHandler) {
            super(req, mUiHandler);
        }

        public byte[] getBuffer() {
            return mBuffer;
        }

        @Override
        protected void onTask() {
            try {
                Object data = getReqest().getData();
                IImageReqEntity  req = (IImageReqEntity) data;

                String digest = req.getPicUrlDigest();
                ICacheSpec spec = new ImageSpec(req);
                Bitmap bitmap = null;

                String url = req.getPicUrl();
                byte[] bytes = null;
                if (LOG) {
                    TudouLog.d(TAG, "url: " + url);
                }            
                bytes = getImage(url);      
                
                Message msg = new Message();
                msg.what = CacheHandlerThread.MSG_INSERT_CACHE;
                Object[] obj = new Object[2];
                obj[0] = spec;
                obj[1] = bytes;
                msg.obj = obj;
                DefaultImageAmbassador.this.mHandler.sendMessage(msg);
                
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                ImageResEntity entity = null;
                entity = new ImageResEntity();
                entity.setBitmap(bitmap);
                entity.setUrl(url);
                entity.setUrlDigest(digest);
                
               mHandler.sendResponseMessage(new ImageResponse(getReqest(), entity));
            } catch (IOException e) {
                // FIXME notify ui this situation
                TudouLog.e(TAG, "IOException", e);
            } catch (IllegalStateException e) {
                // FIXME notify ui this situation
                TudouLog.e(TAG, "IllegalStateException", e); 
            }            
        }

        private  byte[] getImage(String url) throws IOException,
                IllegalStateException {
            HttpClient connection = TudouHttpClient.getHttpClient();
            HttpUriRequest request = new HttpGet(url);
            HttpResponse httpResponse = connection.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
                    httpEntity);
            InputStream in = bufferedHttpEntity.getContent();
            byte[] imageByteArray = readInputStream(in);
            httpEntity.consumeContent();
            request.abort();

            return imageByteArray;
        }

        private byte[] readInputStream(InputStream inStream) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inStream.read(mBuffer)) != -1) {
                outputStream.write(mBuffer, 0, len);
            }
            inStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        }
    }
    
    public class ImageSpec implements IImageCache.ICacheSpec {

        IImageReqEntity mReq;
        
        ImageSpec(IImageReqEntity req) {
            mReq = req;
        }
        
        @Override
        public String getUrlDigest() {
            return mReq.getPicUrlDigest();
        }

        @Override
        public String getCacheFileNameSufix() {
            return ".cache";
        }
        
        @Override
        public String toString() {
            return "url: " + mReq.getPicUrl();
        }
    }

}
