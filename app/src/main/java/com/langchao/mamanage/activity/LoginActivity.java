package com.langchao.mamanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.langchao.mamanage.R;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * @author fei
 * @Description:登录页
 * @date 2016/6/22 22:54
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AutoLayoutActivity {
    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    //点击事件
    @Event(value = {R.id.service,R.id.outStorage}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.service:
                intent.setClass(this,ServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.outStorage:
                this.finish();
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
