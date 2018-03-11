package com.inspur.sp.web.domain;

import java.io.Serializable;

/**
 * Created by My on 2017/6/22.
 */
public class Safe implements Serializable {


    public Safe() {
    }
    private  String id;
    private  String itemId;
    private  String itemValue ;
    private  String dateTime ;
    private  String itemType ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public  String toString(){

     return "Safe{" +
             "ID='" + id + '\'' +
             ", ITEM_ID='" + itemId + '\'' +
             ", ITEM_VALUE='" + itemValue + '\'' +
             ", DATETIME='" + dateTime + '\'' +
             ", ITEM_TYPE='" + itemType + '\'' +
             '}';
 }

 }


