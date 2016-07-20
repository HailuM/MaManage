package com.langchao.mamanage.lcprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.ic_out.Ic_outbill_b;
import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.utils.MathUtils;
import com.langchao.mamanage.utils.MethodUtil;

import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by miaohl on 2016/7/1.
 */
public class PrintUtil {

    public static void print(Context context,Ic_outbill outbill){
        try {
            Ic_outbill newHead = new MaDAO().queryNewHead(outbill.getId());
            List<Ic_outbill_b> details =  new MaDAO().queryDetailForPrint(outbill.getId());
            PrintUtil.print(context,chgBillToString(newHead,details),outbill.getId());
        } catch (DbException e) {
            MessageDialog.show(context,"数据获取失败");
        }
    }

    public static void print(Context context,Ic_diroutbill outbill){
        try {
            Ic_diroutbill newHead = new MaDAO().queryNewHeadDir(outbill.getId());
            List<Ic_diroutbill_b> details =  new MaDAO().queryDetailForPrintDir(outbill.getId());
            PrintUtil.print(context,chgBillToString(newHead,details),outbill.getId());
        } catch (DbException e) {
            MessageDialog.show(context,"数据获取失败");
        }
    }

    public static void print(Context context, String data, String billid) {

        if (null == BluetoothAdapter
                .getDefaultAdapter()) {
            MessageDialog.show(context, "未找到蓝牙适配器");
            return;
        }
        if (!BluetoothAdapter
                .getDefaultAdapter().isEnabled()) {
            startBlueSearch(context, data, billid);
            return;
        }

        String deviceAddress = MethodUtil.getPrintMac(context);
        if (null == deviceAddress || deviceAddress.trim().length() == 0) {
            startBlueSearch(context, data, billid);
            return;
        }
        try {
            BluetoothDevice device = BluetoothAdapter
                    .getDefaultAdapter().getRemoteDevice(deviceAddress);
            if (null == device) {
                startBlueSearch(context, data, billid);
            }

            Intent intent = new Intent();
            intent.setClassName(context,
                    "com.langchao.mamanage.lcprint.PrintDataActivity");
            intent.putExtra("deviceAddress", device.getAddress());

            intent.putExtra("printdata", data);
            intent.putExtra("billid", billid);
            context.startActivity(intent);
        } catch (Exception e) {
            startBlueSearch(context, data, billid);
        }
    }

    private static void startBlueSearch(Context context, String data, String billid) {
        Intent intent = new Intent(context, BluetoothActivity.class);


        intent.putExtra("printdata", data);
        intent.putExtra("billid", billid);
        context.startActivity(intent);

    }



    public static String chgBillToString(Ic_outbill bill, List<Ic_outbill_b> list){

        if(null == bill || null == list || list.size() == 0){
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(MaConstants.PRINT_LINESTART);

        buffer.append("\n");
        Integer count = bill.getPrintcount() == null ? 0 : bill.getPrintcount();
        buffer.append("第"+(count+1)+"次打印");

        buffer.append("\n");

        buffer.append("出库单编号："+bill.getNumber());
        buffer.append("\n");
        buffer.append("供方："+bill.getSupplier());
        buffer.append("\n");
        buffer.append("楼栋："+bill.getAddr());
        buffer.append("\n");
        buffer.append("领用商："+bill.getConsumername());
        buffer.append("\n");
        buffer.append("项目："+bill.getProjectName());
        buffer.append("\n");

        buffer.append(MaConstants.PRINT_LINEBLANK);
        buffer.append("\n");
        //start to join details

        for(Ic_outbill_b detail : list){

            //buffer.append("物料编码："+detail.getNumber());
            buffer.append("物料名称："+detail.getName());
            buffer.append("\n");
            buffer.append("物料规格："+detail.getModel());
            buffer.append("\n");
            buffer.append("计量单位："+detail.getUnit());
            buffer.append("\n");
            buffer.append("出库数量："+detail.getSourceQty());
            buffer.append("\n");
//            buffer.append("物料单价："+ MathUtils.scaleDouble4(detail.getPrice()));
//            buffer.append("\n");
//            buffer.append("金額："+MathUtils.scaleDouble4(detail.getPrice() * detail.getSourceQty()));
//            buffer.append("\n");
            buffer.append("备注："+detail.getNote());
            buffer.append("\n");
            buffer.append(MaConstants.PRINT_LINEBLANK);
            buffer.append("\n");
        }

        buffer.append(MaConstants.PRINT_LINESTART);
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("领用人：________________________");
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("证明人：________________________");
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("\n");
        buffer.append("\n");
        buffer.append("\n");
        buffer.append("\n");

        return buffer.toString();

    }


    public static String chgBillToString(Ic_diroutbill bill, List<Ic_diroutbill_b> list){

        if(null == bill || null == list || list.size() == 0){
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(MaConstants.PRINT_LINESTART);

        buffer.append("\n");
        Integer count = bill.getPrintcount() == null ? 0 : bill.getPrintcount();
        buffer.append("第"+(count+1)+"次打印");

        buffer.append("\n");

        buffer.append("出库单编号："+bill.getNumber());
        buffer.append("\n");
        buffer.append("供方："+bill.getSupplier());
        buffer.append("\n");
        buffer.append("楼栋："+bill.getAddr());
        buffer.append("\n");
        buffer.append("领用商："+bill.getConsumername());
        buffer.append("\n");
        buffer.append("项目："+bill.getProjectName());
        buffer.append("\n");

        buffer.append(MaConstants.PRINT_LINEBLANK);
        buffer.append("\n");
        //start to join details

        for(Ic_diroutbill_b detail : list){

            //buffer.append("物料编码："+detail.getNumber());
            buffer.append("物料名称："+detail.getName());
            buffer.append("\n");
            buffer.append("物料规格："+detail.getModel());
            buffer.append("\n");
            buffer.append("计量单位："+detail.getUnit());
            buffer.append("\n");
            buffer.append("出库数量："+detail.getSourceQty());
            buffer.append("\n");
//            buffer.append("物料单价："+ MathUtils.scaleDouble4(detail.getPrice()));
//            buffer.append("\n");
//            buffer.append("金額："+MathUtils.scaleDouble4(detail.getPrice() * detail.getSourceQty()));
//            buffer.append("\n");
            buffer.append("备注："+detail.getNote());
            buffer.append("\n");
            buffer.append(MaConstants.PRINT_LINEBLANK);
            buffer.append("\n");
        }

        buffer.append(MaConstants.PRINT_LINESTART);
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("领用人：________________________");
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("证明人：________________________");
        buffer.append("\n");
        buffer.append("\n");

        buffer.append("\n");
        buffer.append("\n");
        buffer.append("\n");
        buffer.append("\n");

        return buffer.toString();

    }


}
