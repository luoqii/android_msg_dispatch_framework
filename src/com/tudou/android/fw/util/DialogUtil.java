package com.tudou.android.fw.util;

public class DialogUtil {
    private static int sId = 0;

    public static int nextDialogId() {
        return sId++;
    }
}
