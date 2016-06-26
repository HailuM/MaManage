package com.langchao.mamanage.activity.icinbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.langchao.mamanage.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by wongsuechang on 2016/6/26.
 */
public class InBillOrderAdapter extends BaseAdapter {

    private Context context;

    class ViewHolder {
        @ViewInject(R.id.tv_dir_out_order_id)
        TextView tvOrderId;
        @ViewInject(R.id.tv_dir_out_order_time)
        TextView tvOrderTime;
        @ViewInject(R.id.tv_dir_out_order_supplier)
        TextView tvSupplier;
        @ViewInject(R.id.tv_dir_out_order_material)
        TextView tvMaterial;
        @ViewInject(R.id.tv_dir_out_order_building)
        TextView tvBuilding;

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
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
        } else {
            _Holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}
