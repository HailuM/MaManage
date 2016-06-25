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

package com.langchao.mamanage.db.consumer;


import com.langchao.mamanage.db.order.Pu_order;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Author: wyouflf
 * Date: 13-7-29
 * Time: 下午5:04
 */
@Table(name = "consumer")
public class Consumer {




    /**
     * [
     {
     "Orderid": "00021872-0000-0000-0000-00007c7e9d71",
     "consumerid": "10001",
     "Name": "南通建筑总承包有限公司"
     }
     ]
     */
    @Column(name = "id", isId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "consumerid")
    private String consumerid;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getConsumerid() {
        return consumerid;
    }

    public void setConsumerid(String consumerid) {
        this.consumerid = consumerid;
    }

    @Column(name = "name")

    private String Name;


    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        this.Orderid = orderid;
    }



    @Column(name = "Orderid" /*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private String Orderid; // 外键表id




    public Pu_order getParent(DbManager db) throws DbException {
        return db.findById(Pu_order.class, Orderid);
    }




    @Override
    public String toString()  {
        return "";
    }
}
