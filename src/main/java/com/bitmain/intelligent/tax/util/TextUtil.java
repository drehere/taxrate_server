package com.bitmain.intelligent.tax.util;

public class TextUtil {
    public static boolean isEmpty(String text){
        if(text==null||"".equals(text.toString())){
            return true;
        }
        return false;
    }
    public static boolean isNotEmpty(String text){
        return !isEmpty(text);
    }
}
