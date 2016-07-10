package com.langchao.mamanage.activity.icoutbill;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.langchao.mamanage.R;
import com.langchao.mamanage.common.MaConstants;
import com.langchao.mamanage.db.icin.Ic_inbill_b; 
import com.langchao.mamanage.dialog.AlertForResult;
import com.langchao.mamanage.dialog.PopCallBack;
import com.langchao.mamanage.utils.MathUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wongsuechang on 2016/6/26.
 */
public class IcoutConfirmAdapter extends BaseAdapter {

    private IcoutInbillConfirmActivity context;
    List<Ic_inbill_b> blist = new ArrayList<>();



    public IcoutConfirmAdapter(IcoutInbillConfirmActivity diroutOrderActivity, List<Ic_inbill_b> list) {
        context = diroutOrderActivity;
        blist = list;
    }

    class ViewHolder {
        @ViewInject(R.id.img_m_add)
        ImageView imgAdd;
        @ViewInject(R.id.tv_dir_out_order_m_name)
        TextView tvName;
        @ViewInject(R.id.tv_dir_out_order_m_add)
        TextView tvAdd;
        @ViewInject(R.id.et_dir_out_order_m_num)
        EditText tvNum;
        @ViewInject(R.id.tv_dir_out_order_m_del)
        TextView tvDel;
        @ViewInject(R.id.tv_dir_out_order_m_model)
        TextView tvModel;
        @ViewInject(R.id.tv_dir_out_order_m_unit)
        TextView tvUnit;
        @ViewInject(R.id.tv_dir_out_order_m_brand)
        TextView tvBrand;
        @ViewInject(R.id.tv_dir_out_order_m_note)
        TextView tvNote;
        @ViewInject(R.id.tv_dir_out_order_m_qty)
        TextView leftQty;
        @ViewInject(R.id.tv_dir_out_order_left)
        TextView tvleft;



        public Ic_inbill_b inbill_b;

        public BaseAdapter baseAdapter;

        @Event(value = {R.id.et_dir_out_order_m_num }, type = View.OnClickListener.class)
        private void numClick(View v){
            AlertForResult.popUp( inbill_b.getCurQty(),context,new PopCallBack() {
                @Override
                public void setNum(double num) {
                    if(num > (inbill_b.getSourceQty()-inbill_b.getCkQty())){
                        num =inbill_b.getSourceQty()-inbill_b.getCkQty();
                    }
                    if(num > 0) {
                        inbill_b.setCurQty(num);

                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Event(value = {R.id.tv_dir_out_order_m_add }, type = View.OnClickListener.class)
        private void add(View v){
            if(inbill_b.getCurQty() < (inbill_b.getSourceQty()-inbill_b.getCkQty())) {
                inbill_b.setCurQty(inbill_b.getCurQty() + 1);
                baseAdapter.notifyDataSetChanged();

            }
        }


        @Event(value = {R.id.tv_dir_out_order_m_del }, type = View.OnClickListener.class)
        private void sub(View v){
            if(inbill_b.getCurQty() > 1) {
                inbill_b.setCurQty(inbill_b.getCurQty() - 1);
                baseAdapter.notifyDataSetChanged();

            }
        }

        @Event(value = {R.id.img_m_add }, type = View.OnClickListener.class)
        private void del(View v){

            blist.remove(inbill_b);
            baseAdapter.notifyDataSetChanged();

            notice(inbill_b);
            context.updateTotal(blist.size());
        }

        private void notice(Ic_inbill_b inbill_b){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ic_inbill", inbill_b);

            intent.putExtras(bundle);
            intent.setAction(MaConstants.FRESH); // 说明动作
            context.sendBroadcast(intent);// 该函数用于发送广播
        }
    }


    @Override
    public int getCount() {
        if(null == blist)
            return 0;
        return blist.size();
    }

    @Override
    public Object getItem(int position) {
        return blist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder _Holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(x.app());
            convertView = layoutInflater.inflate(R.layout.item_dir_out_order_material, null);
            x.view().inject(_Holder, convertView);
            convertView.setTag(_Holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else {
            _Holder = (ViewHolder) convertView.getTag();
        }

        Ic_inbill_b order_b = blist.get(position);
        _Holder.imgAdd.setImageResource(R.mipmap.del);
        _Holder.tvBrand.setText(order_b.getBrand());
        _Holder.leftQty.setText(MathUtils.scaleDouble4(order_b.getSourceQty()-order_b.getCkQty()-order_b.getCurQty() ) );
        _Holder.tvModel.setText(order_b.getModel());
        _Holder.tvName.setText(order_b.getName());
        _Holder.tvNum.setText(MathUtils.scaleDouble(order_b.getCurQty()));
        _Holder.tvNote.setText(order_b.getNote());
        _Holder.tvUnit.setText(order_b.getUnit());
        //_Holder.tvleft.setVisibility(View.INVISIBLE);
        //_Holder.leftQty.setVisibility(View.INVISIBLE);
        _Holder.inbill_b = order_b;
        _Holder.baseAdapter = this;
        return convertView;
    }



}
