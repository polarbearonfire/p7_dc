package com.dc.p6_dc;

import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danet on 4/7/2017.
 */
public class XmlManager {
    int level;
    XmlFetcher fetcher;
    XmlParser parser;
    Context mContext;

    public XmlManager(Context context) {
        mContext = context;
        level = 0;
        fetcher = new XmlFetcher(context);
        parser = new XmlParser();
    }

    public List<char[]> getNextLevel() {
        level++;
        return getLevel();
    }

    public List<char[]> getLastLevel() {
        level--;
        return getLevel();
    }

    private List<char[]> getLevel() {
        try {
            String xml = fetcher.getXmlString("level" + level + ".xml");
            List<char[]> level = parser.parseIntoMap(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            return level;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
