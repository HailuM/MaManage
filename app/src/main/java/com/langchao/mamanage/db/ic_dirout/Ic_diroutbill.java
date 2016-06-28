package com.langchao.mamanage.db.ic_dirout;

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
@Table(name = "ic_diroutbill")
public class Ic_diroutbill implements Serializable {

     
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


    @Column(name = "ProjectName")
    private String ProjectName;

    @Column(name = "Company")
    private String Company;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }
    @Column(name = "consumerid")
    private String consumerid;

    public String getConsumername() {
        return consumername;
    }

    public void setConsumername(String consumername) {
        this.consumername = consumername;
    }

    public String getConsumerid() {
        return consumerid;
    }

    public void setConsumerid(String consumerid) {
        this.consumerid = consumerid;
    }


    @Column(name = "isPrint")
    private boolean isPrint;

    public boolean isPrint() {
        return isPrint;
    }

    public void setPrint(boolean print) {
        isPrint = print;
    }

    @Column(name = "consumername")
    private String consumername;

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
    private String date;

    public List<Ic_diroutbill_b> getChildren(DbManager db) throws DbException {
        return db.selector(Ic_diroutbill_b.class).where("orderid", "=", this.id).findAll();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Pu_order{" +
                "id=" + id +


                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
