package com.tudou.android.fw.compatibility.v4;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.ArrayAdapter;

import java.util.Collection;
import java.util.Iterator;

public class ArrayAdapterCompat {
    private static final String TAG = ArrayAdapterCompat.class.getSimpleName();
    
    @SuppressLint("NewApi")
    public static void addALL(ArrayAdapter adapter, Collection data){
        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT){
            adapter.addAll(data);
        } else {
            Iterator iterator = data.iterator();
            if (null == iterator) return;
            while (iterator.hasNext()) {
                adapter.add(iterator.next());
            }
        }
    }
}
