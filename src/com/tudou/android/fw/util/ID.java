package com.tudou.android.fw.util;

public class ID {
    private static int sId;

    public static long nextId() {
        return sId++;
    }
    
    private ID(){}// Utility class, do not instantiate.
}
