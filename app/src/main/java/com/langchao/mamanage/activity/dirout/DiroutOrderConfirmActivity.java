package com.langchao.mamanage.activity.dirout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.langchao.mamanage.R;
import com.langchao.mamanage.converter.MaConvert;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_agg;
import com.langchao.mamanage.db.ic_out.Ic_outbill_agg;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_agg;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.dialog.MessageDialog;
import com.langchao.mamanage.lcprint.PrintUtil;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
@ContentView(R.layout.activity_dir_out_confirm)
public class DiroutOrderConfirmActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_dir_out_no)
    TextView tvOrderNo;//订单号
    @ViewInject(R.id.tv_dir_out_supply)
    TextView tvOrderSupply;//供方
    @ViewInject(R.id.tv_dir_out_build)
    TextView tvOrderBuild;//楼栋
    @ViewInject(R.id.tv_dir_out_contact)
    TextView tvOrderContact;//联系人
    @ViewInject(R.id.sp_dir_out_get)
    Spinner spOrderGet;//领用商
    @ViewInject(R.id.lv_dir_out_m)
    ListView lvOrderMaterial;
    @ViewInject(R.id.img_dir_out_choose)
    ImageView imgOrderChoose;
    @ViewInject(R.id.tv_dir_out_choose)
    TextView tvOrderChoose;


    @Event(value = {R.id.back_image}, type = View.OnClickListener.class)
    private void back(View v) {
        this.finish();

    }
    DiroutConfirmAdapter adapter = null;

    Pu_order_agg orderAgg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


          orderAgg = (Pu_order_agg) this.getIntent().getExtras().getSerializable("order");


        Pu_order order = orderAgg.getPu_order();
        order.setType("zc");
        tvOrderNo.setText(order.getNumber());
        tvOrderBuild.setText(order.getAddr());
        tvOrderContact.setText(order.getName());
        tvOrderSupply.setText(order.getSupplier());

        List<Consumer> consumers = null;
        try {
            consumers = new MaDAO().findConsumers(order.getId());
            spOrderGet.setAdapter(new ConsumerAdapter(this,consumers));
        } catch (DbException e) {
            e.printStackTrace();
        }




        List<Pu_order_b> list = orderAgg.getPu_order_bs();
        tvOrderChoose.setText("已选品种："+ list.size());


        adapter =   new DiroutConfirmAdapter(this, list);
        lvOrderMaterial.setAdapter(adapter);
    }

    public void updateTotal(int size){
        tvOrderChoose.setText("已选品种：" + size);
    }

    /**
     * 确认后保存数据
     *
     * @param v
     */
    @Event(value = {R.id.img_dir_out_choose}, type = View.OnClickListener.class)
    private void confirm(View v) {
        try {
            buildOutBill();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void buildOutBill() throws DbException {


        Ic_diroutbill_agg outbillAgg = MaConvert.convertPuorderToDirOut(this, this.orderAgg);
        if(null != ((Consumer)spOrderGet.getSelectedItem())){
            String consumerid = ((Consumer)spOrderGet.getSelectedItem()).getConsumerid();
            outbillAgg.getIc_diroutbill().setConsumerid(consumerid);
            String consumername = ((Consumer)spOrderGet.getSelectedItem()).getName();
            outbillAgg.getIc_diroutbill().setConsumername(consumername);
            String receiverOID = ((Consumer)spOrderGet.getSelectedItem()).getReceiverOID();
            outbillAgg.getIc_diroutbill().setReceiverOID(receiverOID);
        }

        orderAgg.getPu_order().setType("zc");
        new MaDAO().saveDirOutBillTemp(outbillAgg, this.orderAgg);
        setResult(RESULT_OK);
        Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
        this.finish();
        //MessageDialog.show(this,"准备打印");
        PrintUtil.print(this,PrintUtil.chgBillToString(outbillAgg.getIc_diroutbill(),outbillAgg.getIc_diroutbill_bs()),outbillAgg.getIc_diroutbill().getId());
    }

    public class ConsumerAdapter extends BaseAdapter {
        private List<Consumer> mList;
        private Context mContext;

        public ConsumerAdapter(Context pContext, List<Consumer> pList) {
            this.mContext = pContext;
            this.mList = pList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater= LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(R.layout.item_consumer, null);
            if(convertView!=null)
            {
                TextView _TextView1=(TextView)convertView.findViewById(R.id.tc_consumer);

                _TextView1.setText(mList.get(position).getName());

            }
            return convertView;
        }
    }
}
