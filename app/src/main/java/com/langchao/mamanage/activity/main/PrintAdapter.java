package com.langchao.mamanage.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.activity.icoutbill.IcoutInbillActivity;
import com.langchao.mamanage.activity.icoutbill.IcoutListActivity;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.lcprint.PrintUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
public class PrintAdapter extends BaseAdapter {

    public List<Ic_outbill> out = new ArrayList<>();
    public List<Ic_diroutbill> dirout = new ArrayList<>();


    private PrintActivity context;

    public PrintAdapter(PrintActivity printActivity, List<Ic_outbill> ic_outbills, List<Ic_diroutbill> ic_diroutbills) {
        this.context = printActivity;
        if(null != ic_outbills)
        {
            out = ic_outbills;
        }
        if(null != ic_diroutbills){

            dirout = ic_diroutbills;
        }
    }

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

        public Object outbill;


    }




    @Override
    public int getCount() {

        return out.size()+dirout.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < out.size()){
            return out.get(position);
        }

        return dirout.get(position-out.size());
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
                   Object bill =  ((ViewHolder)view.getTag()).outbill;
                    if(bill.getClass().equals(Ic_outbill.class)){
                        PrintUtil.print(context,(Ic_outbill)bill);
                    }else{
                        PrintUtil.print(context,(Ic_diroutbill) bill);
                    }
                }
            });

        } else {
            _Holder = (ViewHolder) convertView.getTag();
        }

        if(position < out.size()){
            Ic_outbill outbill =  out.get(position);
            _Holder.tvOrderId.setText(outbill.getNumber());
            _Holder.tvBuilding.setText(outbill.getAddr());
            _Holder.tvMaterial.setText(outbill.getMaterialDesc());
            _Holder.tvSupplier.setText(outbill.getSupplier());
            _Holder.tvOrderTime.setText(outbill.getDate().toString());
            _Holder.outbill = outbill;
        }else {

            Ic_diroutbill diroutbill = dirout.get(position - out.size());
            if (null != diroutbill) {
                _Holder.tvOrderId.setText(diroutbill.getNumber());
                _Holder.tvBuilding.setText(diroutbill.getAddr());
                _Holder.tvMaterial.setText(diroutbill.getMaterialDesc());
                _Holder.tvSupplier.setText(diroutbill.getSupplier());
                _Holder.tvOrderTime.setText(diroutbill.getDate().toString());
                _Holder.outbill = diroutbill;
            }
        }

        return convertView;
    }
}
