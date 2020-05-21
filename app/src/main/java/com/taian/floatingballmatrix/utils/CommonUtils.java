package com.taian.floatingballmatrix.utils;
/*
 Created by baotaian on 2020/5/21 0021.
*/


public class CommonUtils {

    public static boolean isHex(String s){
        String regex="^[A-Fa-f0-9]+$";
        return s.matches(regex);
    }
}
