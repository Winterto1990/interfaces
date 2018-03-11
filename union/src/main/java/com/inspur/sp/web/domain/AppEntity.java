package com.inspur.sp.web.domain;

/**
 * Created by 周泽仁 on 2017/6/22.
 */
public class AppEntity {
    public AppEntity(){}

    private String ID ;
    private String ITEM_ID ;
    private String ITEM_VALUE ;
    private String DATETIME ;
    private String ITEM_TYPE ;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public void setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
    }

    public String getITEM_VALUE() {
        return ITEM_VALUE;
    }

    public void setITEM_VALUE(String ITEM_VALUE) {
        this.ITEM_VALUE = ITEM_VALUE;
    }

    public String getDATETIME() {
        return DATETIME;
    }

    public void setDATETIME(String DATETIME) {
        this.DATETIME = DATETIME;
    }

    public String getITEM_TYPE() {
        return ITEM_TYPE;
    }

    public void setITEM_TYPE(String ITEM_TYPE) {
        this.ITEM_TYPE = ITEM_TYPE;
    }

    @Override
    public String toString(){
        return "AppEntity{" +
                "itmAppId='" + ID + '\'' +
                ", itmAppCode='" + ITEM_ID + '\'' +
                ", itmAppName='" + ITEM_VALUE + '\'' +
                ", itmAppDesc='" + DATETIME + '\'' +
                ", itmAppDept='" + ITEM_TYPE + '\'' +
                '}';
    }
}
