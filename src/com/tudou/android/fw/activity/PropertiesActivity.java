package com.tudou.android.fw.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tudou.android.fw.util.ToastUtil;
import com.tudou.android.fw.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesActivity extends DebugActivity {  
    private static final String TAG = PropertiesActivity.class.getSimpleName();
    private static final boolean DEBUG = false;
    
    public static final String EXTRA_PROPERTY_FILE = "com.tuodu.android.PROPERTY_FILE";
    public static final String EXTRA_TEMPLATE = "com.tuodu.android.TEMPLATE";
    
    private static final String DEFAULT_PROPERTY_FILE = "default.properties";
    
    private EditText mContent;
    private String mFileName;
    private String mTemplate;
    
    public static Properties getDefaultProperties(Context context) {
        Properties p = null;
        if (!DEBUG) {
            p = new Properties();
        } else {
            p = new TrackingProperties();
        }
        
        try {
            p.load(new FileInputStream(context.getFileStreamPath(DEFAULT_PROPERTY_FILE).toString()));
        }  catch (FileNotFoundException e) {
            // silent is golden
            //TudouLog.w(TAG, "FileNotFoundException");
        } catch (IOException e) {
            // silent is golden
            //TudouLog.w(TAG, "IOException");
        }
        
        return p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContent = new EditText(this);
        LayoutParams p = new FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.FILL_PARENT, android.widget.FrameLayout.LayoutParams.FILL_PARENT);
        setContentView(mContent, p);
        
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent) {
        mFileName = intent.getStringExtra(EXTRA_PROPERTY_FILE);
        mTemplate = intent.getStringExtra(EXTRA_TEMPLATE);
        
        Log.e(TAG, "properties file: " + mFileName);
        
        if (TextUtils.isEmpty(mFileName)) {
            mFileName = getFileStreamPath(DEFAULT_PROPERTY_FILE).toString();
            Log.w(TAG, "no property file. use default: " +  mFileName);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        String properties = readFrom(mFileName);
        if (TextUtils.isEmpty(properties) && !TextUtils.isEmpty(mTemplate)) {
            properties = mTemplate;
        } else {
            properties += "\n\n" + mTemplate;
        }
        mContent.setText(properties);
    }

    private String readFrom(String file) {
        String content = "";
        try {
            InputStream in = new FileInputStream(file);
            Properties p = new Properties();
            p.load(in);

            Set<Entry<Object, Object>> entrySet = p.entrySet();
            for (Entry e: entrySet) {
                content += e.getKey() + " = " + e.getValue() + "\n";
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        
        return content;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        String properties = mContent.getText().toString();
        saveTo(mFileName, properties);        
    }

    private void saveTo(String file, String properties) {
        try {
            Writer w  = new OutputStreamWriter(new FileOutputStream(file));
            w.write(properties, 0, properties.length());
            w.flush();
            ToastUtil.toast(this, "properties saved", Toast.LENGTH_LONG);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }    
    }
    
    protected String getTag() {
        return null;
    }

    static class TrackingProperties extends Properties {
        private static final String TAG = TrackingProperties.class.getSimpleName();
        
        public TrackingProperties() {
            super();
        }

        public TrackingProperties(Properties properties) {
            super(properties);
        }

        @Override
        public String getProperty(String name) {
            String p = super.getProperty(name);
            Log.d(TAG, "getProperty(), name: " + name + " property: " + p);
            return p;
        }
        
        @Override
        public String getProperty(String name, String defaultValue) {
            String p =  super.getProperty(name, defaultValue);
            
            return p;
        }
    }
}
