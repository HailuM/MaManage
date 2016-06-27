package com.langchao.mamanage.converter;

import android.content.Context;

import com.langchao.mamanage.db.icin.Ic_inbill;
import com.langchao.mamanage.db.icin.Ic_inbill_agg;
import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order;
import com.langchao.mamanage.db.order.Pu_order_agg;
import com.langchao.mamanage.db.order.Pu_order_b;
import com.langchao.mamanage.utils.MethodUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by miaohl on 2016/6/25.
 */
public class MaConvert {


    /**
     * 选择后转换订单到入库单
     *
     * @param pu_order_b
     * @param num
     * @return
     */
    public static Ic_inbill_b convert(Pu_order_b pu_order_b, double num, String id) {
        Ic_inbill_b ic_inbill_b = new Ic_inbill_b();
        ic_inbill_b.setOrderid(id);
        ic_inbill_b.setOrderentryid(UUID.randomUUID().toString());
        ic_inbill_b.setSourceId(pu_order_b.getOrderid());
        ic_inbill_b.setSourcebId(pu_order_b.getOrderentryid());

        ic_inbill_b.setCkQty(0);
        ic_inbill_b.setNote(pu_order_b.getNote());
        ic_inbill_b.setBrand(pu_order_b.getBrand());
        ic_inbill_b.setModel(pu_order_b.getModel());
        ic_inbill_b.setUnit(pu_order_b.getUnit());
        ic_inbill_b.setName(pu_order_b.getName());


        ic_inbill_b.setSourceQty(num);


        return ic_inbill_b;

    }

    public static Ic_inbill_agg convertOrderToInbill(Context content, Pu_order_agg orderAgg) {

        String id = UUID.randomUUID().toString();
        Ic_inbill_agg agg = new Ic_inbill_agg();
        Ic_inbill head = new Ic_inbill();
        Pu_order orderHead = orderAgg.getPu_order();
        head.setAddr(orderHead.getAddr());


        head.setDate(getDate());
        head.setId(id);
        head.setMaterialDesc(orderHead.getMaterialDesc());
        head.setNumber(MethodUtil.generateNo(content, "ICIN"));
        head.setSupplier(orderHead.getSupplier());

        List<Ic_inbill_b> blist = new ArrayList<>();
        for (Pu_order_b item : orderAgg.getPu_order_bs()) {

            if(item.getCurQty() > 0) {
                blist.add(convert(item, item.getCurQty(), id));
                item.setRkQty(item.getRkQty() + item.getCurQty());
            }

        }
        agg.setIc_inbill(head);
        agg.setIc_inbill_bList(blist);
        return agg;
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        return format.format(c1.getTime());
    }
}
