package com.bk.utils;

import android.app.Activity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: salman
 * Date: 5/12/13
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesReader {
    private static Properties ppt;
    public final static String CONFIG_FILE_NAME = "config.properties";

    public static String getProperty(String name, String defaultV) {
        if (ppt == null) {
            ppt = new Properties();
            try {
                InputStream stream = PropertiesReader.class.getClassLoader().getResourceAsStream("assets/"+CONFIG_FILE_NAME);
                ppt.load(stream);
                stream.close();
                System.out.println("Successfully loaded property file "+CONFIG_FILE_NAME);
            } catch (Throwable k) {
                k.printStackTrace();
            }
        }
        String result = ppt.getProperty(name, defaultV.toString());
        return result != null ? result : defaultV;
    }
}
