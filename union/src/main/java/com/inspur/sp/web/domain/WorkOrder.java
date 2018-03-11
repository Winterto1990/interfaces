package com.inspur.sp.web.domain;

/**
 * Created by lixingkai on 2017/6/22.
 */
public class WorkOrder {
    private String orderId;
    private String orderName;
    private String orderDesc;
    private String orderCreateTime;
    private String orderDept;
    private String orderCrgency;
    private String orderProvider;
    private String orderType;
    private String orderPlanTime;
    private String orderDealTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrderDept() {
        return orderDept;
    }

    public void setOrderDept(String orderDept) {
        this.orderDept = orderDept;
    }

    public String getOrderCrgency() {
        return orderCrgency;
    }

    public void setOrderCrgency(String orderCrgency) {
        this.orderCrgency = orderCrgency;
    }

    public String getOrderProvider() {
        return orderProvider;
    }

    public void setOrderProvider(String orderProvider) {
        this.orderProvider = orderProvider;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderPlanTime() {
        return orderPlanTime;
    }

    public void setOrderPlanTime(String orderPlanTime) {
        this.orderPlanTime = orderPlanTime;
    }

    public String getOrderDealTime() {
        return orderDealTime;
    }

    public void setOrderDealTime(String orderDealTime) {
        this.orderDealTime = orderDealTime;
    }
}
