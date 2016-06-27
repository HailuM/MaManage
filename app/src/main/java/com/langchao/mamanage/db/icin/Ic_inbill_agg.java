package com.langchao.mamanage.db.icin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miaohl on 2016/6/27.
 */
public class Ic_inbill_agg implements Serializable {

    Ic_inbill ic_inbill;

    List<Ic_inbill_b> ic_inbill_bList;

    public Ic_inbill getIc_inbill() {
        return ic_inbill;
    }

    public void setIc_inbill(Ic_inbill ic_inbill) {
        this.ic_inbill = ic_inbill;
    }

    public List<Ic_inbill_b> getIc_inbill_bList() {
        return ic_inbill_bList;
    }

    public void setIc_inbill_bList(List<Ic_inbill_b> ic_inbill_bList) {
        this.ic_inbill_bList = ic_inbill_bList;
    }
}
