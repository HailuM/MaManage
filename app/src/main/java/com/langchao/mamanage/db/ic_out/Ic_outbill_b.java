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

package com.langchao.mamanage.db.ic_out;


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
@Table(name = "ic_outbill_b")
public class Ic_outbill_b implements Serializable {


    public Integer getPrintcount() {
        return printcount;
    }

    public void setPrintcount(Integer printcount) {
        this.printcount = printcount;
    }

    private  Integer printcount;

    @Column(name = "orderentryid", isId = true)
    private String orderentryid;

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
    //已出数量
    @Column(name = "rkQty")
    private double rkQty;

    @Column(name = "price")
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    //来源子表
    @Column(name = "sourcebid")
    private String sourcebId;

    //来源主表
    @Column(name = "sourceid")
    private String sourceId;

    //领料商ID
    @Column(name = "consumerid")
    private String consumerId;

    @Column(name = "receivebid")
    private String receivebid;

    @Column(name = "wareentryid")
    private String wareentryid;

    public String getWareentryid() {
        return wareentryid;
    }

    public void setWareentryid(String wareentryid) {
        this.wareentryid = wareentryid;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }

    public String getReceivebid() {
        return receivebid;
    }

    public void setReceivebid(String receivebid) {
        this.receivebid = receivebid;
    }

    @Column(name = "receiveid")
    private String receiveid;

    //领料商名称
    @Column(name = "consumername")
    private String consumerName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //领料商名称
    @Column(name = "number")
    private String number;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
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

    public double getRkQty() {
        return rkQty;
    }

    public void setRkQty(double rkQty) {
        this.rkQty = rkQty;
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




    public Ic_outbill getParent(DbManager db) throws DbException {
        return db.findById(Ic_outbill.class, orderid);
    }




    @Override
    public String toString()  {
        return orderentryid;
    }
}
