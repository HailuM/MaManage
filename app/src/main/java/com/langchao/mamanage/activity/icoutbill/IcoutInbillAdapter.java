package com.langchao.mamanage.activity.icoutbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.order.Pu_order;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
public class IcoutInbillAdapter extends BaseAdapter {

    private List<Ic_inbill> inbills = new ArrayList<>();

    public IcoutInbillAdapter(Context context, List<Ic_inbill> list) {
        this.context = context;
        inbills = list;
    }

    private Context context;

    class ViewHolder {
        @ViewInject(R.id.tv_dir_out_order_id)
        public TextView tvOrderId;
        @ViewInject(R.id.tv_dir_out_order_time)
        public TextView tvOrderTime;
        @ViewInject(R.id.tv_dir_out_order_supplier)
        public TextView tvSupplier;
        @ViewInject(R.id.tv_dir_out_order_material)
        public TextView tvMaterial;
        @ViewInject(R.id.tv_dir_out_order_building)
        public TextView tvBuilding;

        public Ic_inbill inbill;


    }




    @Override
    public int getCount() {
        if(null == inbills)
            return 0;
        return inbills.size();
    }

    @Override
    public Object getItem(int position) {
        return inbills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _Holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_dir_out_order, null);
            x.view().inject(_Holder, convertView);
            convertView.setTag(_Holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewHolder  Holder = (ViewHolder) view.getTag();
                    Ic_inbill ic_in = Holder.inbill;
                    Intent intent = new Intent(context, IcoutInbillActivity.class);


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ic_inbill", ic_in);

                    intent.putExtras(bundle);

                    context.startActivity(intent);
                }
            });
        } else {
            _Holder = (ViewHolder) convertView.getTag();
        }

        Ic_inbill icInbill = inbills.get(position);
        if(null != icInbill){
            _Holder.tvOrderId.setText(icInbill.getNumber());
            _Holder.tvBuilding.setText(icInbill.getAddr());
            _Holder.tvMaterial.setText(icInbill.getMaterialDesc());
            _Holder.tvSupplier.setText(icInbill.getSupplier());
            _Holder.tvOrderTime.setText(icInbill.getDate().toString());
        }
        _Holder.inbill = icInbill;

        return convertView;
    }
}
