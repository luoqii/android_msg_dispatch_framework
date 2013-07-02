package com.tudou.android.fw.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.tudou.android.fw.util.DeviceUtil;
import com.tudou.android.fw.util.FileUtil;
import com.tudou.android.fw.util.TudouLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    
    private static final boolean DEBUG = true;
    
    public static final String EXCEPTION_FILE_PREFIX = "fc";
    public static final String EXCEPTION_FILE_SUFFIX = "txt";

    protected FileHierachySpec mSpec;
    private static App sInstance;
    protected List<ComponentCloseable> mComponents;
  
    @Override
    public void onCreate() {
        super.onCreate();
        
        sInstance = this;
        mComponents = new ArrayList<ComponentCloseable>();
        initFileHierachy();
        onPostInitFileHierachy(mSpec);
    }

    private void onPostInitFileHierachy(FileHierachySpec mSpec2) {
        
    }

    public static App getInstance() {
        return sInstance;
    }
    
    private void initFileHierachy() {
        String path = getExternalFilePath();
        mSpec = FileHierachySpec.getInstance();
        mSpec.setExtDirPath(path);
        mSpec.setExtCacheDirPath(this.getCacheDir().toString() + "/cache");

        // TODO refactor this. bysong@tudou.com
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        getApplicationContext().registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                    String path = getExternalFilePath();
                    mSpec.setExtDirPath(path);
                }
            }
        }, filter);
    }
    
    @TargetApi(Build.VERSION_CODES.FROYO)
    private String getExternalFilePath() {
        // default dir.
        String pName = getPackageName();
        String self = pName.substring(pName.lastIndexOf(".") + 1);
        String path = getFilesDir().toString();
        boolean sdcardAvaiable = FileUtil.isSDCardMounted();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
                && sdcardAvaiable) {
            path = getExternalFilesDir(null).toString();
        } else if (sdcardAvaiable) {
            path = Environment.getExternalStorageDirectory()
                    + "/tudou/android/" + self;
        }
        return path;
    }
    
    /**
     * @return log tag
     */
    public String getLogTag(){
        return "libproject";
    }

    public void closeApplication() {
        for (ComponentCloseable c : mComponents){
            //TudouLog.d(TAG, "close component: " + c);
            c.close();
        }
        
        TudouLog.i(TAG, "bye worold.");
        System.exit(0);
    }

    public void reginsterComponentCallback(ComponentCloseable c) {
        mComponents.add(c);
    }

    public void unRegisterComponentCallback(ComponentCloseable c) {
        mComponents.remove(c);
    }

    abstract public boolean isReleaseMode();

    abstract public boolean isMonkeyMode();

    protected void npe() {
        String npe = null;
        if (npe.length() == 0) {
            TudouLog.e(TAG, "dead code.");
        }
    }

    /**
     * inject a log handler to log this exception,and call original handler;
     */
    protected void injectLogDefaultUncaughtExceptionHandler(File exceptionDir) {
        final UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        final File dir = exceptionDir;
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
//                TudouLog.e(TAG, "uncaughtException thread: " + thread, ex);

                String stem = DateFormat.format("yyyy_MM_dd_hh_mm_ss", new Date()).toString();
                String name = EXCEPTION_FILE_PREFIX + "_" + stem + "." + EXCEPTION_FILE_SUFFIX;
                
                File crashFile = new File(dir, name);
                if (crashFile.exists()) {
                    crashFile.delete();
                }
                FileUtil.createFile(crashFile);
                
                try {
                    PrintStream pStream = new PrintStream(crashFile);
                    
                    pStream.println("date: " + stem);
                    pStream.println(DeviceUtil.fingerPrint());
                    
                    String packageName = getPackageName();
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName,
                            0);
                    pStream.println("app versionCode:" + packageInfo.versionCode + " versionName: " + packageInfo.versionName);
                    String scmInfo = getSCMInfo();
                    if (TextUtils.isEmpty(scmInfo)) {
                        pStream.println(scmInfo);
                    }
                    pStream.println();
                    
                    ex.printStackTrace(pStream);    
                    TudouLog.d(TAG, "fc file saved to: " + crashFile);
                    
                } catch (FileNotFoundException e) {
                    TudouLog.e(TAG, "FileNotFoundException", e);
                } catch (NameNotFoundException e) {
                    TudouLog.e(TAG, "NameNotFoundException", e);
                }
              
              // do this at last.
              defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }
    
    /**
     * @return scn info to log to fc file.
     */
    protected String getSCMInfo(){
        return "";
    }

}
