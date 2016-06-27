package com.langchao.mamanage.activity.icinbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.db.order.Pu_order;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
public class IcinOrderAdapter extends BaseAdapter {

    public List<Pu_order> pu_orderList = new ArrayList<>();

    public IcinOrderAdapter(IcinListActivity context, List<Pu_order> list) {
        this.context = context;
        pu_orderList = list;
    }

    private IcinListActivity context;

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

        public Pu_order pu_order;


    }




    @Override
    public int getCount() {
        if(null == pu_orderList)
            return 0;
        return pu_orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return pu_orderList.get(position);
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
                    Pu_order pu_order = Holder.pu_order;
                    Intent intent = new Intent(context, IcinOrderActivity.class);


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", pu_order);

                    intent.putExtras(bundle);

                    context.startActivityForResult(intent,0);
                }
            });
        } else {
            _Holder = (ViewHolder) convertView.getTag();
        }

        Pu_order order = pu_orderList.get(position);
        if(null != order){
            _Holder.tvOrderId.setText(order.getNumber());
            _Holder.tvBuilding.setText(order.getAddr());
            _Holder.tvMaterial.setText(order.getMaterialDesc());
            _Holder.tvSupplier.setText(order.getSupplier());
            _Holder.tvOrderTime.setText(order.getDate().toString());
        }
        _Holder.pu_order = order;

        return convertView;
    }
}
