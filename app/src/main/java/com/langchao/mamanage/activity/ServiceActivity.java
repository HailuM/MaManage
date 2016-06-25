package com.langchao.mamanage.activity;

import android.os.Bundle;
import android.view.View;

import com.langchao.mamanage.R;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * @author fei
 * @Description:服务器配置
 * @date 2016/6/25 15:52
 */
@ContentView(R.layout.activity_service)
public class ServiceActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    //点击事件
    @Event(value = {R.id.imageView2,R.id.outStorage}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.imageView2:
                this.finish();
                break;
            case R.id.outStorage:
                this.finish();
                break;
        }
    }
}
