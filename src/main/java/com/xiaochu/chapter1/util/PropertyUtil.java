package com.xiaochu.chapter1.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by DongChao on 2016/6/25.
 */
public final class PropertyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);

    /**
     *加载属性文件
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName){
        Properties properties = null ;
        InputStream inputStream = null ;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream==null){
                throw new FileNotFoundException(fileName+" is not found!");
            }
            properties = new Properties();
            properties.load(inputStream);
        }catch (Exception e){
            LOGGER.error("load property file failure!",e);
         }finally {
            /*
                关闭输入流
             */
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch (IOException e){
                    LOGGER.error("close inputStream failure",e);
                }
            }
        }
        return properties ;
    }

    /**
     * 获取字符型属性，默认为空字符串
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }

    private static String getString(Properties properties, String key, String defaultValue) {
      String value = defaultValue ;
        if (properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value ;
    }

    /**
     * 获取数值型属性，默认值为0
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties,String key){
      return getInt(properties,key,0);
    }

    private static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue ;
        if (properties.containsKey(key)){
            value = Integer.parseInt(properties.getProperty(key));
        }
        return value;
    }
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }
    private static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
         boolean value = defaultValue ;
         if (properties.containsKey(key)){
            value = Boolean.parseBoolean(properties.getProperty(key));
         }
        return value ;
    }


}
