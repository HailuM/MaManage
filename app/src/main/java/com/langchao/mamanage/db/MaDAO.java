package com.langchao.mamanage.db;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.activity.MainActivity;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_agg;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.ic_out.Ic_outbill_agg;
import com.langchao.mamanage.db.ic_out.Ic_outbill_b;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_agg;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_agg;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.dialog.LoadingDialog;
import com.langchao.mamanage.manet.MaCallback;
import com.langchao.mamanage.manet.NetUtils;
import com.langchao.mamanage.utils.MethodUtil;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            }).setAllowTransaction(true);

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

    public List<Pu_order> queryPuOrder(String orderno, String supplier) throws DbException {
        DbManager db = x.getDb(daoConfig);
        if (null == orderno) {
            orderno = "";
        }
        if (null == supplier) {
            supplier = "";
        }
        List<String> orderIdList = new ArrayList<>();
        Cursor cursor = db.execQuery("select distinct orderId from pu_order_b where sourceQty > ckQty");
        while (cursor.moveToNext()) {
            String orderId = cursor.getString(0);
            orderIdList.add(orderId);
        }
        cursor.close();

        if (orderIdList.size() == 0)
            return new ArrayList<>();

        List<Pu_order> list2 = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", null).findAll();

        List<Pu_order> list = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", "zc").findAll();
        list.addAll(list2);
        return list;
    }

    public List<Pu_order> queryPuOrderForRk(String orderno, String supplier) throws DbException {
        DbManager db = x.getDb(daoConfig);

        if (null == orderno) {
            orderno = "";
        }
        if (null == supplier) {
            supplier = "";
        }
        List<String> orderIdList = new ArrayList<>();
        Cursor cursor = db.execQuery("select distinct orderId from pu_order_b where sourceQty > rkQty");
        while (cursor.moveToNext()) {
            String orderId = cursor.getString(0);
            orderIdList.add(orderId);
        }
        cursor.close();

        if (orderIdList.size() == 0)
            return new ArrayList<>();
        List<Pu_order> list = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", null).and("id", "in", orderIdList.toArray(new String[]{})).findAll();

        List<Pu_order> list2 = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", "rk").and("id", "in", orderIdList.toArray(new String[]{})).findAll();
        list.addAll(list2);
        return list;
    }

    public List<Pu_order_b> queryOrderDetail(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Pu_order_b> list = db.selector(Pu_order_b.class).where("orderid", "=", orderId).findAll();
        List<Pu_order_b> newlist = new ArrayList<>();
        for (Pu_order_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getCkQty() > 0) {
                newlist.add(orderb);
            }
        }
        return newlist;
    }

    public List<Pu_order_b> queryOrderDetailForIn(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Pu_order_b> list = db.selector(Pu_order_b.class).where("orderid", "=", orderId).findAll();
        List<Pu_order_b> newlist = new ArrayList<>();
        for (Pu_order_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getRkQty() > 0) {
                newlist.add(orderb);
            }
        }
        return newlist;
    }


    public List<Ic_inbill_b> queryInbillDetail(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Ic_inbill_b> list = db.selector(Ic_inbill_b.class).where("orderid", "=", id).findAll();
        List<Ic_inbill_b> newlist = new ArrayList<>();
        for (Ic_inbill_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getCkQty() > 0) {
                newlist.add(orderb);
            }
        }
        return newlist;
    }

    public void syncData(String userId, final MainActivity mainActivity) throws DbException {

//        final LoadingDialog loadingDialog = new LoadingDialog(mainActivity);
//        final Dialog dialog = loadingDialog.createLoadingDialog(mainActivity,"同步数据");
//        dialog.show();

        //进度
        final ProgressDialog progressDialog = ProgressDialog.show(mainActivity, "同步入库数据", "正在同步", false, true);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);//

        DbManager db = x.getDb(daoConfig);

        db.dropTable(Pu_order.class);
        db.dropTable(Pu_order_b.class);
        db.dropTable(Ic_inbill.class);
        db.dropTable(Ic_inbill_b.class);
        db.dropTable(Ic_outbill.class);
        db.dropTable(Ic_outbill_b.class);

        db.dropTable(Ic_diroutbill.class);
        db.dropTable(Ic_diroutbill_b.class);
        db.dropTable(Consumer.class);
        final String userOID = userId;

        NetUtils.Mobile_DownloadOrderInfo(userOID, new MaCallback.MainInfoCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws InterruptedException {

                if (null == jsonObject) {
                    Toast.makeText(x.app(), "下载订单失败", Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                    return;
                }
                //token 需要存到数据库 上传使用
                String tokenStr = jsonObject.getString("tokenStr");


                JSONArray orderArray = jsonObject.getJSONArray("details");


                JSONObject receiveObject = NetUtils.Mobile_downloadReceiveInfo(userOID);

                String reToken = receiveObject.getString("tokenStr");

                MethodUtil.saveOrderToken(mainActivity,tokenStr);
                MethodUtil.saveInbillToken(mainActivity,reToken);


                if (null == receiveObject) {
                    Toast.makeText(x.app(), "下载入库单失败", Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                    return;
                }
                JSONArray receiveArray = receiveObject.getJSONArray("details");


                progressDialog.setMax(orderArray.size() + receiveArray.size());
                progressDialog.setTitle("开始下载明细数据");

                if (orderArray.size() > 0) {

                    //Toast.makeText(x.app(),"开始同步:"+orderArray.size()+"条",Toast.LENGTH_LONG).show();
                    for (int i = 0; i < orderArray.size(); i++) {
                        JSONObject order = (JSONObject) orderArray.get(i);


                        final Pu_order pu_order = JSON.parseObject(order.toJSONString(), Pu_order.class);


                        //查询表体物料

                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadOrderMaterial(userOID, pu_order.getId(), tokenStr);
                            List<Pu_order_b> list = JSON.parseArray(jsonArray.toJSONString(), Pu_order_b.class);
                            // Toast.makeText(x.app(),"开始同步明细:"+list.size()+"条",Toast.LENGTH_LONG).show();
                            if (null == jsonArray) {
                                Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            //保存
                            new MaDAO().save(pu_order, list);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        //查询领料商
                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadOrderconsumer(userOID, pu_order.getId(), tokenStr);
                            if (null == jsonArray) {
                                Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            List<Consumer> consumerList = JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();

                            new MaDAO().save(consumerList);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        progressDialog.setProgress(i + 1);
                    }

                    //
                }

                if (receiveArray.size() > 0) {

                    //Toast.makeText(x.app(),"开始同步:"+orderArray.size()+"条",Toast.LENGTH_LONG).show();
                    for (int i = 0; i < receiveArray.size(); i++) {
                        JSONObject receive = (JSONObject) receiveArray.get(i);


                        final Ic_inbill ic_inbill = JSON.parseObject(receive.toJSONString(), Ic_inbill.class);


                        //查询表体物料

                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadReceiveMaterial(userOID, ic_inbill.getId(), reToken);
                            if (null == jsonArray) {
                                Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            List<Ic_inbill_b> list = JSON.parseArray(jsonArray.toJSONString(), Ic_inbill_b.class);
                            // Toast.makeText(x.app(),"开始同步明细:"+list.size()+"条",Toast.LENGTH_LONG).show();
                            for (Ic_inbill_b icb : list
                                    ) {
                                icb.setCreateType(MaConstants.TYPE_SYNC);
                                if (null == icb.getOrderentryid() || icb.getOrderentryid().trim().length() == 0) {
                                    icb.setOrderentryid(UUID.randomUUID().toString());
                                }
                            }
                            //保存
                            new MaDAO().save(ic_inbill, list);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        //查询领料商
                        try {
                            JSONArray jsonArray = NetUtils.Mobile_DownloadReceiveconsumer(userOID, ic_inbill.getId(), reToken);
                            List<Consumer> consumerList = JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();
                            if (null == jsonArray) {
                                Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                            new MaDAO().save(consumerList);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        progressDialog.setProgress(i + orderArray.size() + 1);

                    }

                    // Toast.makeText(x.app(),"同步订单成功",Toast.LENGTH_LONG).show();
                }


                progressDialog.dismiss();
                Toast.makeText(x.app(), "同步成功", Toast.LENGTH_LONG).show();
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
        return db.selector(Consumer.class).where("Orderid", "=", id).findAll();
    }

    /**
     * 保存入库单
     *
     * @param inbillAgg
     * @param orderAgg
     */
    public void saveInBill(Ic_inbill_agg inbillAgg, Pu_order_agg orderAgg) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(inbillAgg.getIc_inbill());
        db.save(inbillAgg.getIc_inbill_bList());
        db.saveOrUpdate(orderAgg.getPu_order());
        db.saveOrUpdate(orderAgg.getPu_order_bs());


    }

    public List<Ic_inbill> queryInbillForCk(String orderno, String supplier) throws DbException {
        DbManager db = x.getDb(daoConfig);

        if (null == orderno) {
            orderno = "";
        }
        if (null == supplier) {
            supplier = "";
        }

        List<String> orderIdList = new ArrayList<>();
        Cursor cursor = db.execQuery("select distinct orderId from ic_inbill_b where sourceQty > ckQty");
        while (cursor.moveToNext()) {
            String orderId = cursor.getString(0);
            orderIdList.add(orderId);
        }
        cursor.close();

        if (orderIdList.size() == 0)
            return new ArrayList<>();

        List<Ic_inbill> list = db.selector(Ic_inbill.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("id", "in", orderIdList.toArray(new String[]{})).findAll();


        return list;
    }


    public void saveOutBill(Ic_outbill_agg outbillAgg, Ic_inbill_agg inbillAgg) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(outbillAgg.getIc_outbill());
        db.save(outbillAgg.getIc_outbill_bs());
        db.saveOrUpdate(inbillAgg.getIc_inbill());
        db.saveOrUpdate(inbillAgg.getIc_inbill_bList());


    }

    /**
     * 暂存
     *
     * @param outbillAgg
     */
    public void saveDirOutBillTemp(Ic_diroutbill_agg outbillAgg,Pu_order_agg order) throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.save(outbillAgg.getIc_diroutbill());
        outbillAgg.getIc_diroutbill().setStatus(MaConstants.STATUS_TEMP);
        for (Ic_diroutbill_b b : outbillAgg.getIc_diroutbill_bs()) {
            b.setStatus(MaConstants.STATUS_TEMP);
        }
        db.save(outbillAgg.getIc_diroutbill_bs());
        db.saveOrUpdate(order.getPu_order());
        db.saveOrUpdate(order.getPu_order_bs());

    }

    /**
     * 删除临时直出
     */

    public void deleteTempDirout() throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.delete(Ic_diroutbill.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP));
        db.delete(Ic_diroutbill_b.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP));
    }

    public void updateTempDirToNormal() throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.executeUpdateDelete("update ic_diroutbill set status = '"+MaConstants.STATUS_NORMAL+"' where status = '"+MaConstants.STATUS_TEMP+"'");
        db.executeUpdateDelete("update ic_diroutbill_b set status = '"+MaConstants.STATUS_NORMAL+"' where status = '"+MaConstants.STATUS_TEMP+"'");
//        db.update(Ic_diroutbill.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP),new KeyValue("status",MaConstants.STATUS_NORMAL));
//        db.update(Ic_diroutbill_b.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP),new KeyValue("status",MaConstants.STATUS_NORMAL));
       // db.findAll(Ic_diroutbill_b.class);
    }








    public void updateDataToServer(Context context) throws DbException {

        String ordertoken = MethodUtil.getOrderToken(context);

        String inbilltoken = MethodUtil.getInbillToken(context);

        DbManager db = x.getDb(daoConfig);
        List<Ic_inbill_b> ic_inbill_bs =  db.findAll(Ic_inbill_b.class);
        List<Ic_diroutbill_b> ic_diroutbill_bs = db.findAll(Ic_diroutbill_b.class);
        List<Ic_outbill_b> ic_outbill_bs =  db.findAll(Ic_outbill_b.class);

        for(Ic_inbill_b ic_inbill_b : ic_inbill_bs){
            if(!ic_inbill_b.getCreateType().equals(MaConstants.TYPE_SYNC)){
                //过滤同步过来的入库单
                //upload to server
            }
        }
        for(Ic_diroutbill_b ic_diroutbill_b : ic_diroutbill_bs){
            if(ic_diroutbill_b.getStatus().equals(MaConstants.STATUS_NORMAL)){
                //正常状态的才可以上传
                //upload to server
            }
        }

        for(Ic_outbill_b ic_outbill_b : ic_outbill_bs){

                //upload to server

        }

    }
}
