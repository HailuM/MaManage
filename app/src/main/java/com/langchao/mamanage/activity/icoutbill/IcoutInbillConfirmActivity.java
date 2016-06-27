package com.langchao.mamanage.activity.icoutbill;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.converter.MaConvert;
import com.langchao.mamanage.db.MaDAO;
import com.langchao.mamanage.db.consumer.Consumer;
import com.langchao.mamanage.db.ic_out.Ic_outbill_agg;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_agg;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_agg;

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
public class IcoutInbillConfirmActivity extends AppCompatActivity {

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
    @ViewInject(R.id.tv_dir_out_get)
    TextView tvOrderGet;//联系人
    @ViewInject(R.id.lv_dir_out_m)
    ListView lvOrderMaterial;
    @ViewInject(R.id.img_dir_out_choose)
    ImageView imgOrderChoose;
    @ViewInject(R.id.tv_dir_out_choose)
    TextView tvOrderChoose;


    @ViewInject(R.id.textViewTitle)
    private TextView textViewTitle;
    IcoutConfirmAdapter adapter = null;


    Ic_inbill_agg inbillAgg = null;

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


        Ic_outbill_agg outbillAgg = MaConvert.convertInbilToOut(this, this.inbillAgg);
        if(null != ((Consumer)spOrderGet.getSelectedItem())){
            String consumerid = ((Consumer)spOrderGet.getSelectedItem()).getConsumerid();
            String consumername = ((Consumer)spOrderGet.getSelectedItem()).getName();
            outbillAgg.getIc_outbill().setConsumerid(consumerid);
            outbillAgg.getIc_outbill().setConsumername(consumername);
        }

        new MaDAO().saveOutBill(outbillAgg, this.inbillAgg);
        setResult(RESULT_OK);
        this.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        textViewTitle.setText("出库确认");

        inbillAgg = (Ic_inbill_agg) this.getIntent().getExtras().getSerializable("ic_inbill");


        Ic_inbill order = inbillAgg.getIc_inbill();

        tvOrderNo.setText(order.getNumber());
        tvOrderBuild.setText(order.getAddr());

        List<Ic_inbill_b> list = inbillAgg.getIc_inbill_bList();
        List<Consumer> consumers = null;
        try {
            consumers = new MaDAO().findConsumers(order.getId());
            if(null == consumers || consumers.size() == 0){
                consumers = new MaDAO().findConsumers(list.get(0).getSourceId());
            }
            spOrderGet.setAdapter(new ConsumerAdapter(this, consumers));
        } catch (DbException e) {
            e.printStackTrace();
        }



        tvOrderChoose.setText("已选品种：" + list.size());


        adapter = new IcoutConfirmAdapter(this, list);
        lvOrderMaterial.setAdapter(adapter);
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
            LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
            convertView = _LayoutInflater.inflate(R.layout.item_consumer, null);
            if (convertView != null) {
                TextView _TextView1 = (TextView) convertView.findViewById(R.id.tc_consumer);

                _TextView1.setText(mList.get(position).getName());

            }
            return convertView;
        }
    }
}
