package com.langchao.mamanage.db.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miaohl on 2016/6/26.
 */
public class Pu_order_agg implements Serializable{

    Pu_order pu_order;

    List<Pu_order_b> pu_order_bs;

    public Pu_order getPu_order() {
        return pu_order;
    }

    public void setPu_order(Pu_order pu_order) {
        this.pu_order = pu_order;
    }

    public List<Pu_order_b> getPu_order_bs() {
        return pu_order_bs;
    }

    public void setPu_order_bs(List<Pu_order_b> pu_order_bs) {
        this.pu_order_bs = pu_order_bs;
    }
}
