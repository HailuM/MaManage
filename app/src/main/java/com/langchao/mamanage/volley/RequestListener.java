package com.langchao.mamanage.volley;

import com.android.volley.VolleyError;

/**
 * 
 * Created by Administrator on 2015/3/11.
 */
public interface RequestListener  {

    /** 成功 */
    public void requestSuccess(String json);

    /** 错误 */
    public void requestError(VolleyError e);
}
