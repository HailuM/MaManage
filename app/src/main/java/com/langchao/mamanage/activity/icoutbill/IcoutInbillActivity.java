package com.langchao.mamanage.activity.icoutbill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class IcoutInbillActivity extends AutoLayoutActivity {
    @ViewInject(R.id.tv_dir_out_order_no)
    TextView tvOrderNo;//订单号
    @ViewInject(R.id.tv_dir_out_order_supply)
    TextView tvOrderSupply;//供方
    @ViewInject(R.id.tv_item_number)
    TextView tv_item_number;//出库单号

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

    @ViewInject(R.id.et_dir_out_list_search)
    private EditText etSearch;

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

            this.startActivityForResult(intent,0);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
               fresh();
                break;
            default:
                fresh();
                break;
        }
    }

    private void fresh() {
        List<Ic_inbill_b> list = null;
        try {
            list = new MaDAO().queryInbillDetail(ic_inbill.getId());
            adapter.blist = list;
            adapter.choosedList = new ArrayList<>();
            updateTotalNum(0);
            cbOrderChoose.setChecked(false);
            adapter.notifyDataSetChanged();
            if(null == list || list.size() == 0){
                setResult(RESULT_OK);
                finish();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.back_image}, type = View.OnClickListener.class)
    private void back(View v) {
        this.finish();

    }

    Ic_inbill ic_inbill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        textViewTitle.setText("出库选择");
        tv_item_number.setText("出库单号");
        ic_inbill = (Ic_inbill) this.getIntent().getExtras().getSerializable("ic_inbill");


        tvOrderNo.setText(ic_inbill.getNumber());
        tvOrderBuild.setText(ic_inbill.getAddr());
        //tvOrderContact.setText(ic_inbill.getName());
        tvOrderSupply.setText(ic_inbill.getSupplier());


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
            adapter.addBack(in_b);
            adapter.notifyDataSetChanged();
            updateTotalNum(adapter.choosedList.size());
        }
    };

    public void updateTotalNum(int count) {

        tvOrderChoose.setText("已选品种：" + count);
    }
}
