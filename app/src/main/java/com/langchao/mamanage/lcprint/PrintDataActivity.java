package com.langchao.mamanage.lcprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.langchao.mamanage.R;
import com.langchao.mamanage.dialog.MessageDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.Event;
import org.xutils.x;

public class PrintDataActivity extends AutoLayoutActivity {
    private Context context = null;

    @Event(value = {R.id.imageViewBack}, type = View.OnClickListener.class)
    private void back(View v) {
        this.finish();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        this.setTitle("蓝牙打印");  
        this.setContentView(R.layout.printdata_layout);
        this.context = this;
        if(null == this.getDeviceAddress()){
            MessageDialog.show(this,"蓝牙连接失败");
            this.finish();
            return;
        }
        String data = getIntent().getStringExtra("printdata");
        TextView printdata = (TextView) this
                .findViewById(R.id.print_data);
        printdata.setText(data);
        printdata.setMovementMethod(ScrollingMovementMethod.getInstance());

        this.initListener();

//        ImageView backView = (ImageView) this
//                .findViewById(R.id.imageViewBack);
//        backView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }  
  
    /** 
     * 获得从上一个Activity传来的蓝牙地址 
     * @return String 
     */  
    private String getDeviceAddress() {  
        // 直接通过Context类的getIntent()即可获取Intent  
        Intent intent = this.getIntent();
        // 判断  
        if (intent != null) {  
            return intent.getStringExtra("deviceAddress");  
        } else {  
            return null;  
        }  
    }  
  
    private void initListener() {  
        TextView deviceName = (TextView) this.findViewById(R.id.device_name);
        TextView connectState = (TextView) this  
                .findViewById(R.id.connect_state);  
  
        PrintDataAction printDataAction = new PrintDataAction(this.context,this.getDeviceAddress(), deviceName, connectState,getIntent());
  
        TextView printData = (TextView) this.findViewById(R.id.print_data);
        BootstrapButton send = (BootstrapButton) this.findViewById(R.id.send);
        //Button command = (Button) this.findViewById(R.id.command);
        printDataAction.setPrintData(printData);  
  
        send.setOnClickListener(printDataAction);  
        //command.setOnClickListener(printDataAction);
    }  
  
      
    @Override  
    protected void onDestroy() {  
        PrintDataService.disconnect();  
        super.onDestroy();  
    }  
      
}