package com.langchao.mamanage.db.order;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author: wyouflf
 * Date: 13-7-25
 * Time: 下午7:06
 */
@Table(name = "Pu_order")
public class Pu_order implements Serializable{

    /**
     * {
     * "tokenStr": "3958f542-c88e-4de0-9327-6f1b510bd6b4",
     * "details": [
     * {
     * "id": "441F5FBA-5978-4DFF-8859-18AA70886300",
     * "number": "201606020004_002",
     * "date": "2016/06/02",
     * "supplier": "",
     * "materialDesc": "大理石4,",
     * "Addr": "城山路83号"
     * }
     * ]
     * }
     */

    @Column(name = "id", isId = true)
    private String id;

    @Column(name = "number")
    private String number;
    @Column(name = "supplier")
    private String supplier;
    @Column(name = "materialDesc")
    private String materialDesc;
    @Column(name = "addr")
    private String Addr;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "type")
    private String type;

    //是否直出完成
    @Column(name = "zcwc")
    private boolean zcwc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")
    public String name;


    @Column(name = "time")
    private Date time;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    @Column(name = "date")

    private java.sql.Date date;

    public List<Pu_order_b> getChildren(DbManager db) throws DbException {
        return db.selector(Pu_order_b.class).where("orderid", "=", this.id).findAll();
    }

    // 一对一
    //public Pu_order_b getChild(DbManager db) throws DbException {
    //    return db.selector(Pu_order_b.class).where("parentId", "=", this.id).findFirst();
    //}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Pu_order{" +
                "id=" + id +
                ", name='" + name + '\'' +

                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
