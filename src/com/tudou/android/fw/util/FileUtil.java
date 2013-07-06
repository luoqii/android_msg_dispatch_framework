
package com.tudou.android.fw.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    private FileUtil(){}// Utility class, do not instantiate.
    
    /**
     * create file if it's parent is exixt, otherwise
     * create file's dir(s) and create this file.
     * 
     * @param file
     * @return
     */
    public static boolean createFile(File file) {
        boolean hasCreated = false;
        File p = file.getParentFile();
        if (!p.exists()) {
            hasCreated = p.mkdirs();
            if (hasCreated) {
                try {
                    file.createNewFile();
                    hasCreated = true;
                } catch (IOException e) {
                    Log.e(TAG, "IOException", e);
                }
            }
        } else {
            try {
                file.createNewFile();
                hasCreated = true;
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            }
        }

        return hasCreated;
    }
    
    public static boolean createFile(String path) {
        return createFile(new File(path));
    }
    
    public static File create(String path){
        File f = null;
        f = new File(path);
        if (!createFile(f)) {
            f = null;
        }
        return f;
    }

    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
