package com.wave.library.util;

import android.text.TextUtils;

/**
 * the string tool
 * Created by cherish on 2016/4/18.
 */
public class StringUtil {

    /**
     * string to string[]
     * @param str string
     * @return string[]
     */
    public static String[] toStringArray(String str){
        String[] strArray;
        if(TextUtils.isEmpty(str)){
            strArray =  new String[]{};
        }else{
            strArray =  new String[str.length()];
            for(int i = 0; i < str.length(); i ++){
                strArray[i] = str.substring(i, i + 1);
            }
            strArray[str.length()- 1] = str.substring(str.length()- 1);
        }

        return strArray;
     }

 }
