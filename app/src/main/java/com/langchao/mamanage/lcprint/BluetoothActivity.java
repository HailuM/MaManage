package com.langchao.mamanage.lcprint;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.langchao.mamanage.R;

import org.xutils.view.annotation.Event;
import org.xutils.x;

public class BluetoothActivity extends Activity {
    private Context context = null;

    @Event(value = {R.id.back_image}, type = View.OnClickListener.class)
    private void back(View v) {
        this.finish();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        this.context = this;  
        setTitle("蓝牙打印");  
        setContentView(R.layout.bluetooth_layout);
        this.initListener();  
    }  
  
    private void initListener() {  
        ListView unbondDevices = (ListView) this
                .findViewById(R.id.unbondDevices);  
        ListView bondDevices = (ListView) this.findViewById(R.id.bondDevices);  
        Button switchBT = (Button) this.findViewById(R.id.openBluetooth_tb);
        Button searchDevices = (Button) this.findViewById(R.id.searchDevices);  
  
        BluetoothAction bluetoothAction = new BluetoothAction(this.context,  
                unbondDevices, bondDevices, switchBT, searchDevices,  
                BluetoothActivity.this,getIntent());
  
        Button returnButton = (Button) this  
                .findViewById(R.id.return_Bluetooth_btn);  
        bluetoothAction.setSearchDevices(searchDevices);  
        bluetoothAction.initView();  
  
        switchBT.setOnClickListener(bluetoothAction);  
        searchDevices.setOnClickListener(bluetoothAction);  
        returnButton.setOnClickListener(bluetoothAction);

        ImageView backView = (ImageView) this.findViewById(R.id.back_image);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }  
    //屏蔽返回键的代码:  
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {  
//        switch(keyCode)
//        {
//           // case KeyEvent.KEYCODE_BACK:return true;
//        }
        return super.onKeyDown(keyCode, event);  
    }  
}