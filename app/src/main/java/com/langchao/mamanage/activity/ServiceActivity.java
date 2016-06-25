package com.langchao.mamanage.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.langchao.mamanage.R;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.manet.NetUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author fei
 * @Description:服务器配置
 * @date 2016/6/25 15:52
 */
@ContentView(R.layout.activity_service)
public class ServiceActivity extends AutoLayoutActivity {


    @ViewInject(R.id.textViewIp)
    private EditText textViewIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        String ip = readIp();
        textViewIp.setText(ip);

    }

    //点击事件
    @Event(value = {R.id.imageView2, R.id.outStorage}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.imageView2:
                this.finish();
                break;
            case R.id.outStorage:

                if (null == textViewIp.getText() || textViewIp.getText().toString().trim().length() == 0) {
                    Toast.makeText(x.app(), "IP地址不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String ip = textViewIp.getText().toString();
                if(!ip.startsWith("http")){
                    ip = "http://"+ip;
                }
                NetUtils.ip = ip;
                saveIp(ip);
                this.finish();
                break;
        }
    }

    private void saveIp(String ip){
        SharedPreferences.Editor editor = getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).edit();
        // 存入键值对
        editor.putString("ip", ip);
        // 将内存中的数据写到XML文件中去
        editor.commit();
    }

    private String readIp(){
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_IP,"");

    }
}
