
package com.tudou.android.fw.compatibility.v4;

import com.tudou.android.fw.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author bysong@tudou.com
 *
 */
public class StrictModeCompat {
    private static String TAG = StrictModeCompat.class.getSimpleName();

    /**
     * 
     */
    public static void enableStrictMode() {       
    	try {
			Class<?> strictModeClass = Class.forName("android.os.StrictMode",
					true, Thread.currentThread().getContextClassLoader());
			Class<?> threadPolicyClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy", true, Thread
							.currentThread().getContextClassLoader());
			Class<?> threadPolicyBuilderClass = Class.forName(
					"android.os.StrictMode$ThreadPolicy$Builder", true, Thread
							.currentThread().getContextClassLoader());
			Method setThreadPolicyMethod = strictModeClass.getMethod(
					"setThreadPolicy", threadPolicyClass);
			
			Method detectAllMethod = threadPolicyBuilderClass
					.getMethod("detectAll");
			Method penaltyMethod = threadPolicyBuilderClass
					.getMethod("penaltyLog");
			
			Method buildMethod = threadPolicyBuilderClass.getMethod("build");
			Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass
					.getConstructor();
			Object threadPolicyBuilderObject = threadPolicyBuilderConstructor
					.newInstance();
			Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);
			obj = penaltyMethod.invoke(obj);
			penaltyMethod = threadPolicyBuilderClass.getMethod("penaltyFlashScreen");
			obj = penaltyMethod.invoke(obj);
			Object threadPolicyObject = buildMethod.invoke(obj);
			setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);
		} catch (Exception ex) {
		    // just log this.
			Log.w(TAG , "Exception StrictModeCompat", ex);
		}
    }
}
