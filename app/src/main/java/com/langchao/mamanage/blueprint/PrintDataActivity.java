package com.langchao.mamanage.blueprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.langchao.mamanage.R;


public class PrintDataActivity extends Activity {  
    private Context context = null;  

    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.setTitle("连接打印预览");  
        this.setContentView(R.layout.activity_print_data);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);//竖屏 
        this.context = this;  
        this.initListener();  
      
    }  
  
    /** 
     * 鑾峰緱浠庝笂涓?釜Activity浼犳潵鐨勮摑鐗欏湴鍧?
     * @return String 
     */  
    private String getDeviceAddress() {  
        // 鐩存帴閫氳繃Context绫荤殑getIntent()鍗冲彲鑾峰彇Intent  
        Intent intent = this.getIntent();  
        // 鍒ゆ柇  
        if (intent != null) {  
            return "88:68:1E:00:0A:B7";  
        } else {  
            return null;  
        }  
    }  
  
    private void initListener() {  
        TextView deviceName = (TextView) this.findViewById(R.id.device_name);  
        TextView connectState = (TextView) this  
                .findViewById(R.id.connect_state);  
  
        PrintDataAction printDataAction = new PrintDataAction(this.context,  
                this.getDeviceAddress(), deviceName, connectState);  
  
        EditText printData = (EditText) this.findViewById(R.id.print_data);  
        @SuppressWarnings("unused")
		Bundle b=this.getIntent().getExtras();
        Intent intent = this.getIntent();  
        
     
 	   final String namezl= intent.getStringExtra("xians");
 	   printData.setText(namezl);
        Button send = (Button) this.findViewById(R.id.send);  
        Button command = (Button) this.findViewById(R.id.command);  
        printDataAction.setPrintData(printData);  
  
        send.setOnClickListener(printDataAction);  
        command.setOnClickListener(printDataAction);  
        
    }  
  
      
    @Override  
    protected void onDestroy() {  
        PrintDataService.disconnect();  
        super.onDestroy();  
    }  
      
}
