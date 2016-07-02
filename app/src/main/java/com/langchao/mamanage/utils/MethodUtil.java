package com.langchao.mamanage.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.converter.MaConvert;

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


        Integer no = content.getSharedPreferences(MaConstants.FILENAME, 0).getInt("NO_"+type,0);


        no = no + 1;
        String newno = MaConvert.getDate() + "-"+no.toString()+getNano4();

        SharedPreferences.Editor editor = content.getSharedPreferences(MaConstants.FILENAME, 0).edit();
        // 存入键值对
        editor.putInt("NO_"+type, no);


        // 将内存中的数据写到XML文件中去
        editor.commit();

        return newno;
    }

    public static String getNano4(){
        String nano = System.nanoTime()+"";
        return  nano.substring(nano.length()-5,nano.length());
    }

    public static void saveRkToken(Context content, String token){
        SharedPreferences.Editor editor = content.getSharedPreferences(MaConstants.FILENAME, 0).edit();
        // 存入键值对
        editor.putString("ordertoken", token);


        // 将内存中的数据写到XML文件中去
        editor.commit();
    }

    public static void saveCkToken(Context content, String token){
        SharedPreferences.Editor editor = content.getSharedPreferences(MaConstants.FILENAME, 0).edit();
        // 存入键值对
        editor.putString("inbilltoken", token);


        // 将内存中的数据写到XML文件中去
        editor.commit();
    }

    public static String getRkToken(Context content){
        return  content.getSharedPreferences(MaConstants.FILENAME, 0).getString("ordertoken" ,"");

    }

    public static String getCkToken(Context content){
        return  content.getSharedPreferences(MaConstants.FILENAME, 0).getString("inbilltoken" ,"");

    }

    public static void savePrintMac(Context content, String mac){
        SharedPreferences.Editor editor = content.getSharedPreferences(MaConstants.FILENAME, 0).edit();
        // 存入键值对
        editor.putString("printmac", mac);


        // 将内存中的数据写到XML文件中去
        editor.commit();
    }

    public static String getPrintMac(Context content ){
        return  content.getSharedPreferences(MaConstants.FILENAME, 0).getString("printmac" ,"");
    }
}
