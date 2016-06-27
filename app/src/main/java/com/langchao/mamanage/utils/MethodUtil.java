package com.langchao.mamanage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.langchao.mamanage.common.MaConstants;

import java.util.Date;
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

    /**
     * 生成一个单号
     * @return
     */
    public static String  generateNo(Context content,String type){
        Date date = new Date(System.currentTimeMillis());
        Integer year = date.getYear();
        Integer month = date.getMonth();
        Integer day = date.getDay();

        Integer no = content.getSharedPreferences(MaConstants.FILENAME, 0).getInt("NO_"+type,0);


        no = no + 1;
        String newno = year.toString() + month.toString() + day.toString() + "-"+no.toString();

        SharedPreferences.Editor editor = content.getSharedPreferences(MaConstants.FILENAME, 0).edit();
        // 存入键值对
        editor.putInt("NO_"+type, no);


        // 将内存中的数据写到XML文件中去
        editor.commit();

        return newno;
    }
}
