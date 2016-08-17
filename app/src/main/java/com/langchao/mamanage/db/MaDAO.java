package com.langchao.mamanage.db;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
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
import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.manet.NetUtils;
import com.langchao.mamanage.utils.MethodUtil;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
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
        try {
            Cursor cursor = db.execQuery("select distinct orderId from pu_order_b where sourceQty > ckQty and rkQty = 0");
            while (cursor.moveToNext()) {
                String orderId = cursor.getString(0);
                orderIdList.add(orderId);
            }
            cursor.close();
        } catch (Exception e) {

        }

        if (orderIdList.size() == 0)
            return new ArrayList<>();

        List<Pu_order> list2 = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", null).and("id", "in", orderIdList.toArray(new String[]{})).orderBy("number").findAll();

        List<Pu_order> list = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", "zc").and("id", "in", orderIdList.toArray(new String[]{})).orderBy("number").findAll();
        list.addAll(list2);
        return list;
    }

    public List<Pu_order> queryPuOrderForRk(String orderno, String supplier) throws DbException {
        deleteTempDirout();
        DbManager db = x.getDb(daoConfig);

        if (null == orderno) {
            orderno = "";
        }
        if (null == supplier) {
            supplier = "";
        }
        List<String> orderIdList = new ArrayList<>();
        try {
            Cursor cursor = db.execQuery("select distinct orderId from pu_order_b where sourceQty > rkQty  and ckQty = 0");
            while (cursor.moveToNext()) {
                String orderId = cursor.getString(0);
                orderIdList.add(orderId);
            }
            cursor.close();
        } catch (Exception e) {

        }

        if (orderIdList.size() == 0)
            return new ArrayList<>();
        List<Pu_order> list = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("id", "in", orderIdList.toArray(new String[]{})).orderBy("number").findAll();

        //List<Pu_order> list2 = db.selector(Pu_order.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("type", "=", "rk").and("id", "in", orderIdList.toArray(new String[]{})).findAll();
        // list.addAll(list2);
        return list;
    }

    public List<Pu_order_b> queryOrderDetail(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Pu_order_b> list = db.selector(Pu_order_b.class).where("orderid", "=", orderId).orderBy("name").findAll();
        List<Pu_order_b> newlist = new ArrayList<>();
        for (Pu_order_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getCkQty() > 0) {
                newlist.add(orderb);
                orderb.setCurQty(orderb.getSourceQty() - orderb.getCkQty());
            }
        }
        return newlist;
    }

    public List<Pu_order_b> queryOrderDetailForIn(String orderId) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Pu_order_b> list = db.selector(Pu_order_b.class).where("orderid", "=", orderId).orderBy("name").findAll();
        List<Pu_order_b> newlist = new ArrayList<>();
        for (Pu_order_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getRkQty() > 0) {
                newlist.add(orderb);
                orderb.setCurQty(orderb.getSourceQty() - orderb.getRkQty());
            }
        }
        return newlist;
    }


    public List<Ic_inbill_b> queryInbillDetail(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        List<Ic_inbill_b> list = db.selector(Ic_inbill_b.class).where("orderid", "=", id).orderBy("name").findAll();
        List<Ic_inbill_b> newlist = new ArrayList<>();
        for (Ic_inbill_b orderb : list) {
            if (orderb.getSourceQty() - orderb.getCkQty() > 0) {
                newlist.add(orderb);
                orderb.setCurQty(orderb.getSourceQty() - orderb.getCkQty());
            }
        }
        return newlist;
    }


    public static int outnum = 0;

    public static int innum = 0;

    public static int ditnum = 0;

    public static int ordernum = 0;

    public static int receivenum = 0;

    public static void cleanNums() {
        innum = 0;
        outnum = 0;
        ditnum = 0;
        ordernum = 0;
        receivenum = 0;
    }

    /**
     * 同步入库
     *
     * @param userId
     * @param mainActivity
     */
    public void syncRk(final String userId, final MainActivity mainActivity) throws DbException {


        if (!isExistInBill() && !isExistOutBill()) {
            MessageDialog.show(mainActivity, "没有需要上传的数据!");
            return;
        }

        cleanNums();

        DbManager db = x.getDb(daoConfig);

        List<Pu_order> a_orders = db.findAll(Pu_order.class) == null ? new ArrayList<Pu_order>() : db.findAll(Pu_order.class);
        int a_orderssize = 0;
        if (null != a_orders && a_orders.size() > 0) {
            a_orderssize = a_orders.size();
        }

        //查看是否有入库TOKEN  没有的话清除直入直出  清除入库
        final String rkToken = MethodUtil.getRkToken(mainActivity);
        if (null == rkToken || rkToken.trim().length() == 0) {
            db.dropTable(Ic_diroutbill.class);
            db.dropTable(Ic_diroutbill_b.class);

            //20160726 不全部删除  保留下载的单子

            try {
                db.executeUpdateDelete("delete from ic_inbill_b where  createType is null or createType != '" + MaConstants.TYPE_SYNC + "'");
                db.executeUpdateDelete("delete from consumer where  createType is null or createType != '" + MaConstants.TYPE_SYNC + "'");
            } catch (Exception e) {

            }

//            db.dropTable(Ic_inbill.class);
//            db.dropTable(Ic_inbill_b.class);
        }


        int size = 0;


        //存在入库TOKEN  上传直入直出数据
        final List<Ic_diroutbill_b> ic_diroutbill_bs = db.selector(Ic_diroutbill_b.class).where("status", "=", MaConstants.STATUS_NORMAL).findAll() == null ? new ArrayList<Ic_diroutbill_b>() : db.selector(Ic_diroutbill_b.class).where("status", "=", MaConstants.STATUS_NORMAL).findAll();

        if (null != ic_diroutbill_bs && ic_diroutbill_bs.size() > 0) {
            for (Ic_diroutbill_b ib : ic_diroutbill_bs) {
                String billid = ib.getOrderid();
                Ic_diroutbill head = queryNewHeadDir(billid);
                ib.setPrintcount(head.getPrintcount() == null ? 0 : head.getPrintcount());
            }
        }
        MaDAO.ditnum = ic_diroutbill_bs.size();

        List<Ic_inbill_b> ic_inbill_bs_t = db.selector(Ic_inbill_b.class).findAll() == null ? new ArrayList<Ic_inbill_b>() : db.selector(Ic_inbill_b.class).findAll();

        List<Ic_inbill_b> ic_inbill_bs_new = new ArrayList<>();
        if (ic_inbill_bs_t.size() > 0) {
            for (Ic_inbill_b ib : ic_inbill_bs_t) {
                if (null == ib.getCreateType()) {
                    ic_inbill_bs_new.add(ib);
                }
            }
        }
        MaDAO.innum = ic_inbill_bs_new.size();

        final List<Ic_inbill_b> ic_inbill_bs = ic_inbill_bs_new;
        size = size + ic_diroutbill_bs.size() + ic_inbill_bs.size();

        String ckToken = MethodUtil.getCkToken(mainActivity);

        List<Ic_outbill_b> ic_outbill_bs = new ArrayList<>();
        int outSize = 0;
        if (null == ckToken || ckToken.trim().length() == 0) {
            //没有出库TOKEN的时候  一起上传出库单

            ic_outbill_bs = db.findAll(Ic_outbill_b.class) == null ? new ArrayList<Ic_outbill_b>() : db.findAll(Ic_outbill_b.class);
            outSize = ic_outbill_bs.size();

            MaDAO.outnum = outSize;

            if (null != ic_outbill_bs && ic_outbill_bs.size() > 0) {
                for (Ic_outbill_b ib : ic_outbill_bs) {
                    String billid = ib.getOrderid();
                    Ic_outbill head = queryNewHead(billid);
                    ib.setPrintcount(head.getPrintcount() == null ? 0 : head.getPrintcount());
                }
            }
        }

        final boolean delOut = (null == ckToken || ckToken.trim().length() == 0);
        final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setTitle("数据上传");
        progressDialog.setMessage("正在上传入库单数据");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(size + outSize);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final int finalA_orderssize = a_orderssize;
        final Runnable afterThread = new Runnable() {
            public void run() {
                try {

                    //下载前调用确认完成

                    String rkTokenOld = MethodUtil.getRkToken(mainActivity);
                    if (null != rkTokenOld && rkTokenOld.trim().length() > 0) {
                        NetUtils.Mobile_uploadrkComplete(userId, rkTokenOld, ditnum, innum, outnum);
                    }
                    if ((innum + outnum + ditnum) > 0) {
                        Toast.makeText(mainActivity, "本次上传入库单明细" + innum + "条 出库单明细" + outnum + "条 直入直出明细" + ditnum + "条", Toast.LENGTH_LONG).show();
                    }
                    DbManager db = x.getDb(daoConfig);
                    if (delOut) {
                        db.dropTable(Ic_outbill.class);
                        db.dropTable(Ic_outbill_b.class);
                    }
                    db.dropTable(Ic_inbill.class);
                    db.dropTable(Ic_inbill_b.class);
                    db.dropTable(Ic_diroutbill.class);
                    db.dropTable(Ic_diroutbill_b.class);
                    //开始上传出库
                    syncCk(userId, mainActivity);

                    // 无数据上传  增加弹框 判断是否下载
//                    if(MaDAO.innum == 0 && MaDAO.outnum == 0 && MaDAO.ditnum == 0 && finalA_orderssize > 0){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
//                        builder.setMessage("您想重新下载数据吗？若是则会清除已下载数据");
//                        builder.setTitle("确认下载");
//
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                try {
//                                    downLoadOrder(userId, mainActivity, delOut);
//                                } catch (Throwable throwable) {
//                                    MessageDialog.show(mainActivity, throwable.getMessage());
//                                }
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        AlertDialog alertDialog = builder.create();
//
//                        alertDialog.setCancelable(false);
//                        alertDialog.show();
//                    }else{
//                        downLoadOrder(userId, mainActivity, delOut);
//                    }

                } catch (Exception e) {
                    MessageDialog.show(mainActivity, e.getMessage());
                } catch (Throwable throwable) {
                    MessageDialog.show(mainActivity, throwable.getMessage());
                }
            }
        };

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == -1) {

                    progressDialog.cancel();
                    progressDialog.dismiss();
                    String err = msg.getData().getString("err");
                    MessageDialog.show(mainActivity, err);
                    return;
                }
                if (msg.what >= 10000) {
                    afterThread.run();
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }
                progressDialog.setProgress(msg.what);
                super.handleMessage(msg);
            }
        };

        final List<Ic_outbill_b> finalIc_outbill_bs = ic_outbill_bs;
        final int finalSize = size;
        Runnable doThread = new Runnable() {
            Integer count = 0;

            public void run() {
                try {


                    if (null == rkToken || rkToken.trim().length() == 0) {
                        handler.sendEmptyMessage(10000);

                        return;
                    }

                    if (finalSize == 0) {
                        //不返回
//                        handler.sendEmptyMessage(100);
//                        return;
                    }
                    //先上传直出
                    for (Ic_diroutbill_b ic_diroutbill_b : ic_diroutbill_bs) {
                        try {
                            count = count + 1;
                            NetUtils.uploadZrzc(ic_diroutbill_b, rkToken, userId);

                            handler.sendEmptyMessage(count);
                        } catch (Throwable throwable) {
                            // Toast.makeText(mainActivity,"上传失败:"+throwable.getMessage(),Toast.LENGTH_LONG);
                            Message errmsg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("err", throwable.getMessage());
                            errmsg.setData(bundle);
                            errmsg.what = -1;
                            handler.sendMessage(errmsg);
                            return;
                            // handler.sendEmptyMessage(-1);
                        }
                    }

                    //上传入库
                    for (Ic_inbill_b ic_inbill_b : ic_inbill_bs) {
                        try {
                            count = count + 1;
                            NetUtils.uploadInbill(ic_inbill_b, rkToken, userId);

                            handler.sendEmptyMessage(count);
                        } catch (Throwable throwable) {
                            Message errmsg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("err", throwable.getMessage());
                            errmsg.setData(bundle);
                            errmsg.what = -1;
                            handler.sendMessage(errmsg);
                            return;
                        }
                    }

                    if (null != finalIc_outbill_bs && finalIc_outbill_bs.size() > 0) {
                        for (Ic_outbill_b ic_outbill_b : finalIc_outbill_bs) {
                            try {
                                count = count + 1;
                                NetUtils.uploadOutbill(ic_outbill_b, rkToken, userId, "rkck");

                                handler.sendEmptyMessage(count);
                            } catch (Throwable throwable) {

                                Message errmsg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("err", throwable.getMessage());
                                errmsg.setData(bundle);
                                errmsg.what = -1;
                                handler.sendMessage(errmsg);
                                return;
                            }
                        }
                    }


//                    try {
//                        NetUtils.Mobile_uploadrkComplete(userId, rkToken, ic_diroutbill_bs.size(), ic_inbill_bs.size(), finalIc_outbill_bs.size());
//                    } catch (Throwable throwable) {
//
//                        Message errmsg = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("err", throwable.getMessage());
//                        errmsg.setData(bundle);
//                        errmsg.what = -1;
//                        handler.sendMessage(errmsg);
//                        return;
//                    }
                    //修改为 点击确认后调用

                    handler.sendEmptyMessage(10000);
                    return;

                    // ///
                } catch (Exception e) {
                    Message errmsg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("err", e.getMessage());
                    errmsg.setData(bundle);
                    errmsg.what = -1;
                    handler.sendMessage(errmsg);
                    return;
                } finally {
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        };
        new Thread(doThread).start();
    }


    public void downLoadOrder(final String userId, final MainActivity mainActivity, boolean delout) throws Throwable {


        String rkTokenOld = MethodUtil.getRkToken(mainActivity);
        if (null != rkTokenOld && rkTokenOld.trim().length() > 0) {
            NetUtils.Mobile_uploadrkComplete(userId, rkTokenOld, 0, 0, 0);
        }

        DbManager db = x.getDb(daoConfig);
        db.dropTable(Pu_order.class);
        db.dropTable(Pu_order_b.class);

        //不全部删除
        try {
            int num1 = db.executeUpdateDelete("delete from ic_inbill_b where  createType is null or createType != '" + MaConstants.TYPE_SYNC + "'");

        } catch (Exception e) {

        }

        try {

            int num2 = db.executeUpdateDelete("delete from consumer where  createType is null or createType != '" + MaConstants.TYPE_SYNC + "'");
        } catch (Exception e) {

        }

//        db.dropTable(Ic_inbill.class);
//        db.dropTable(Ic_inbill_b.class);
//        if (delout) {
//            db.dropTable(Ic_outbill.class);
//            db.dropTable(Ic_outbill_b.class);
//        }
//
//        db.dropTable(Ic_diroutbill.class);
//        db.dropTable(Ic_diroutbill_b.class);
//        db.dropTable(Consumer.class);

        JSONObject jsonObject = NetUtils.Mobile_DownloadOrderInfo(userId);

        final String rkToken = jsonObject.getString("tokenStr");
        MethodUtil.saveRkToken(mainActivity, rkToken);

        final JSONArray orderArray = jsonObject.getJSONArray("details");


        if (orderArray.size() > 0) {
            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setTitle("入库下载");
            progressDialog.setMessage("下载订单中");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(orderArray.size());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            final Runnable afterThread = new Runnable() {
                public void run() {
                    try {
                        NetUtils.Mobile_DownLoadOrderComplete(userId, rkToken);
                        MessageDialog.show(mainActivity, "同步入库成功");
                        if (ordernum > 0) {
                            Toast.makeText(mainActivity, "本次下载订单" + ordernum + "张", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        MessageDialog.show(mainActivity, e.getMessage());
                    } catch (Throwable throwable) {
                        MessageDialog.show(mainActivity, throwable.getMessage());
                    }
                }
            };

            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == -1) {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        String err = msg.getData().getString("err");
                        MessageDialog.show(mainActivity, err);
                        return;
                    }
                    if (msg.what >= 10000) {
                        afterThread.run();
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                    progressDialog.setProgress(msg.what);
                    super.handleMessage(msg);
                }
            };


            final Runnable doThread = new Runnable() {

                public void run() {
                    try {
                        for (int i = 0; i < orderArray.size(); i++) {
                            try {
                                JSONObject order = (JSONObject) orderArray.get(i);


                                final Pu_order pu_order = JSON.parseObject(order.toJSONString(), Pu_order.class);

                                JSONArray orderArray = NetUtils.Mobile_DownloadOrderMaterial(userId, pu_order.getId(), rkToken);


                                if (null == orderArray) {
                                    // Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                    Message errmsg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("err", pu_order.getNumber() + "未能获取订单物料");
                                    errmsg.setData(bundle);
                                    errmsg.what = -1;
                                    handler.sendMessage(errmsg);
                                    return;
                                }
                                List<Pu_order_b> list = JSON.parseArray(orderArray.toJSONString(), Pu_order_b.class);
                                //保存


                                JSONArray jsonArray = NetUtils.Mobile_DownloadOrderconsumer(userId, pu_order.getId(), rkToken);
                                if (null == jsonArray || jsonArray.size() == 0) {
                                    // Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                    Message errmsg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("err", pu_order.getNumber() + "未能获取订单领料商");
                                    errmsg.setData(bundle);
                                    errmsg.what = -1;
                                    handler.sendMessage(errmsg);
                                    return;
                                }
                                new MaDAO().save(pu_order, list);
                                if (null != jsonArray) {
                                    List<Consumer> consumerList = JSON.parseArray(jsonArray.toJSONString(), Consumer.class);
                                    new MaDAO().save(consumerList);
                                }
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();


                            } catch (Throwable throwable) {
                                //Toast.makeText(mainActivity,"下载订单明细失败："+throwable.getMessage(),Toast.LENGTH_LONG).show();
                                Message errmsg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("err", throwable.getMessage());
                                errmsg.setData(bundle);
                                errmsg.what = -1;
                                handler.sendMessage(errmsg);
                                return;
                            }


                            MaDAO.ordernum = orderArray.size();
                            handler.sendEmptyMessage(i);

                        }
                        handler.sendEmptyMessage(10000);
                    } catch (Exception e) {
                        Message errmsg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("err", e.getMessage());
                        errmsg.setData(bundle);
                        errmsg.what = -1;
                        handler.sendMessage(errmsg);
                        return;
                    } finally {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                }
            };
            new Thread(doThread).start();

        }


    }


    /**
     * 同步出库
     *
     * @param userId
     * @param mainActivity
     */
    public void syncCk(final String userId, final MainActivity mainActivity) throws DbException {

        if (isExistInBill()) {
            MessageDialog.show(mainActivity, "存在未上传的入库单，请先同步入库！");
            return;
        }

        cleanNums();

        DbManager db = x.getDb(daoConfig);


        List<Ic_inbill> a_iclist = db.findAll(Ic_inbill.class);
        int a_iclistsize = 0;
        if (null != a_iclist && a_iclist.size() > 0) {
            a_iclistsize = a_iclist.size();
        }

        final String ckToken = MethodUtil.getCkToken(mainActivity);

        List<Ic_outbill_b> ic_outbill_bs = db.findAll(Ic_outbill_b.class) == null ? new ArrayList<Ic_outbill_b>() : db.findAll(Ic_outbill_b.class);
        if (null != ic_outbill_bs && ic_outbill_bs.size() > 0) {
            for (Ic_outbill_b ib : ic_outbill_bs) {
                String billid = ib.getOrderid();
                Ic_outbill head = queryNewHead(billid);
                ib.setPrintcount(head.getPrintcount() == null ? 0 : head.getPrintcount());
            }
        }
        outnum = ic_outbill_bs.size();

        final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setTitle("数据上传");
        progressDialog.setMessage("正在上传出库单数据");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(ic_outbill_bs.size());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final int finalA_iclistsize = a_iclistsize;
        final Runnable afterThread = new Runnable() {
            public void run() {
                try {
                    String ckTokenOld = MethodUtil.getCkToken(mainActivity);
                    if (null != ckTokenOld && ckTokenOld.trim().length() > 0) {
                        NetUtils.Mobile_uploadckComplete(userId, ckTokenOld, outnum);
                    }

                    if (outnum > 0) {
                        Toast.makeText(mainActivity, "本次上传出库单明细" + outnum + "条", Toast.LENGTH_LONG).show();
                    }
                    DbManager db = x.getDb(daoConfig);

                    db.dropTable(Ic_outbill.class);
                    db.dropTable(Ic_outbill_b.class);

                    db.dropTable(Ic_inbill.class);
                    db.dropTable(Ic_inbill_b.class);

                    MessageDialog.show(mainActivity, "上传数据成功!");
                    // 无数据上传  增加弹框 判断是否下载
//                    if(MaDAO.outnum == 0 && finalA_iclistsize > 0){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
//                        builder.setMessage("您想重新下载数据吗？若是则会清除已下载数据");
//                        builder.setTitle("确认下载");
//
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                try {
//                                    downLoadReceive(userId, mainActivity);
//                                } catch (Throwable throwable) {
//                                    MessageDialog.show(mainActivity, throwable.getMessage());
//                                }
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        AlertDialog alertDialog = builder.create();
//
//                        alertDialog.setCancelable(false);
//                        alertDialog.show();
//                    }else{
//                        downLoadReceive(userId, mainActivity);
//                    }

                } catch (Exception e) {
                    MessageDialog.show(mainActivity, e.getMessage());
                } catch (Throwable throwable) {
                    MessageDialog.show(mainActivity, throwable.getMessage());
                }
            }
        };

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == -1) {

                    progressDialog.cancel();
                    progressDialog.dismiss();
                    String err = msg.getData().getString("err");
                    MessageDialog.show(mainActivity, err);
                    return;
                }
                if (msg.what >= 10000) {
                    afterThread.run();
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }
                progressDialog.setProgress(msg.what);
                super.handleMessage(msg);
            }
        };

        final List<Ic_outbill_b> finalIc_outbill_bs = ic_outbill_bs;
        Runnable doThread = new Runnable() {
            Integer count = 0;

            public void run() {
                try {

                    if (null == ckToken || ckToken.trim().length() == 0) {
                        handler.sendEmptyMessage(10000);
                        return;
                    }

                    if (null != finalIc_outbill_bs && finalIc_outbill_bs.size() > 0) {
                        for (Ic_outbill_b ic_outbill_b : finalIc_outbill_bs) {
                            try {
                                count = count + 1;
                                NetUtils.uploadOutbill(ic_outbill_b, ckToken, userId, "ck");

                                handler.sendEmptyMessage(count);
                            } catch (Throwable throwable) {

                                Message errmsg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("err", throwable.getMessage());
                                errmsg.setData(bundle);
                                errmsg.what = -1;
                                handler.sendMessage(errmsg);
                                return;
                            }
                        }
                    }

//修改为确认下载后调用
//                    try {
//                        NetUtils.Mobile_uploadckComplete(userId, ckToken, finalIc_outbill_bs.size());
//                    } catch (Throwable throwable) {
//
//                        Message errmsg = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("err", throwable.getMessage());
//                        errmsg.setData(bundle);
//                        errmsg.what = -1;
//                        handler.sendMessage(errmsg);
//                        return;
//                    }

                    handler.sendEmptyMessage(10000);
                    return;

                    // ///
                } catch (Exception e) {
                    Message errmsg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("err", e.getMessage());
                    errmsg.setData(bundle);
                    errmsg.what = -1;
                    handler.sendMessage(errmsg);
                    return;
                } finally {
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        };
        new Thread(doThread).start();
    }


    public void downLoadReceive(final String userId, final MainActivity mainActivity) throws Throwable {


        //调用上传完成
        String ckTokenOld = MethodUtil.getCkToken(mainActivity);
        if (null != ckTokenOld && ckTokenOld.trim().length() > 0) {
            NetUtils.Mobile_uploadckComplete(userId, ckTokenOld, 0);
        }

        DbManager db = x.getDb(daoConfig);

        db.dropTable(Ic_outbill.class);
        db.dropTable(Ic_outbill_b.class);

        db.dropTable(Ic_inbill.class);
        db.dropTable(Ic_inbill_b.class);

        JSONObject jsonObject = NetUtils.Mobile_downloadReceiveInfo(userId);

        final String ckToken = jsonObject.getString("tokenStr");
        MethodUtil.saveCkToken(mainActivity, ckToken);

        final JSONArray orderArray = jsonObject.getJSONArray("details");

        if (null == orderArray || orderArray.size() == 0) {
            MessageDialog.show(mainActivity, "没有待下载的入库单");
            return;
        }

        if (orderArray.size() > 0) {
            final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
            progressDialog.setTitle("出库下载");
            progressDialog.setMessage("下载入库单中");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(orderArray.size());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final Runnable afterThread = new Runnable() {
                public void run() {
                    try {
                        // NetUtils.Mobile_DownLoadReceiveComplete(userId,ckToken);
                        MessageDialog.show(mainActivity, "同步出库成功");
                        Toast.makeText(mainActivity, "本次下载入库单" + receivenum + "张", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        MessageDialog.show(mainActivity, e.getMessage());
                    } catch (Throwable throwable) {
                        MessageDialog.show(mainActivity, throwable.getMessage());
                    }
                }
            };


            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == -1) {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        String err = msg.getData().getString("err");
                        MessageDialog.show(mainActivity, err);
                        return;
                    }
                    if (msg.what >= 10000) {
                        afterThread.run();
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                    progressDialog.setProgress(msg.what);
                    super.handleMessage(msg);
                }
            };


            Runnable doThread = new Runnable() {
                Integer count = 0;

                public void run() {
                    try {
                        // Looper.loop();
                        for (int i = 0; i < orderArray.size(); i++) {
                            JSONObject receive = (JSONObject) orderArray.get(i);


                            Ic_inbill ic_inbill = JSON.parseObject(receive.toJSONString(), Ic_inbill.class);
                            try {
                                JSONArray jsonArray = NetUtils.Mobile_DownloadReceiveMaterial(userId, ic_inbill.getId(), ckToken);


                                if (null == jsonArray) {

                                    Message errmsg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("err", ic_inbill.getNumber() + "未能获取入库单物料");
                                    errmsg.setData(bundle);
                                    errmsg.what = -1;
                                    handler.sendMessage(errmsg);
                                    return;
                                }
                                List<Ic_inbill_b> list = JSON.parseArray(jsonArray.toJSONString(), Ic_inbill_b.class);
                                for (Ic_inbill_b b : list) {
                                    b.setSourceId(ic_inbill.getOrderId());
                                    if (null == b.getOrderentryid()) {
                                        b.setOrderentryid(b.getWareentryid());

                                    }
                                    b.setCreateType(MaConstants.TYPE_SYNC);
                                }


                                JSONArray consumerArray = NetUtils.Mobile_DownloadReceiveconsumer(userId, ic_inbill.getId(), ckToken);
                                if (null == consumerArray || consumerArray.size() == 0) {
                                    // Toast.makeText(x.app(), "下载明细失败", Toast.LENGTH_LONG).show();
                                    Message errmsg = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("err", ic_inbill.getNumber() + "未能获取入库单领料商");
                                    errmsg.setData(bundle);
                                    errmsg.what = -1;
                                    handler.sendMessage(errmsg);
                                    return;
                                }
                                //保存
                                new MaDAO().save(ic_inbill, list);
                                if (null != consumerArray) {
                                    List<Consumer> consumerList = JSON.parseArray(consumerArray.toJSONString(), Consumer.class);
                                    for (Consumer consumer : consumerList) {
                                        consumer.setCreateType(MaConstants.TYPE_SYNC);
                                    }
                                    new MaDAO().save(consumerList);
                                }
//                            Toast.makeText(x.app(),"开始同步领料商:"+consumerList.size()+"条",Toast.LENGTH_LONG).show();


                            } catch (Throwable throwable) {

                                Message errmsg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("err", throwable.getMessage());
                                errmsg.setData(bundle);
                                errmsg.what = -1;
                                handler.sendMessage(errmsg);
                                return;
                            }
                        }
                        NetUtils.Mobile_DownLoadReceiveComplete(userId, ckToken);
                        receivenum = orderArray.size();
                        handler.sendEmptyMessage(10000);
                        return;

                        // ///
                    } catch (Exception e) {
                        Message errmsg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("err", e.getMessage());
                        errmsg.setData(bundle);
                        errmsg.what = -1;
                        handler.sendMessage(errmsg);
                        return;
                    } catch (Throwable throwable) {
                        Message errmsg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("err", throwable.getMessage());
                        errmsg.setData(bundle);
                        errmsg.what = -1;
                        handler.sendMessage(errmsg);
                        return;
                    } finally {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                    }
                }
            };
            new Thread(doThread).start();


        }

    }


    public void syncData(String userId, final MainActivity mainActivity) throws Throwable {

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

        JSONObject jsonObject = NetUtils.Mobile_DownloadOrderInfo(userOID);
        {

            if (null == jsonObject) {
                Toast.makeText(x.app(), "下载订单失败", Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
                return;
            }
            //token 需要存到数据库 上传使用
            String rkToken = jsonObject.getString("tokenStr");


            JSONArray orderArray = jsonObject.getJSONArray("details");


            JSONObject receiveObject = NetUtils.Mobile_downloadReceiveInfo(userOID);

            String reToken = receiveObject.getString("tokenStr");

            MethodUtil.saveRkToken(mainActivity, rkToken);
            MethodUtil.saveCkToken(mainActivity, reToken);


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
                        JSONArray jsonArray = NetUtils.Mobile_DownloadOrderMaterial(userOID, pu_order.getId(), rkToken);
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
                        JSONArray jsonArray = NetUtils.Mobile_DownloadOrderconsumer(userOID, pu_order.getId(), rkToken);
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

    }

    public void clearData(Context context) throws DbException {
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

        MethodUtil.saveRkToken(context, "");
        MethodUtil.saveCkToken(context, "");

        MessageDialog.show(context, "清除离线数据成功");
    }

    public List<Consumer> findConsumers(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Consumer.class).where("Orderid", "=", id.toUpperCase()).or("Orderid", "=", id.toLowerCase()).findAll();
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

        List<Ic_inbill> list = db.selector(Ic_inbill.class).where("number", "like", "%" + orderno + "%").and("supplier", "like", "%" + supplier + "%").and("id", "in", orderIdList.toArray(new String[]{})).orderBy("number").findAll();


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
    public void saveDirOutBillTemp(Ic_diroutbill_agg outbillAgg, Pu_order_agg order) throws DbException {
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

        List<String> orderIdList = new ArrayList<>();
        try {
            Cursor cursor = db.execQuery("select distinct sourceId from ic_diroutbill_b where status = '" + MaConstants.STATUS_TEMP + "'");
            while (cursor.moveToNext()) {
                String orderId = cursor.getString(0);
                orderIdList.add(orderId);
            }
            cursor.close();
            for (String orderId : orderIdList) {
                db.executeUpdateDelete("update pu_order_b set ckQty = 0 where orderid = '" + orderId + "'");
                // db.executeUpdateDelete("update pu_order set type = null where orderid = '" + orderId + "'");
            }
        } catch (Exception e) {

        }

        db.delete(Ic_diroutbill.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP));
        db.delete(Ic_diroutbill_b.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP));
    }

    public void updateTempDirToNormal() throws DbException {
        DbManager db = x.getDb(daoConfig);
        db.executeUpdateDelete("update ic_diroutbill set status = '" + MaConstants.STATUS_NORMAL + "' where status = '" + MaConstants.STATUS_TEMP + "'");
        db.executeUpdateDelete("update ic_diroutbill_b set status = '" + MaConstants.STATUS_NORMAL + "' where status = '" + MaConstants.STATUS_TEMP + "'");
//        db.update(Ic_diroutbill.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP),new KeyValue("status",MaConstants.STATUS_NORMAL));
//        db.update(Ic_diroutbill_b.class, WhereBuilder.b("status", "=", MaConstants.STATUS_TEMP),new KeyValue("status",MaConstants.STATUS_NORMAL));
        // db.findAll(Ic_diroutbill_b.class);
    }


    public void updateDataToServer(Context context) throws DbException {

        String ordertoken = MethodUtil.getRkToken(context);

        String inbilltoken = MethodUtil.getCkToken(context);

        DbManager db = x.getDb(daoConfig);
        List<Ic_inbill_b> ic_inbill_bs = db.findAll(Ic_inbill_b.class);
        List<Ic_diroutbill_b> ic_diroutbill_bs = db.findAll(Ic_diroutbill_b.class);
        List<Ic_outbill_b> ic_outbill_bs = db.findAll(Ic_outbill_b.class);

        for (Ic_inbill_b ic_inbill_b : ic_inbill_bs) {
            if (!ic_inbill_b.getCreateType().equals(MaConstants.TYPE_SYNC)) {
                //过滤同步过来的入库单
                //upload to server
            }
        }
        for (Ic_diroutbill_b ic_diroutbill_b : ic_diroutbill_bs) {
            if (ic_diroutbill_b.getStatus().equals(MaConstants.STATUS_NORMAL)) {
                //正常状态的才可以上传
                //upload to server
            }
        }

        for (Ic_outbill_b ic_outbill_b : ic_outbill_bs) {

            //upload to server

        }

    }


    /**
     * 查询补打
     *
     * @return
     */
    public List<Ic_outbill> queryForPrint() throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Ic_outbill.class).where("isPrint", "=", false).findAll();
    }

    /**
     * 查询补打
     *
     * @return
     */
    public List<Ic_diroutbill> queryDirForPrint() throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Ic_diroutbill.class).where("isPrint", "=", false).findAll();
    }


    public void addPrintCount(String billid) throws DbException {
        String sql = "update ic_outbill set printcount = (printcount+1) where id = '" + billid + "'";
        DbManager db = x.getDb(daoConfig);
        Ic_outbill outbill = db.findById(Ic_outbill.class, billid);

        if (null != outbill) {
            Integer count = outbill.getPrintcount() == null ? 0 : outbill.getPrintcount();
            outbill.setPrintcount(count + 1);
            db.saveOrUpdate(outbill);
        }

        Ic_diroutbill diroutbill = db.findById(Ic_diroutbill.class, billid);

        if (null != diroutbill) {
            Integer count = diroutbill.getPrintcount() == null ? 0 : diroutbill.getPrintcount();
            diroutbill.setPrintcount(count + 1);
            db.saveOrUpdate(diroutbill);
        }
    }

    public List<Ic_outbill_b> queryDetailForPrint(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        //db.findAll(Ic_outbill_b.class);
        return db.selector(Ic_outbill_b.class).where("orderid", "=", id).findAll();
    }

    public List<Ic_diroutbill_b> queryDetailForPrintDir(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.selector(Ic_diroutbill_b.class).where("orderid", "=", id).findAll();
    }

    public Ic_outbill queryNewHead(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.findById(Ic_outbill.class, id);
    }

    public Ic_diroutbill queryNewHeadDir(String id) throws DbException {
        DbManager db = x.getDb(daoConfig);
        return db.findById(Ic_diroutbill.class, id);
    }

    public boolean isExistInBill() {
        DbManager db = x.getDb(daoConfig);

        List<Ic_inbill_b> ic_inbill_bs_t = new ArrayList<Ic_inbill_b>();
        try {
            ic_inbill_bs_t = db.selector(Ic_inbill_b.class).findAll() == null ? new ArrayList<Ic_inbill_b>() : db.selector(Ic_inbill_b.class).findAll();
        } catch (DbException e) {
            // e.printStackTrace();
        }

        List<Ic_inbill_b> ic_inbill_bs_new = new ArrayList<>();
        if (ic_inbill_bs_t.size() > 0) {
            for (Ic_inbill_b ib : ic_inbill_bs_t) {
                if (null == ib.getCreateType()) {
                    ic_inbill_bs_new.add(ib);
                }
            }
        }
        if (ic_inbill_bs_new.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isExistOutBill() {
        DbManager db = x.getDb(daoConfig);

        List<Ic_outbill_b> ic_outbill_bs = new ArrayList<Ic_outbill_b>();
        List<Ic_diroutbill_b> ic_diroutbill_bs = new ArrayList<>();
        try {
            ic_outbill_bs = db.findAll(Ic_outbill_b.class) == null ? new ArrayList<Ic_outbill_b>() : db.findAll(Ic_outbill_b.class);
            ic_diroutbill_bs = db.selector(Ic_diroutbill_b.class).where("status", "=", MaConstants.STATUS_NORMAL).findAll() == null ? new ArrayList<Ic_diroutbill_b>() : db.selector(Ic_diroutbill_b.class).where("status", "=", MaConstants.STATUS_NORMAL).findAll();

        } catch (DbException e) {
            // e.printStackTrace();
        }


        if (ic_outbill_bs.size() > 0 || ic_diroutbill_bs.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 数据上传
     *
     * @param s
     * @param activity
     */
    public void Sjsc(String userid, MainActivity activity) throws DbException {
        syncRk(userid, activity);
    }

    /**
     * 出库下载
     *
     * @param s
     * @param activity
     */
    public void Ckxz(final String userid, final MainActivity activity) throws Throwable {
        if (!checkCanDownload(activity)) {
            return;
        }
        if (isExistReceiveBill()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("您想重新下载数据吗？若是则会清除已下载数据");
            builder.setTitle("提示");

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        downLoadReceive(userid, activity);
                    } catch (Throwable throwable) {
                        MessageDialog.show(activity, throwable.getMessage());
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
//
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            downLoadReceive(userid, activity);
        }

    }

    /**
     * 入库下载
     *
     * @param s
     * @param activity
     */
    public void Rkxz(final String userid, final MainActivity activity) throws Throwable {
        if (!checkCanDownload(activity)) {
            return;
        }


        if (isExistOrderBill()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("您想重新下载数据吗？若是则会清除已下载数据");
            builder.setTitle("提示");

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        downLoadOrder(userid, activity, false);
                    } catch (Throwable throwable) {
                        MessageDialog.show(activity, throwable.getMessage());
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
//
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {
            downLoadOrder(userid, activity, false);
        }
    }

    public boolean checkCanDownload(MainActivity activity) {
        if (isExistInBill() || isExistOutBill()) {
            MessageDialog.show(activity, "当前手机有待上传数据，请先数据上传，或清除离线数据!");
            return false;
        }
        return true;
    }


    public boolean isExistReceiveBill() {
        DbManager db = x.getDb(daoConfig);

        try {
            List<Ic_inbill_b> list = db.findAll(Ic_inbill_b.class);
            if (null != list && list.size() > 0) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistOrderBill() {
        DbManager db = x.getDb(daoConfig);

        try {
            List<Pu_order_b> list = db.findAll(Pu_order_b.class);
            if (null != list && list.size() > 0) {
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
