package com.fabrizio.junit5.services.responses;

import java.util.ArrayList;

public class ServiceResponse {

    private String status;
    private Object data;
    private int code;
    private String message;

    public final static String STATUS_ERROR = "error";
    public final static String STATUS_SUCCESS = "success";

    public ServiceResponse(String status, Object data, int code, String message) {
        this.status = status;
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public ServiceResponse(ArrayList arrayList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ServiceResponse(){}
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}