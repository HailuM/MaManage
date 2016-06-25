package com.langchao.mamanage.manet;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_b;

import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by miaohl on 2016/6/24.
 */
public class TestNet {

    public void TestToLogin(){
        final String userName = "jp";
        String pwd = "aaaaaa";
        NetUtils.ToLogin(userName, pwd, new MaCallback.ToLoginCallBack() {
            @Override
            public void onSuccess(String userId) {
                Toast.makeText(x.app(),  userId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(x.app(),  ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 测试下载入库
     */
    public void TestReceiveInfo(){

        final String userOID= "00096b9f-0000-0000-0000-0000c17d1036";

        NetUtils.Mobile_downloadReceiveInfo(userOID, new MaCallback.MainInfoCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                //token 需要存到数据库 上传使用
                String tokenStr = jsonObject.getString("tokenStr");

                Toast.makeText(x.app(),"RKTOKEN:"+ tokenStr, Toast.LENGTH_LONG).show();

                JSONArray orderArray =  jsonObject.getJSONArray("details");
                if(orderArray.size() > 0){
                    for(int i = 0 ; i < orderArray.size() ; i++)
                    {
                        JSONObject order = (JSONObject) orderArray.get(i);


//                        Pu_order pu_order = new Pu_order();
//                        pu_order.setId(order.getString("id"));
//                        pu_order.setMaterialDesc("materialDesc");
//                        pu_order.setNumber(order.getString("number"));
//                        pu_order.setAddr(order.getString("Addr"));
//                        pu_order.setSupplier(order.getString("order"));

                        final Ic_inbill ic_inbill = JSON.parseObject(order.toJSONString(),Ic_inbill.class);

                        //查询表体物料
                        NetUtils.Mobile_DownloadReceiveMaterial(userOID,ic_inbill.getId(),tokenStr,new MaCallback.ArrayInfoCallBack(){
                            @Override
                            public void onSuccess(JSONArray jsonArray) {

                                List<Ic_inbill_b> list =  JSON.parseArray(jsonArray.toJSONString(), Ic_inbill_b.class);

                                Toast.makeText(x.app(), list.size()+"明细", Toast.LENGTH_LONG).show();


                                //save to db
                               // new MaDAO().save(ic_inbill,list);
                            }

                            @Override
                            public void onError(Throwable ex) {
                                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                        //查询领料商
                        NetUtils.Mobile_DownloadReceiveconsumer(userOID,ic_inbill.getId(),tokenStr,new MaCallback.ArrayInfoCallBack(){
                            @Override
                            public void onSuccess(JSONArray jsonArray) {

                                List<Consumer> consumerList  =  JSON.parseArray(jsonArray.toJSONString(), Consumer.class);

                                Toast.makeText(x.app(), consumerList.size()+"领料商明细", Toast.LENGTH_LONG).show();


                                //save to db
                                try {
                                    new MaDAO().save(consumerList);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex) {
                                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
