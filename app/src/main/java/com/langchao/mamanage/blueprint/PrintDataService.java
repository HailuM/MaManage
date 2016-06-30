package com.langchao.mamanage.blueprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.langchao.mamanage.dialog.MessageDialog;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class PrintDataService {  
    private Context context = null;  
    private String deviceAddress = null;  
  //  BluetoothActivity ac=new BluetoothActivity();
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter  
            .getDefaultAdapter();  
    private BluetoothDevice device = null;  
    private static BluetoothSocket bluetoothSocket = null;  
    private static OutputStream outputStream = null;  
    private static final UUID uuid = UUID  
            .fromString("00001101-0000-1000-8000-00805F9B34FB");  
    private boolean isConnection = false;  
    final String[] items = { "复位打印", "标准ASCII字体", "压缩ASCII字体", "字体不放",  
            "宽高加�?", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
            "取消顺时针旋�?0°", "选择顺时针旋�?0°" };
    final byte[][] byteCommands = { { 0x1b, 0x40 },// 复位打印�?
            { 0x1b, 0x4d, 0x00 },// 标准ASCII字体
            { 0x1b, 0x4d, 0x01 },// 压缩ASCII字体
            { 0x1d, 0x21, 0x00 },// 字体不放�?
            { 0x1d, 0x21, 0x11 },// 宽高加�?
            { 0x1b, 0x45, 0x00 },// 取消加粗模式
            { 0x1b, 0x45, 0x01 },// 选择加粗模式
            { 0x1b, 0x7b, 0x00 },// 取消倒置打印
            { 0x1b, 0x7b, 0x01 },// 选择倒置打印
            { 0x1d, 0x42, 0x00 },// 取消黑白反显
            { 0x1d, 0x42, 0x01 },// 选择黑白反显
            { 0x1b, 0x56, 0x00 },// 取消顺时针旋�?0°
            { 0x1b, 0x56, 0x01 },// 选择顺时针旋�?0°
    };  
  
    public PrintDataService(Context context, String deviceAddress) {  
        super();  
        this.context = context;  
        this.deviceAddress = "88:68:1E:00:0A:B7";

        this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);  
    }  
  
    /** 
     * 获取设备名称 
     *  
     * @return String 
     */  
    public String getDeviceName() {  
        return this.device.getName();  
    }  
  
    /** 
     * 连接蓝牙设备 
     */  
    public boolean connect() {  
        if (!this.isConnection) {  
            try {  
                bluetoothSocket = this.device  
                        .createRfcommSocketToServiceRecord(uuid);  
                bluetoothSocket.connect();  
                outputStream = bluetoothSocket.getOutputStream();  
                this.isConnection = true;  
                if (this.bluetoothAdapter.isDiscovering()) {  
                    System.out.println("关闭适配器！");  
                    this.bluetoothAdapter.isDiscovering();  
                }  
            } catch (Exception e) {  
                Toast.makeText(this.context, "连接失败", Toast.LENGTH_LONG).show();
                return false;  
            }     
            Toast.makeText(this.context, this.device.getName() + "连接成功",  
                    Toast.LENGTH_SHORT).show();  
            return true;  
        } else {  
            return true;  
        }  
    }  
  
    /** 
     * 断开蓝牙设备连接 
     */  
    public static void disconnect() {  
        System.out.println("断开蓝牙设备连接");  
        try {  
            bluetoothSocket.close();  
            outputStream.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
  
    }  
  
   
  
    /** 
     * 发送数据 
     */  
    public void send(String sendData) {  
        if (this.isConnection) {  
            System.out.println("打印！！");  
         //   Toast.makeText(this.context, "打印成功", Toast.LENGTH_SHORT)  
        //    .show();
          
            try {  
                byte[] data = sendData.getBytes("gbk");  
                outputStream.write(data, 0, data.length);  
                outputStream.flush();  
                Intent i=new Intent();
                i.setClassName(context, "com.ntznjxapp.zhuye");
                context.startActivity(i);
                Toast.makeText(this.context, "打印成功", Toast.LENGTH_SHORT)     .show();
                System.exit(0);
                disconnect();
            } catch (IOException e) {  
                Toast.makeText(this.context, "发送失败", Toast.LENGTH_SHORT)  
                        .show();  
            }  
        } else {  
            Toast.makeText(this.context, "设备未连接，请重新连接！", Toast.LENGTH_SHORT)  
                    .show();  
  
        }  
    }

	
  
}
