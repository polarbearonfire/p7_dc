package com.dc.p6_dc;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by danet on 4/7/2017.
 */
public class XmlFetcher {
    public Context mContext;

    public XmlFetcher(Context context) {
        mContext = context;
    }

    public String getXmlString(String path) {
        return new String(getXmlBytes(path));
    }

    private byte[] getXmlBytes(String path) {

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            InputStream inStream = mContext.getAssets().open(path);

            byte[] buff = new byte[1024];
            int bytes = inStream.read(buff);
            while (bytes > 0) {
                outStream.write(buff, 0, bytes);
                bytes = inStream.read(buff);
            }
            outStream.close();
            return outStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


}
