package com.dc.p6_dc;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danet on 4/7/2017.
 */
public class XmlParser {

    final String ns = null;

    public List<char[]> parseIntoMap(InputStream iStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(iStream, null);
            parser.next();
            parser.next();
            return readRows(parser);
        } finally {
            iStream.close();
        }
    }

    public List readRows(XmlPullParser parser) throws IOException, XmlPullParserException {
        List rows = new ArrayList();
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            rows.add(readRow(parser));
        }
        return rows;
    }

    private char[] readRow(XmlPullParser parser) throws IOException, XmlPullParserException {
        String row = parser.nextText();
        parser.next();
        return row.toCharArray();
    }
}

