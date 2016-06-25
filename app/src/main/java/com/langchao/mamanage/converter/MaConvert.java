package com.langchao.mamanage.converter;

import com.langchao.mamanage.db.icin.Ic_inbill_b;
import com.langchao.mamanage.db.order.Pu_order_b;

/**
 * Created by miaohl on 2016/6/25.
 */
public class MaConvert {



    /**
     * 选择后转换订单到入库单
     * @param pu_order_b
     * @param num
     * @return
     */
    public Ic_inbill_b convert(Pu_order_b pu_order_b, int num){
        Ic_inbill_b ic_inbill_b = new Ic_inbill_b();

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
}
