package com.langchao.mamanage.activity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.ServiceActivity;
import com.langchao.mamanage.common.MaConstants;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author fei
 * @Description:参数设置
 * @date 2016/6/25 16:28
 */
@ContentView(R.layout.activity_set)
public class SetActivity extends AutoLayoutActivity {

    @ViewInject(R.id.textView16)
    private TextView user;


    @ViewInject(R.id.textView24)
    private TextView ip;

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        user.setText(readUserName());
        ip.setText(readIp());
    }

    //点击事件
    @Event(value = {R.id.service, R.id.set}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.service:
                intent.setClass(this, ServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.set://参数设置
                intent.setClass(this, ServiceActivity.class);
                startActivity(intent);
                break;
        }
    }


    private String readUserName() {
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_USERNAME, "");

    }


    private String readIp() {
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_IP, "");

    }
}
