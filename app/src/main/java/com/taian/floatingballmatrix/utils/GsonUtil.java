package com.taian.floatingballmatrix.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baotaian on 2016/8/22.
 * 联网请求
 */
public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    public static <T> T fromJson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            if (gson != null) {
                t = gson.fromJson(jsonString, cls);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("TAG", "fromJson: " + e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    public static String toJson(Object obj) {
        String str = "";
        try {
            if (gson != null) {
                str = gson.toJson(obj);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return str;
    }

    public static <T> List<T> getList(String strJson, Class<T> cls) {
        List<T> datalist = new ArrayList<T>();
        if (null == strJson) {
            return datalist;
        }
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                datalist.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datalist;
    }
}
