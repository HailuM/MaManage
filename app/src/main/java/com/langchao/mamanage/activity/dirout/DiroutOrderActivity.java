package com.langchao.mamanage.activity.dirout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.langchao.mamanage.R;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_agg;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.dialog.MessageDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_order)
public class DiroutOrderActivity extends AutoLayoutActivity {
    @ViewInject(R.id.tv_dir_out_order_no)
    TextView tvOrderNo;//订单号
    @ViewInject(R.id.tv_dir_out_order_supply)
    TextView tvOrderSupply;//供方
    @ViewInject(R.id.tv_dir_out_order_build)
    TextView tvOrderBuild;//楼栋
    @ViewInject(R.id.tv_dir_out_order_contact)
    TextView tvOrderContact;//联系人
    @ViewInject(R.id.lv_dir_out_order_m)
    ListView lvOrderMaterial;
    @ViewInject(R.id.img_dir_out_order_choose)
    ImageView imgOrderChoose;
    @ViewInject(R.id.tv_dir_out_order_choose)
    TextView tvOrderChoose;
    @ViewInject(R.id.cb_dir_out_order_choose)
    CheckBox cbOrderChoose;


    DiroutMaterialAdapter adapter = null;


    @Event(value = {R.id.cb_dir_out_order_choose }, type = View.OnClickListener.class)
    private void chooseClick(View v){
        if(cbOrderChoose.isChecked()){
            adapter.chooseAll();
        }else{
            adapter.unChooseAll();
        }
    }
    @Event(value = {R.id.img_dir_out_order_choose}, type = View.OnClickListener.class)
    private void chooseConfirm(View v){
       if(adapter.choosedList == null || adapter.choosedList.size() == 0){
           Toast.makeText(this,"未选中数据",Toast.LENGTH_LONG).show();
           return;
       }else{
           Pu_order_agg agg = new Pu_order_agg();
           agg.setPu_order(order);
           agg.setPu_order_bs(adapter.choosedList);
           Intent intent = new Intent(this, DiroutOrderConfirmActivity.class);


           Bundle bundle = new Bundle();
           bundle.putSerializable("order", agg);

           intent.putExtras(bundle);

           this.startActivityForResult(intent,0);
       }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                freshData();
                break;
            default:
                freshData();
                break;
        }
    }

    @Event(value = {R.id.back_image}, type = View.OnClickListener.class)
    private void back(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该直入直出未完毕，请点击取消后继续进行“直入直出”操作；如果只需要部分入库或出库，请点击确认按钮，退出“直入直出”功能，进入“入库”或“出库”功能，原直入直出已保存的入库、出库单据，打印的出库单将失效，是否确认返回。");
        builder.setTitle("确认返回");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    DiroutOrderActivity.this.finish();
                } catch (Throwable throwable) {
                    MessageDialog.show(DiroutOrderActivity.this, throwable.getMessage());
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该直入直出未完毕，请点击取消后继续进行“直入直出”操作；如果只需要部分入库或出库，请点击确认按钮，退出“直入直出”功能，进入“入库”或“出库”功能，原直入直出已保存的入库、出库单据，打印的出库单将失效，是否确认返回。");
        builder.setTitle("确认返回");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    DiroutOrderActivity.super.onBackPressed();
                } catch (Throwable throwable) {
                    MessageDialog.show(DiroutOrderActivity.this, throwable.getMessage());
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    void onBackPressed2(){
        super.onBackPressed();
    }

    void freshData(){
        List<Pu_order_b> list = null;
        try {
            list = new MaDAO().queryOrderDetail(order.getId());

            adapter.blist = list;
            adapter.choosedList = new ArrayList<>();
            updateTotalNum(0);
            cbOrderChoose.setChecked(false);
            adapter.notifyDataSetChanged();
            if(null == list || list.size() == 0){
                //完成了  修改单据为正常
                new MaDAO().updateTempDirToNormal();
                setResult(RESULT_OK);
                finish();
            }
        } catch (DbException e) {
            MessageDialog.show(this,e.getMessage());
        }
    }

    Pu_order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

          order = (Pu_order) this.getIntent().getExtras().getSerializable("order");


        tvOrderNo.setText(order.getNumber());
        tvOrderBuild.setText(order.getAddr());
        tvOrderContact.setText(order.getName());
        tvOrderSupply.setText(order.getSupplier());


        List<Pu_order_b> list = null;
        try {
            list = new MaDAO().queryOrderDetail(order.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter =   new DiroutMaterialAdapter(this, list);
        lvOrderMaterial.setAdapter(adapter);
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(MaConstants.FRESH);
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }
    // 写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
            Pu_order_b order_b = (Pu_order_b) intent.getExtras().getSerializable("order");
            adapter.addBack(order_b);
            adapter.notifyDataSetChanged();
            updateTotalNum(adapter.choosedList.size());

        }
    };

    public void updateTotalNum(int count){

        tvOrderChoose.setText("已选品种："+count);
    }
}
