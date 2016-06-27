package com.langchao.mamanage.db.ic_dirout;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miaohl on 2016/6/27.
 */
public class Ic_diroutbill_agg implements Serializable{

    Ic_diroutbill ic_diroutbill;

    List<Ic_diroutbill_b> ic_diroutbill_bs;

    public Ic_diroutbill getIc_diroutbill() {
        return ic_diroutbill;
    }

    public void setIc_diroutbill(Ic_diroutbill ic_diroutbill) {
        this.ic_diroutbill = ic_diroutbill;
    }

    public List<Ic_diroutbill_b> getIc_diroutbill_bs() {
        return ic_diroutbill_bs;
    }

    public void setIc_diroutbill_bs(List<Ic_diroutbill_b> ic_diroutbill_bs) {
        this.ic_diroutbill_bs = ic_diroutbill_bs;
    }
}
