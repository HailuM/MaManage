package com.langchao.mamanage.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.dirout.DiroutListActivity;
import com.langchao.mamanage.activity.dirout.DiroutOrderConfirmActivity;
import com.langchao.mamanage.activity.icinbill.IcinListActivity;
import com.langchao.mamanage.activity.icoutbill.IcoutListActivity;
import com.langchao.mamanage.activity.main.PrintActivity;
import com.langchao.mamanage.activity.main.SetActivity;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.lcprint.PrintUtil;
import com.langchao.mamanage.manet.MaCallback;
import com.langchao.mamanage.manet.NetUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

/**
 * @author fei
 * @Description:主页
 * @date 2016/6/25 15:52
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends AutoLayoutActivity {



    Intent intent = new Intent();
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        x.view().inject(this);
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(MaConstants.CLOSE);
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }

    // 写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
            ((Activity) context).finish();
        }
    };


    ProgressDialog progressDialog = null;

    //点击事件
    @Event(value = {R.id.service, R.id.set, R.id.imageViewSjsc, R.id.imageViewdirectout, R.id.imageViewinstorage, R.id.imageViewoutstorage,R.id.imageViewRkxz,R.id.imageViewCkxz,R.id.imageViewsupplement}, type = View.OnClickListener.class)
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.service:
                intent.setClass(this, ServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.set://参数设置
                intent.setClass(this, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.imageViewSjsc:

                //ProgressDialog.show(this,"同步入库数据","开始同步",false,true);


                try {
                    new MaDAO().Sjsc(readUserId(), MainActivity.this);
                } catch (Exception e) {
                   Toast.makeText(this,"数据上传失败:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewdirectout:
                intent.setClass(this, DiroutListActivity.class);
                startActivity(intent);
                break;
            case R.id.imageViewinstorage:
                intent.setClass(this, IcinListActivity.class);
                startActivity(intent);
                break;
            case R.id.imageViewoutstorage:

                intent.setClass(this, IcoutListActivity.class);
                startActivity(intent);
                break;
            case R.id.imageViewRkxz:

                try {
                    new MaDAO().Rkxz(readUserId(), MainActivity.this);
                } catch (Exception e) {
                    Toast.makeText(this,"入库下载失败:"+e.getMessage(),Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    Toast.makeText(this,"入库下载失败:"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewCkxz:

                try {
                    new MaDAO().Ckxz(readUserId(), MainActivity.this);
                } catch (Exception e) {
                    Toast.makeText(this,"出库下载失败:"+e.getMessage(),Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    Toast.makeText(this,"出库下载失败:"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewsupplement:

//                startActivityForResult(new Intent(MainActivity.this,
//                        SelectPicPopupWindow.class), 1);
                intent.setClass(this, PrintActivity.class);
                startActivity(intent);
                break;

        }
    }


    // 按两次返回键退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private String readUserId() {
        return getSharedPreferences(MaConstants.FILENAME, MODE_PRIVATE).getString(MaConstants.PARA_USERID, "");

    }

}
