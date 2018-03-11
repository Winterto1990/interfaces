package com.inspur.sp.web.domain;

import java.io.Serializable;

/**
 * Created by xingwentao on 2017/6/22.
 */
public class Infrastructure implements Serializable {
    private String id;
    private String itemid;
    private String itemvalue;
    private String datetime;
    private String itemtype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemvalue() {
        return itemvalue;
    }

    public void setItemvalue(String itemvalue) {
        this.itemvalue = itemvalue;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}
