package com.zx.play.utils;

import java.util.ResourceBundle;

/**
 * @author zx
 * @create 2022/9/11 15:37
 */
public class PropertiesUtil {
    private static ResourceBundle bundle = ResourceBundle.getBundle("config");

    public static String getValue(String name){
        return bundle.getString(name);
    }


}
