
package com.tudou.android.fw.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.android.bitmapfun.util.ImageResizer;
import com.example.android.bitmapfun.util.ImageWorker;
import com.example.android.bitmapfun.util.Utils;
import com.tudou.android.fw.util.TudouLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TudouImageWorker extends ImageWorker {
    private static final String TAG = TudouImageWorker.class.getSimpleName();
    private static final boolean DEBUG = false;

    public TudouImageWorker(Context context) {
        super(context);
    }

    @Override
    protected Bitmap processBitmap(Object imgUrl, Object imagview) {
        String urlString = (String) imgUrl;
        ImageView iv = (ImageView) imagview;
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        if (DEBUG) {
            TudouLog.d(TAG, "process bitmap. url: " + urlString);
        }
        Bitmap bitmap = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5 * 1000);
            urlConnection.setReadTimeout(2 * 1000);
            final BufferedInputStream in =
                    new BufferedInputStream(urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] data = swapStream.toByteArray(); // in_b为转换之后的结果
            bitmap = ImageResizer.decodeSampledBitmapFromByteArray(data, iv.getWidth(), iv.getHeight());

        } catch (final IOException e) {// XXX final ??? bysong@tudou.com
            bitmap = null;
            TudouLog.e(TAG, "IOException in downloadBitmap(" + urlString + ") - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    TudouLog.e(TAG, "IOException in downloadBitmap(" + urlString + ") - " + e);
                }
            }
        }
        if (DEBUG) {
            TudouLog.d(TAG, "rcvd bitmap: " + bitmap + "(" + urlString + ")");
        }
        return bitmap;
    }
}
