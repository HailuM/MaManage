/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.langchao.mamanage.db.icin;


import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: wyouflf
 * Date: 13-7-29
 * Time: 下午5:04
 */
@Table(name = "ic_inbill_b")
public class Ic_inbill_b implements Serializable {




    /**
     *[
     {
     "orderid": "0010300E-0000-0000-0000-00007C8EC3F2",
     "OrderMXID": "00038f7c-0000-0002-0000-00007c7ec0eb",
     "Name": "混凝土",
     "model": "60x90",
     "unit": "千克",
     "brand": "创益",
     "note": "3",
     "sourceQty": "230.00000"
     }
     ]
     */
    @Column(name = "orderentryid", isId = true)
    private String orderentryid;

    @Column(name = "OrderMXID")
    private String OrderMXID;

    public String getOrderMXID() {
        return OrderMXID;
    }

    public void setOrderMXID(String orderMXID) {
        OrderMXID = orderMXID;
        if(null != orderMXID && orderMXID.trim().length() > 0)
        {
            setOrderentryid(orderMXID);
        }
    }

    @Column(name = "name")
    private String Name;


    @Column(name = "model")
    private String model;

    @Column(name = "unit")
    private String unit;


    @Column(name = "brand")
    private String brand;


    @Column(name = "note")
    private String note;

    @Column(name = "sourceQty")
    private double sourceQty;


    //已出数量
    @Column(name = "ckQty")
    private double ckQty;

    //来源子表
    @Column(name = "sourcebid")
    private String sourcebId;

    //来源主表
    @Column(name = "sourceid")
    private String sourceId;

    @Column(name = "createtype")
    private String createType;

    private double curQty;


    @Column(name = "time" )
    private Date time;

    public Date getTime() {
        if(null == time)
            return new Date();
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    @Column(name = "price")
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCurQty() {
        return curQty;
    }

    public void setCurQty(double curQty) {
        this.curQty = curQty;
    }

    Integer position;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourcebId() {
        return sourcebId;
    }

    public void setSourcebId(String sourcebId) {
        this.sourcebId = sourcebId;
    }

    public double getCkQty() {
        return ckQty;
    }

    public void setCkQty(double ckQty) {
        this.ckQty = ckQty;
    }

    public String getOrderentryid() {
        return orderentryid;
    }

    public void setOrderentryid(String orderentryid) {
        this.orderentryid = orderentryid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getSourceQty() {
        return sourceQty;
    }

    public void setSourceQty(double sourceQty) {
        this.sourceQty = sourceQty;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }



    @Column(name = "orderid" /*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private String orderid; // 外键表id




    public Ic_inbill getParent(DbManager db) throws DbException {
        return db.findById(Ic_inbill.class, orderid);
    }




    @Override
    public String toString()  {
        return orderentryid;
    }
}
