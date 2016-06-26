package com.langchao.mamanage.db;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.activity.MainActivity;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.ic_out.Ic_outbill_b;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.dialog.LoadingDialog;
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

    public void save(Ic_inbill ic_inbill, List<Ic_inbill_b> ic_inbill_bs) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(ic_inbill);
        db.save(ic_inbill_bs);

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
        List<Pu_order> list2 = db.selector(Pu_order.class).where("number","like","%"+orderno+"%").and("supplier","like","%"+supplier+"%").and("type","=",null).findAll();

        List<Pu_order> list  = db.selector(Pu_order.class).where("number","like","%"+orderno+"%").and("supplier","like","%"+supplier+"%").and("type","=","zc").findAll();
        list.addAll(list2);
        return list ;
    }

    public List<Pu_order> queryPuOrderForRk(String orderno,String supplier) throws DbException {
        DbManager db = x.getDb(daoConfig);
        if(null == orderno){
            orderno = "";
        }
        if(null == supplier){
            supplier = "";
        }

        List<Pu_order> list = db.selector(Pu_order.class).where("number","like","%"+orderno+"%").and("supplier","like","%"+supplier+"%").and("type","=",null).findAll();

        List<Pu_order> list2 = db.selector(Pu_order.class).where("number","like","%"+orderno+"%").and("supplier","like","%"+supplier+"%").and("type","=","rk").findAll();
        list.addAll(list2);
        return list ;
    }

    public List<Pu_order_b> queryOrderDetail(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Pu_order_b.class).where("orderid","=",orderId).findAll();
    }

    public void syncData(String userId, final MainActivity mainActivity ) throws DbException {

//        final LoadingDialog loadingDialog = new LoadingDialog(mainActivity);
//        final Dialog dialog = loadingDialog.createLoadingDialog(mainActivity,"同步数据");
//        dialog.show();

        //进度
        final ProgressDialog progressDialog = ProgressDialog.show(mainActivity,"同步入库数据","正在同步",false,true);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//

        DbManager db = x.getDb(daoConfig);

        db.dropTable(Pu_order.class);
        db.dropTable(Pu_order_b.class);
        db.dropTable(Ic_inbill.class);
        db.dropTable(Ic_inbill_b.class);
        db.dropTable(Consumer.class);
        final String userOID= userId;

        NetUtils.Mobile_DownloadOrderInfo(userOID, new MaCallback.MainInfoCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws InterruptedException {

                if(null == jsonObject){
                    Toast.makeText(x.app(),"下载订单失败",Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                    return;
                }
                //token 需要存到数据库 上传使用
                String tokenStr = jsonObject.getString("tokenStr");



                JSONArray orderArray =  jsonObject.getJSONArray("details");


                JSONObject receiveObject = NetUtils.Mobile_downloadReceiveInfo(userOID);

                String reToken = receiveObject.getString("tokenStr");
                if(null == receiveObject){
                    Toast.makeText(x.app(),"下载入库单失败",Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                    return;
                }
                JSONArray receiveArray =  receiveObject.getJSONArray("details");


                progressDialog.setMax(orderArray.size() + receiveArray.size());
                progressDialog.setTitle("开始下载明细数据");

                if(orderArray.size() > 0){

                    //Toast.makeText(x.app(),"开始同步:"+orderArray.size()+"条",Toast.LENGTH_LONG).show();
                    for(int i = 0 ; i < orderArray.size() ; i++)
                    {
                        JSONObject order = (JSONObject) orderArray.get(i);


                        final Pu_order pu_order = JSON.parseObject(order.toJSONString(),Pu_order.class);


                        //查询表体物料

                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadOrderMaterial(userOID,pu_order.getId(),tokenStr );
                            List<Pu_order_b> list =  JSON.parseArray(jsonArray.toJSONString(), Pu_order_b.class);
                           // Toast.makeText(x.app(),"开始同步明细:"+list.size()+"条",Toast.LENGTH_LONG).show();
                            if(null == jsonArray){
                                Toast.makeText(x.app(),"下载明细失败",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            //保存
                            new MaDAO().save(pu_order,list);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        //查询领料商
                        try {
                            JSONArray jsonArray =   NetUtils.Mobile_DownloadOrderconsumer(userOID,pu_order.getId(),tokenStr );
                            if(null == jsonArray){
                                Toast.makeText(x.app(),"下载明细失败",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            List<Consumer> consumerList  =  JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();

                            new MaDAO().save(consumerList);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        progressDialog.setProgress(i+1);
                    }

                   //
                }

                if(receiveArray.size() > 0){

                    //Toast.makeText(x.app(),"开始同步:"+orderArray.size()+"条",Toast.LENGTH_LONG).show();
                    for(int i = 0 ; i < receiveArray.size() ; i++)
                    {
                        JSONObject receive = (JSONObject) receiveArray.get(i);


                        final Ic_inbill ic_inbill = JSON.parseObject(receive.toJSONString(),Ic_inbill.class);


                        //查询表体物料

                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadReceiveMaterial(userOID,ic_inbill.getId(),reToken );
                            if(null == jsonArray){
                                Toast.makeText(x.app(),"下载明细失败",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            List<Ic_inbill_b> list =  JSON.parseArray(jsonArray.toJSONString(), Ic_inbill_b.class);
                            // Toast.makeText(x.app(),"开始同步明细:"+list.size()+"条",Toast.LENGTH_LONG).show();
                            for (Ic_inbill_b icb:list
                                 ) {
                                icb.setCreateType(MaConstants.TYPE_SYNC);
                            }
                            //保存
                            new MaDAO().save(ic_inbill,list);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        //查询领料商
                        try {
                            JSONArray jsonArray =   NetUtils.Mobile_DownloadReceiveconsumer(userOID,ic_inbill.getId(),reToken );
                            List<Consumer> consumerList  =  JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();
                            if(null == jsonArray){
                                Toast.makeText(x.app(),"下载明细失败",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            new MaDAO().save(consumerList);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        progressDialog.setProgress(i+orderArray.size()+1);

                    }

                    // Toast.makeText(x.app(),"同步订单成功",Toast.LENGTH_LONG).show();
                }




                    progressDialog.dismiss();
                Toast.makeText(x.app(),"同步成功",Toast.LENGTH_LONG).show();
//                dialog.dismiss();
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void clearData() throws DbException {
        DbManager db = x.getDb(daoConfig);

        db.dropTable(Pu_order.class);
        db.dropTable(Pu_order_b.class);
        db.dropTable(Ic_inbill.class);
        db.dropTable(Ic_inbill_b.class);
        db.dropTable(Consumer.class);

        db.dropTable(Ic_outbill.class);
        db.dropTable(Ic_outbill_b.class);
        db.dropTable(Ic_diroutbill.class);
        db.dropTable(Ic_diroutbill_b.class);

        Toast.makeText(x.app(), "清除离线数据成功", Toast.LENGTH_LONG).show();
    }

    public List<Consumer> findConsumers(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return  db.selector(Consumer.class).where("Orderid","=",id).findAll();
    }
}
