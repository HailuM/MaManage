package com.langchao.mamanage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class MethodUtil {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;


    /**
     * @param content, String key, String spkey
     * @throws
     * @Description: 获得SharedPreferences里面的值
     * @author fei
     * @date 2016/5/23 14:47
     */
    public static String getSharedPreferences(Context content, String key, String spkey) {
        sp = content.getSharedPreferences(spkey, 0);
        String result = "";
        if (sp.contains(key)) {
            result = sp.getString(key, "");
        }
        return result;
    }

    /**
     * @throws
     * @Description: 保存到SharedPreferences
     * @paramContext content, String spkey, Map<String, Object> map
     * @author fei
     * @date 2016/5/23 15:09
     */
    public static void commitEditor(Context content, String spkey, Map<String, Object> map) {
        sp = content.getSharedPreferences(spkey, 0);
        editor = sp.edit();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    /**
     * @throws
     * @Description:删除editor
     * @author fei
     * @date 2016/6/13 17:20
     */
    public static void removeEditor(Context content, String spkey, Map<String, Object> map) {
        sp = content.getSharedPreferences(spkey, 0);
        editor = sp.edit();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            editor.remove(value.toString());
        }
        editor.commit();
    }
}
