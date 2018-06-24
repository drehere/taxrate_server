package com.bitmain.intelligent.tax.Bean;

public class ResponseBean<T> {
    private int status;
    private String message;
    private T data;

    public ResponseBean() {
    }

    public ResponseBean(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseBean(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseBean newSuccess(T data) {
        ResponseBean responseBean=new ResponseBean(data);
        responseBean.setMessage("ok");
        return responseBean;
    }
    public static <T> ResponseBean newError(int status,String message) {
        return new ResponseBean(status,message);
    }
}
