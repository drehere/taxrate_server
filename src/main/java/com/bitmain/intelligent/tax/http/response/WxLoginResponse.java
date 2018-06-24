package com.bitmain.intelligent.tax.http.response;

import com.google.gson.annotations.SerializedName;

/**
 * {
 * "access_token":"ACCESS_TOKEN",
 * "expires_in":7200,
 * "refresh_token":"REFRESH_TOKEN","openid":"OPENID",
 * "scope":"SCOPE"
 * }
 */
public class WxLoginResponse {
    private String openid;
    @SerializedName("session_key")
    private String sessionKey;

    public String getOpenid() {

        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
