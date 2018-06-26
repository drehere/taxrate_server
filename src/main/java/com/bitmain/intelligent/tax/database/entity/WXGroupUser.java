package com.bitmain.intelligent.tax.database.entity;

import java.util.Date;

public class WXGroupUser {
    private long id;
    private long groupID;
    private long userID;
    private Date insertTime;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}

