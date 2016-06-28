package com.langchao.mamanage.activity.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.LoginActivity;
import com.langchao.mamanage.activity.ServiceActivity;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.MaDAO;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.ex.DbException;
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
    @Event(value = {R.id.service, R.id.set,R.id.logOut,R.id.textViewClear}, type = View.OnClickListener.class)
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
            case R.id.logOut://参数设置
                clearUserInfo();
                Intent intent = new Intent();
                intent.setAction(MaConstants.CLOSE); // 说明动作
                sendBroadcast(intent);// 该函数用于发送广播
                finish();
                //SetActivity.this.finish();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.textViewClear://清除离线数据
                try {
                    new MaDAO().clearData(this);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void clearUserInfo() {

            SharedPreferences.Editor editor = getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).edit();
            // 存入键值对
            editor.remove(MaConstants.PARA_USERNAME);
            editor.remove(MaConstants.PARA_USERID);
            editor.remove(MaConstants.PARA_AUTOLOGIN);

            // 将内存中的数据写到XML文件中去
            editor.commit();

    }


    private String readUserName() {
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_USERNAME, "");

    }


    private String readIp() {
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_IP, "");

    }
}
