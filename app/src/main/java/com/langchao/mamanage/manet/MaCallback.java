package com.langchao.mamanage.manet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by miaohl on 2016/6/25.
 */
public interface MaCallback {

    public interface ToLoginCallBack extends  MaCallback{
        void onSuccess(String userId,String realName);

        void onError(Throwable ex);


    }


    public interface MainInfoCallBack extends MaCallback{
        void onSuccess(JSONObject jsonObject) throws InterruptedException;

        void onError(Throwable ex);
    }

    public interface ArrayInfoCallBack extends MaCallback{
        void onSuccess(JSONArray jsonArray);

        void onError(Throwable ex);
    }
}
