package com.bitmain.intelligent.tax.Bean;

public class ResponseBean<T> {
    private int status;
    private String message;
    private T data;

    public ResponseBean() {
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
        return new ResponseBean(data);
    }
}
