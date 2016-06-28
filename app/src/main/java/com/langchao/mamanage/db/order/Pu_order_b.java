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

package com.langchao.mamanage.db.order;


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
@Table(name = "pu_order_b")
public class Pu_order_b implements Serializable{




    /**
     * [
     {
     "orderid": "441F5FBA-5978-4DFF-8859-18AA70886300",
     "orderentryid": "97b8ce05-e93d-445a-bcc2-3fce08f5c6eb",
     "Name": "大理石4",
     "model": "200x68",
     "unit": "块",
     "brand": "美耐德4",
     "note": "4",
     "sourceQty": "15.0000",
     "limitQty": "315.0000"
     }
     ]
     */
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


    @Column(name = "limitQty")
    private double limitQty;

    //已出数量  直出
    @Column(name = "ckQty")
    private double ckQty;

    //已入库数量
    @Column(name = "rkQty")
    private double rkQty;

    private int position;//当前位置 LIST中

    public int getPosition() {
        return position;
    }

    public void setPosition(int cusor) {
        this.position = cusor;
    }

    //当前选择数量
    private double curQty;

    public double getCurQty() {
        return curQty;
    }

    public void setCurQty(double curQty) {
        this.curQty = curQty;
    }


    private double leftQty;

    public double getCkQty() {
        return ckQty;
    }

    public void setCkQty(double ckQty) {
        this.ckQty = ckQty;
    }

    public double getRkQty() {
        return rkQty;
    }

    public void setRkQty(double rkQty) {
        this.rkQty = rkQty;
    }



    public double getLimitQty() {
        return limitQty;
    }

    public void setLimitQty(double limitQty) {
        this.limitQty = limitQty;
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

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }



    @Column(name = "orderid" /*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private String orderid; // 外键表id




    public Pu_order getParent(DbManager db) throws DbException {
        return db.findById(Pu_order.class, orderid);
    }




    @Override
    public String toString()  {
        return orderentryid;
    }
}
