package com.langchao.mamanage.db.ic_out;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miaohl on 2016/6/27.
 */
public class Ic_outbill_agg implements Serializable{

    Ic_outbill ic_outbill;

    List<Ic_outbill_b> ic_outbill_bs;

    public Ic_outbill getIc_outbill() {
        return ic_outbill;
    }

    public void setIc_outbill(Ic_outbill ic_outbill) {
        this.ic_outbill = ic_outbill;
    }

    public List<Ic_outbill_b> getIc_outbill_bs() {
        return ic_outbill_bs;
    }

    public void setIc_outbill_bs(List<Ic_outbill_b> ic_outbill_bs) {
        this.ic_outbill_bs = ic_outbill_bs;
    }
}
