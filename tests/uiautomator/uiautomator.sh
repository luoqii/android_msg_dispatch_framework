#! /bin/bash
adb shell am start -a com.tudou.android.fw.ACTION_DEMO_SENDER_RECEIVE
ant build;
adb push bin/test.jar /data/local/tmp/;
adb shell uiautomator runtest test.jar -c com.tudou.android.fw.test.SenderReceiverPageTestCast 
