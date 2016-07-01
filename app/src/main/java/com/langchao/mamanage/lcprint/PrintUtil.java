package com.langchao.mamanage.lcprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.utils.MethodUtil;

/**
 * Created by miaohl on 2016/7/1.
 */
public class PrintUtil {
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
}
