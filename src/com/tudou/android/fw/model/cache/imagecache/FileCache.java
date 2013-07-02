
package com.tudou.android.fw.model.cache.imagecache;

import com.tudou.android.fw.util.TudouLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCache implements IImageCache {
    private static final String TAG = FileCache.class.getSimpleName();
    private File mCacheDir;

    public FileCache(File cacheDir) {
        mCacheDir = cacheDir;
    }

    @Override
    public byte[] get(ICacheSpec spec) throws IOException {
        File destFile = newFile(spec);
        
        FileInputStream inputStream = new FileInputStream(destFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        
        inputStream.close();
        outputStream.close();
        
        return outputStream.toByteArray();
    }

    @Override
    public boolean insert(ICacheSpec spec, byte[] data) throws IOException {
        File destFile = newFile(spec);
        if (!destFile.exists()) {
            if (!create(destFile)){
                
                TudouLog.w(TAG, "create file failed. file: " + destFile);
                return false;
            }
        }
        FileOutputStream outputStream = new FileOutputStream(destFile);
        outputStream.write(data);
        outputStream.close();
        
        return true;
    }

    @Override
    public boolean has(ICacheSpec spec) throws IOException {
        return newFile(spec).exists();
    }
    
    private boolean create(File file) throws IOException {
        boolean created = false;
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                created = dir.mkdirs();
                if (!created) {
                    return false;
                }
            }
            
            created = file.createNewFile();
        }
        
        return created;
    }

    private File newFile(ICacheSpec spec) throws IOException {
        String path = toName(spec.getUrlDigest());
        String cacheFileName = mCacheDir + "/" + 
                            path +
                            spec.getCacheFileNameSufix();
        
        File file = new File(cacheFileName);

        return file;
    }
    
    private String toName(String digest) {
        String name = "";
        name += digest.substring(0, 2) + "/" + digest.substring(2, 4) +  "/" + digest.substring(4);
        
        return name;
    }
}
