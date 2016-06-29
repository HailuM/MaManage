package com.langchao.mamanage.converter;

import android.content.Context;

import com.langchao.mamanage.activity.dirout.DiroutOrderConfirmActivity;
import com.langchao.mamanage.activity.icoutbill.IcoutInbillConfirmActivity;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_agg;
import com.langchao.mamanage.db.ic_dirout.Ic_diroutbill_b;
import com.langchao.mamanage.db.ic_out.Ic_outbill;
import com.langchao.mamanage.db.ic_out.Ic_outbill_agg;
import com.langchao.mamanage.db.ic_out.Ic_outbill_b;
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
        ic_inbill_b.setPrice(pu_order_b.getPrice());

        ic_inbill_b.setSourceQty(num);


        return ic_inbill_b;

    }

    public static Ic_outbill_b convert(Ic_inbill_b inbill_b, double num, String id, String number) {
        Ic_outbill_b outbill_b = new Ic_outbill_b();
        outbill_b.setOrderid(id);
        outbill_b.setOrderentryid(UUID.randomUUID().toString());
        outbill_b.setSourceId(inbill_b.getOrderid());
        outbill_b.setSourcebId(inbill_b.getOrderentryid());
        if(null != inbill_b.getSourcebId() && inbill_b.getSourcebId() .trim().length() == 0){
            outbill_b.setSourcebId(inbill_b.getSourcebId());
        }

        outbill_b.setNumber(number);

        outbill_b.setNote(inbill_b.getNote());
        outbill_b.setBrand(inbill_b.getBrand());
        outbill_b.setModel(inbill_b.getModel());
        outbill_b.setUnit(inbill_b.getUnit());
        outbill_b.setName(inbill_b.getName());
        outbill_b.setPrice(inbill_b.getPrice());

        outbill_b.setSourceQty(num);


        return outbill_b;

    }

    public static Ic_inbill_agg convertOrderToInbill(Context content, Pu_order_agg orderAgg) {

        String id = UUID.randomUUID().toString();
        Ic_inbill_agg agg = new Ic_inbill_agg();
        Ic_inbill head = new Ic_inbill();
        Pu_order orderHead = orderAgg.getPu_order();
        head.setAddr(orderHead.getAddr());

        head.setProjectName(orderHead.getProjectName());
        head.setCompany(orderHead.getCompany());
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
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        return format.format(c1.getTime());
    }

    public static Ic_outbill_agg convertInbilToOut(IcoutInbillConfirmActivity content, Ic_inbill_agg inbillAgg) {

        Ic_outbill_agg agg = new Ic_outbill_agg();

        String id = UUID.randomUUID().toString();

        Ic_outbill head = new Ic_outbill();
        Ic_inbill orderHead = inbillAgg.getIc_inbill();
        head.setAddr(orderHead.getAddr());

        head.setProjectName(orderHead.getProjectName());
        head.setCompany(orderHead.getCompany());
        head.setDate(getDate());
        head.setId(id);
        head.setMaterialDesc(orderHead.getMaterialDesc());
        head.setNumber(MethodUtil.generateNo(content, "ICOUT"));
        head.setSupplier(orderHead.getSupplier());

        List<Ic_outbill_b> blist = new ArrayList<>();
        for (Ic_inbill_b item : inbillAgg.getIc_inbill_bList()) {

            if(item.getCurQty() > 0) {
                blist.add(convert(item, item.getCurQty(), id,head.getNumber()));
                item.setCkQty(item.getCkQty() + item.getCurQty());

            }

        }
        agg.setIc_outbill(head);
        agg.setIc_outbill_bs(blist);

        return agg;
    }

    public static Ic_diroutbill_agg convertPuorderToDirOut(DiroutOrderConfirmActivity content, Pu_order_agg orderAgg) {
        Ic_diroutbill_agg agg = new Ic_diroutbill_agg();
        String id = UUID.randomUUID().toString();

        Ic_diroutbill head = new Ic_diroutbill();
        Pu_order orderHead = orderAgg.getPu_order();
        head.setAddr(orderHead.getAddr());

        head.setProjectName(orderHead.getProjectName());
        head.setCompany(orderHead.getCompany());
        head.setDate(getDate());
        head.setId(id);
        head.setMaterialDesc(orderHead.getMaterialDesc());
        head.setNumber(MethodUtil.generateNo(content, "ICDIROUT"));
        head.setSupplier(orderHead.getSupplier());

        List<Ic_diroutbill_b> blist = new ArrayList<>();
        for (Pu_order_b item : orderAgg.getPu_order_bs()) {

            if(item.getCurQty() > 0) {
                blist.add(convertToDir(item, item.getCurQty(), id));
                item.setCkQty(item.getCkQty() + item.getCurQty());
            }

        }
        agg.setIc_diroutbill(head);
        agg.setIc_diroutbill_bs(blist);
        return agg;
    }

    public static Ic_diroutbill_b convertToDir(Pu_order_b pu_order_b, double num, String id) {
        Ic_diroutbill_b ic_inbill_b = new Ic_diroutbill_b();
        ic_inbill_b.setOrderid(id);
        ic_inbill_b.setOrderentryid(UUID.randomUUID().toString());
        ic_inbill_b.setSourceId(pu_order_b.getOrderid());
        ic_inbill_b.setSourcebId(pu_order_b.getOrderentryid());


        ic_inbill_b.setNote(pu_order_b.getNote());
        ic_inbill_b.setBrand(pu_order_b.getBrand());
        ic_inbill_b.setModel(pu_order_b.getModel());
        ic_inbill_b.setUnit(pu_order_b.getUnit());
        ic_inbill_b.setName(pu_order_b.getName());
        ic_inbill_b.setPrice(pu_order_b.getPrice());

        ic_inbill_b.setSourceQty(num);


        return ic_inbill_b;

    }
}
