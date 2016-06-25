package com.langchao.mamanage.db;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.manet.MaCallback;
import com.langchao.mamanage.manet.NetUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by miaohl on 2016/6/25.
 */
public class MaDAO {

    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("manage.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            // .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

    public void save(Pu_order pu_order, List<Pu_order_b> pu_order_b) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(pu_order);
        db.save(pu_order_b);
    }

    public void save(List<Consumer> consumerList) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(consumerList);

    }

    public List<Pu_order> queryPuOrder(String orderno,String supplier) throws DbException {
        DbManager db = x.getDb(daoConfig);
        if(null == orderno){
            orderno = "";
        }
        if(null == supplier){
            supplier = "";
        }
        db.findAll(Pu_order.class);
        return db.selector(Pu_order.class).where("number","like","%"+orderno+"%").and("supplier","like","%"+supplier+"%").findAll();
    }

    public List<Pu_order_b> queryOrderDetail(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Pu_order_b.class).where("orderid","=",orderId).findAll();
    }

    public void syncData(String userId) throws DbException {
        DbManager db = x.getDb(daoConfig);

        db.dropTable(Pu_order.class);
        db.dropTable(Pu_order_b.class);
        db.dropTable(Consumer.class);
        final String userOID= userId;

        NetUtils.Mobile_DownloadOrderInfo(userOID, new MaCallback.MainInfoCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject)   {

                //token 需要存到数据库 上传使用
                String tokenStr = jsonObject.getString("tokenStr");



                JSONArray orderArray =  jsonObject.getJSONArray("details");


                if(orderArray.size() > 0){

                    Toast.makeText(x.app(),"开始同步:"+orderArray.size()+"条",Toast.LENGTH_LONG).show();
                    for(int i = 0 ; i < orderArray.size() ; i++)
                    {
                        JSONObject order = (JSONObject) orderArray.get(i);


                        final Pu_order pu_order = JSON.parseObject(order.toJSONString(),Pu_order.class);


                        //查询表体物料

                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadOrderMaterial(userOID,pu_order.getId(),tokenStr );
                            List<Pu_order_b> list =  JSON.parseArray(jsonArray.toJSONString(), Pu_order_b.class);
                            Toast.makeText(x.app(),"开始同步明细:"+list.size()+"条",Toast.LENGTH_LONG).show();
                            //保存
                            new MaDAO().save(pu_order,list);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        //查询领料商
                        try {
                            JSONArray jsonArray =   NetUtils.Mobile_DownloadOrderconsumer(userOID,pu_order.getId(),tokenStr );
                            List<Consumer> consumerList  =  JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();
                            new MaDAO().save(consumerList);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    }
                    Toast.makeText(x.app(),"同步订单成功",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(x.app(),"同步成功订单0条",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
