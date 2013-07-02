package com.tudou.android.fw.util;

import android.content.Context;
import android.view.Window;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WindowUtil {
    private static final String TAG = WindowUtil.class.getSimpleName();
    
    private WindowUtil(){}// Utility class, do not instantiate.
    
    public static Window makeNewWindow(Context context) {
        Window w = null;
        try {
            Class<?> clazz;
            clazz = Class.forName("com.android.internal.policy.PolicyManager");
            Method method = clazz.getMethod("makeNewWindow", Context.class);
            w = (Window) method.invoke(clazz, context);
        } catch (ClassNotFoundException e) {
            TudouLog.e(TAG, "ClassNotFoundException", e);// ignore this, it's safe.
        } catch (SecurityException e) {
            TudouLog.e(TAG, "SecurityException", e);// ignore this, it's safe.
        } catch (NoSuchMethodException e) {
            TudouLog.e(TAG, "NoSuchMethodException", e);// ignore this, it's safe.
        } catch (IllegalArgumentException e){
            TudouLog.e(TAG, "IllegalArgumentException", e);// ignore this, it's safe.
        } catch (IllegalAccessException e) {
            TudouLog.e(TAG, "IllegalAccessException", e);// ignore this, it's safe.
        } catch (InvocationTargetException e) {
            TudouLog.e(TAG, "InvocationTargetException", e);// ignore this, it's safe.
        }

        return w;
    }
}
