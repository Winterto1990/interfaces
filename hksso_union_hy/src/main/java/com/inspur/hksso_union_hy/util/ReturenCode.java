package com.inspur.hksso_union_hy.util;

import java.io.Serializable;

/**
 * Created by wanggangyfw on 2017/6/22.
 */
public class ReturenCode implements Serializable{

    private String statusCode;
    private String msg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ReturenCode{" +
                "statusCode='" + statusCode + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
