package com.langchao.mamanage.activity.icoutbill;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_agg;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order_agg;
import com.langchao.mamanage.db.order.Pu_order_b;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_order)
public class IcoutInbillActivity extends AppCompatActivity {
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

    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;
    IcoutMaterialAdapter adapter = null;


    @Event(value = {R.id.cb_dir_out_order_choose}, type = View.OnClickListener.class)
    private void chooseClick(View v) {
        if (cbOrderChoose.isChecked()) {
            adapter.chooseAll();
        } else {
            adapter.unChooseAll();
        }
    }


    @Event(value = {R.id.img_dir_out_order_choose}, type = View.OnClickListener.class)
    private void chooseConfirm(View v) {
        if (adapter.choosedList == null || adapter.choosedList.size() == 0) {
            Toast.makeText(this, "未选中数据", Toast.LENGTH_LONG).show();
            return;
        } else {
            Ic_inbill_agg agg = new Ic_inbill_agg();
            agg.setIc_inbill(ic_inbill);
            agg.setIc_inbill_bList(adapter.choosedList);
            Intent intent = new Intent(this, IcoutInbillConfirmActivity.class);


            Bundle bundle = new Bundle();
            bundle.putSerializable("ic_inbill", agg);

            intent.putExtras(bundle);

            this.startActivity(intent);
        }
    }

    Ic_inbill ic_inbill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        textViewTitle.setText("出库选择");

        ic_inbill = (Ic_inbill) this.getIntent().getExtras().getSerializable("ic_inbill");


        tvOrderNo.setText(ic_inbill.getNumber());
        tvOrderBuild.setText(ic_inbill.getAddr());
        //tvOrderContact.setText(ic_inbill.getName());


        List<Ic_inbill_b> list = null;
        try {
            list = new MaDAO().queryInbillDetail(ic_inbill.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter = new IcoutMaterialAdapter(this, list);
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
            // unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错

            Ic_inbill_b in_b = (Ic_inbill_b) intent.getExtras().getSerializable("ic_inbill");
            adapter.update(in_b);
            adapter.notifyDataSetChanged();
        }
    };

    public void updateTotalNum(int count) {

        tvOrderChoose.setText("已选品种：" + count);
    }
}
