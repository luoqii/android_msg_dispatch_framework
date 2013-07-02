package com.tudou.android.fw.test;

import android.os.RemoteException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SenderReceiverPageTestCast extends UiAutomatorTestCase {
    private UiDevice mDevice;
    
    @Override
    public void setName(String name) {
        mDevice = getUiDevice();
        
        super.setName(name);
    }

    public void testSenderReceiver() throws UiObjectNotFoundException {
        
        assertTextHasReceivedCorrectly();
    }
    
    public void testSenderReceiverOrientation() throws UiObjectNotFoundException, RemoteException {
        mDevice.setOrientationLeft();
        assertTextHasReceivedCorrectly();
        
        mDevice.setOrientationRight();
        assertTextHasReceivedCorrectly();
        
        mDevice.setOrientationNatural();
        assertTextHasReceivedCorrectly();
    }    

    private void assertTextHasReceivedCorrectly() throws UiObjectNotFoundException {
        UiObject sendTextV = new UiObject(new UiSelector().description("send text"));
        UiObject sendButton = new UiObject(new UiSelector().description("send button"));
 
        String sendText = "android";
        
        sendTextV.setText(sendText);
        if (!"com.tudou.android.fw".equals(mDevice.getCurrentPackageName())) {
            mDevice.pressBack();
        }
        sendButton.click();
        
        mDevice.waitForIdle(1* 1000);
        
        UiObject receivedTextV = new UiObject(new UiSelector().description("received text"));
        String receivedText = receivedTextV.getText();
        
        assertEquals(sendText, receivedText);
    }
}
