package com.tudou.android.fw.util;

import java.io.IOException;
import java.util.Random;

public class MockUtil {
    private static final String TAG = MockUtil.class.getSimpleName();
    
    private MockUtil() {};// Utility class, do not instantiate.
    
    static public void mockDelay(int sleepTime){
        mockDelay(true, sleepTime);
    }
    /**
     * 
     */
    static public void mockDelay(boolean delay, int sleepTime) {
        if (delay) {
            TudouLog.i(TAG, "mock network delay.");
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                TudouLog.e(TAG, "InterruptedException", e); //ignore
            }
        }
    }

    static public void mockIOE(int sleepTime) throws IOException{
        mockIOE(true, sleepTime);
    }
    /**
     * @throws IOException
     */
    static public void mockIOE(boolean ioe, int sleepTime) throws IOException {
        if (ioe) {
            TudouLog.i(TAG, "mock network exception.");
            if (new Random().nextInt(100) < 50) { // exception rate
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    TudouLog.e(TAG, "InterruptedException", e); //ignore
                }

                throw new IOException("mock IOException.");
            }
        }
    }
}
